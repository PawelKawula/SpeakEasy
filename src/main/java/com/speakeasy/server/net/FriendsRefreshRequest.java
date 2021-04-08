package com.speakeasy.server.net;

import com.speakeasy.client.net.Handler;
import com.speakeasy.core.database.DatabaseConnection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

public class FriendsRefreshRequest
{
    private final   DataOutputStream out;
    private final   DataInputStream in;
    private final   Map<String, byte[]> friends;
    private final   Map<Integer, String> hostMap;

    private final static String findFriendsQuery =
            "SELECT Users.username, Users.avatar "
    + "FROM Friendships, Users, (SELECT id FROM Users WHERE username = ?) AS fID WHERE "
    + "(Friendships.active = fID.id OR Friendships.passive = fID.id) "
    + "AND (Users.id <> Friendships.active AND Users.id <> Friendships.passive)";

    public FriendsRefreshRequest(DataInputStream in, DataOutputStream out,
                                 ConcurrentHashMap<Integer, String> hostMap)
    {
        this.out = out;
        this.in = in;
        this.hostMap = hostMap;
        this.friends = new TreeMap<>();
    }

    public void execute() throws ExecutionException, InterruptedException, IOException, SQLException
    {
        String username = hostMap.get(in.readInt());
        boolean success = false;

        if (username == null)
        {
            out.writeInt(Handler.QUERY_FAILURE);
            return;
        }
        out.writeInt(Handler.SUCCESS);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement findFriendsStatement = conn.prepareStatement(findFriendsQuery))
        {
            findFriendsStatement.setString(1, username);
            try (ResultSet set = findFriendsStatement.executeQuery())
            {
                while (set.next())
                {
                    String uname = set.getString("username");
                    System.out.println("Fetching friend " + uname);
                    Blob blob = set.getBlob("avatar");
                    byte[] bytes = blob.getBinaryStream().readAllBytes();
                    friends.put(uname, bytes);
                }
            }
            success = true;
        }
        finally
        {
            send(success);
        }
    }

    private void send(boolean success) throws IOException
    {
        if (success)
        {
            out.writeInt(Handler.SUCCESS);
            Set<Map.Entry<String, byte[]>> friendsPackets = friends.entrySet();
            System.out.println("Liczba znajomych: " + friendsPackets.size());
            out.writeInt(friendsPackets.size());
            System.out.println("Wczesniej nie ma bledu");
            for (Map.Entry<String, byte[]> f : friendsPackets)
            {
                out.writeUTF(f.getKey());
                int bytesCount = f.getValue().length;
                out.writeInt(bytesCount);
                out.write(f.getValue(), 0, bytesCount);
            }
        }
        else
            out.writeInt(Handler.DATABASE_FAILURE);
    }
}
