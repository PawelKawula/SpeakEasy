package com.speakeasy.server.requests;

import com.speakeasy.client.net.Handler;
import com.speakeasy.core.database.DatabaseConnection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.*;
import java.time.*;
import java.util.Map;

public class FriendAddRequest
{
    private final DataInputStream in;
    private final DataOutputStream out;
    private final Map<Integer, String> hostMap;

    private final static String findIdsQuery =
            "SELECT id, username FROM Users WHERE username = ? or username = ?";

    private final static String addFriendQuery =
            "INSERT INTO Friendships(active_id, passive_id, create_time, pending) VALUES(?, ?, ?, 1)";

    public FriendAddRequest(DataInputStream in, DataOutputStream out, Map<Integer, String> hostMap)
    {
        this.in = in;
        this.out = out;
        this.hostMap = hostMap;
    }

    public FriendAddRequest execute() throws IOException
    {
        String username = hostMap.get(in.readInt());
        boolean success = false;

        if (username == null)
        {
            out.writeInt(Handler.QUERY_FAILURE);
            return this;
        }
        out.writeInt(Handler.SUCCESS);

        String fName = in.readUTF();
        int fID = 0, uID = 0;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement findIdsStatement = conn.prepareStatement(findIdsQuery);
             PreparedStatement addFriendStatement = conn.prepareStatement(addFriendQuery))
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
                addFriendStatement.setInt(1, uID);
                addFriendStatement.setInt(2, fID);
                addFriendStatement.setDate(3,
                        Date.valueOf(LocalDateTime.now().atZone(ZoneId.of("UTC")).toLocalDate()));
                success = addFriendStatement.executeUpdate() != 0;
            }
            send(success);
        } catch (SQLException e)
        {
            System.out.println("Database failure");
            e.printStackTrace();
        }
        return this;
    }

    private void send(boolean success) throws IOException
    {
        if (success)
            out.writeInt(Handler.SUCCESS);
        else
            out.writeInt(Handler.QUERY_FAILURE);
    }
}
