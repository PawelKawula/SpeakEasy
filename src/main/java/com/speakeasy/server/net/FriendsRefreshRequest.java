package com.speakeasy.server.net;

import com.speakeasy.client.net.Handler;
import com.speakeasy.core.database.DatabaseConnection;

import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

public class FriendsRefreshRequest
{
    private DataOutputStream out;
    private String username;
    private Map<String, byte[]> friends;

    private final static String findIDQuery =
            "SELECT id FROM Users WHERE username = ?";
    private final static String findFriendsQuery =
            "SELECT Users.username, Users.avatar "
    + "FROM Friendships, Users, (SELECT id FROM Users WHERE username = ?) AS fID WHERE "
    + "(Friendships.active = fID.id OR Friendships.passive = fID.id) "
    + "AND (Users.id <> Friendships.active AND Users.id <> Friendships.passive)";

    public FriendsRefreshRequest(DataOutputStream out, String username)
    {
        this.out = out;
        this.username = username;
        this.friends = new TreeMap<>();
    }

    public int execute() throws ExecutionException, InterruptedException, IOException
    {
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
            return Handler.SUCCESS;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return Handler.DATABASE_FAILURE;
        }
    }

    public void send() throws IOException, InterruptedException
    {
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
}
