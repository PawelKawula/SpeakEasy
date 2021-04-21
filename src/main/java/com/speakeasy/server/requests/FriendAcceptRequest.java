package com.speakeasy.server.requests;

import com.speakeasy.client.net.Handler;
import com.speakeasy.core.database.DatabaseConnection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class FriendAcceptRequest
{
    private final DataInputStream in;
    private final DataOutputStream out;
    private final Map<Integer, String> hostMap;

    private final static String findIdsQuery =
            "SELECT id, username FROM Users WHERE username = ? or username = ? ";

    private final static String acceptFriendQuery =
            "UPDATE Friendships SET pending = 0 WHERE active_id = ? AND passive_id = ?";

    public FriendAcceptRequest(DataInputStream in, DataOutputStream out, Map<Integer, String> hostMap)
    {
        this.in = in;
        this.out = out;
        this.hostMap = hostMap;
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

        String fName = in.readUTF();
        int fID = 0, uID = 0;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement findIdsStatement = conn.prepareStatement(findIdsQuery);
             PreparedStatement acceptFriendStatement = conn.prepareStatement(acceptFriendQuery))
        {
            findIdsStatement.setString(1, username);
            findIdsStatement.setString(2, fName);
            try (ResultSet set = findIdsStatement.executeQuery())
            {
                while (set.next())
                {
                    if (set.getString("username").equals(username))
                        uID = set.getInt("id");
                    if (set.getString("username").equals(fName))
                        fID = set.getInt("id");
                }
            }
            if (fID != 0 && uID != 0)
            {
                acceptFriendStatement.setInt(1, fID);
                acceptFriendStatement.setInt(2, uID);
                success = acceptFriendStatement.executeUpdate() != 0;
            }
        } catch (SQLException e)
        {
            System.out.println("Database failure");
            e.printStackTrace();
        }
        send(success);
    }

    private void send(boolean success) throws IOException
    {
        if (success)
            out.writeInt(Handler.SUCCESS);
        else
            out.writeInt(Handler.QUERY_FAILURE);
    }
}
