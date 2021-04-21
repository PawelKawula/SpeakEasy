package com.speakeasy.server.requests;

import com.speakeasy.client.net.Handler;
import com.speakeasy.core.database.DatabaseConnection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FriendsRefreshRequest
{
    private final   DataOutputStream out;
    private final   DataInputStream in;
    private final   Set<FriendPacket> friends;
    private final   Map<Integer, String> hostMap;

    private final static String findUserIdQuery =
            "SELECT id FROM Users WHERE username = ?";

    private final static String findFriendsQuery =
            "SELECT Users.id, Users.username, Users.avatar, " +
                    "Friendships.pending, Friendships.active_id, Friendships.passive_id " +
            "FROM Users RIGHT OUTER JOIN Friendships ON " +
                "Friendships.active_id = Users.id OR Friendships.passive_id = Users.id " +
            "WHERE (active_id = ? OR passive_id = ?) AND username != ?";

    public FriendsRefreshRequest(DataInputStream in, DataOutputStream out,
                                 Map<Integer, String> hostMap)
    {
        this.out = out;
        this.in = in;
        this.hostMap = hostMap;
        this.friends = new HashSet<>();
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
             PreparedStatement findUserId = conn.prepareStatement(findUserIdQuery);
             PreparedStatement findFriendsStatement = conn.prepareStatement(findFriendsQuery))
        {
            int userId;
            findUserId.setString(1, username);
            try (ResultSet set = findUserId.executeQuery())
            {
                if(!set.next())
                    return;
                userId = set.getInt("id");
            }
            findFriendsStatement.setInt(1, userId);
            findFriendsStatement.setInt(2, userId);
            findFriendsStatement.setString(3, username);
            try (ResultSet set = findFriendsStatement.executeQuery())
            {
                while (set.next())
                {
                    String uname = set.getString("username");
                    Blob blob = set.getBlob("avatar");
                    byte[] bytes = blob.getBinaryStream().readAllBytes();
                    boolean pending = set.getInt("pending") == 1;
                    boolean meActive = set.getInt("id") != set.getInt("active_id");
                    friends.add(new FriendPacket(uname, bytes, pending, meActive));
                }
            }
            success = true;
        }
        catch (SQLException e)
        {
            System.out.println("Blad bazy danych");
            e.printStackTrace();
        }
        send(success);
    }

    private void send(boolean success) throws IOException
    {
        if (success)
        {
            out.writeInt(Handler.SUCCESS);
            out.writeInt(friends.size());
            for (FriendPacket f : friends)
            {
                out.writeUTF(f.name);
                int bytesCount = f.image.length;
                out.writeInt(bytesCount);
                out.write(f.image, 0, bytesCount);
                out.writeBoolean(f.pending);
                out.writeBoolean(f.meActive);
            }
        }
        else
            out.writeInt(Handler.DATABASE_FAILURE);
    }

    private class FriendPacket
    {
        public String name;
        public byte[] image;
        public boolean pending;
        public boolean meActive;

        public FriendPacket(String name, byte[] image, boolean pending, boolean meActive)
        {
            this.name = name;
            this.image = image;
            this.pending = pending;
            this.meActive = meActive;
        }
    }
}
