package com.speakeasy.server.requests;

import com.speakeasy.client.net.Handler;
import com.speakeasy.core.database.DatabaseConnection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Map;
import java.util.TreeMap;

public class FriendsRefreshRequest
{
    private final   DataOutputStream out;
    private final   DataInputStream in;
    private final   Map<String, byte[]> friends;
    private final   Map<Integer, String> hostMap;

    private final static String findFriendsQuery =
      "SELECT Users.username, Users.avatar "
    + "FROM Friendships, Users, (SELECT id FROM Users WHERE username = ?) AS fID WHERE "
    + "(Friendships.active_id = fID.id OR Friendships.passive_id = fID.id) "
    + "AND (Users.id <> Friendships.active_id AND Users.id <> Friendships.passive_id)";

    public FriendsRefreshRequest(DataInputStream in, DataOutputStream out,
                                 Map<Integer, String> hostMap)
    {
        this.out = out;
        this.in = in;
        this.hostMap = hostMap;
        this.friends = new TreeMap<>();
    }

    public void execute() throws IOException
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
        catch (SQLException e)
        {
            System.out.println("Blad bazy danych");
            e.printStackTrace();
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
            out.writeInt(friends.size());
            System.out.println("Wczesniej nie ma bledu");
            for (Map.Entry<String, byte[]> f : friends.entrySet())
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
