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

public class FriendRemoveRequest
{
    private final DataInputStream in;
    private final DataOutputStream out;
    private final Map<Integer, String> hostMap;

    private final static String findIdsQuery =
            "SELECT id, username FROM Users WHERE username = ? or username = ? ";

    private final static String deleteFriendshipQuery =
            "DELETE FROM Friendships WHERE (active_id = ? and passive_id = ?) " +
                    "OR (active_id = ? AND passive_id = ?)";

    public FriendRemoveRequest(DataInputStream in, DataOutputStream out, Map<Integer, String> hostMap)
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
             PreparedStatement deleteFriendshipStat = conn.prepareStatement(deleteFriendshipQuery))
        {
            findIdsStatement.setString(1, username);
            findIdsStatement.setString(2, fName);
            try (ResultSet set = findIdsStatement.executeQuery())
            {
                while (set.next())
                {
                    if (set.getString("username").equals(username))
                        fID = set.getInt("id");
                    if (set.getString("username").equals(fName))
                        uID = set.getInt("id");
                }
            }
            if (fID != 0 && uID != 0)
            {
                deleteFriendshipStat.setInt(1, fID);
                deleteFriendshipStat.setInt(2, uID);
                deleteFriendshipStat.setInt(3, uID);
                deleteFriendshipStat.setInt(4, fID);
                success = deleteFriendshipStat.executeUpdate() != 0;
            }
            send(success);
        } catch (SQLException e)
        {
            System.out.println("Database failure");
            e.printStackTrace();
        }
    }

    private void send(boolean success) throws IOException
    {
        if (success)
            out.writeInt(Handler.SUCCESS);
        else
            out.writeInt(Handler.QUERY_FAILURE);
    }
}