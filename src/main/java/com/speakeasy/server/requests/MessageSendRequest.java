package com.speakeasy.server.requests;

import com.speakeasy.client.net.Handler;
import com.speakeasy.core.database.DatabaseConnection;

import javax.sql.rowset.serial.SerialClob;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Map;

public class MessageSendRequest
{
    private  final DataInputStream in;
    private final DataOutputStream out;
    private final Map<Integer, String> hostMap;

    private final static String findIdsQuery =
            "SELECT id, username FROM Users WHERE username = ? or username = ? ";

    private final static String sendMessageQuery =
            "INSERT INTO Messages(subject, create_date, creator_id, recipient_id, message_body)" +
            "VALUES(null, ?, ?, ?, ?)";

    public MessageSendRequest(DataInputStream in, DataOutputStream out, Map<Integer, String> hostMap)
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
        String message = in.readUTF();
        int fID = 0, uID = 0;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement findIdsStatement = conn.prepareStatement(findIdsQuery);
             PreparedStatement sendMessageStatement = conn.prepareStatement(sendMessageQuery))
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
            if (fID == 0 || uID == 0)
            {
                send(success);
                return;
            }
            LocalDateTime nowUTC = LocalDateTime.now().atZone(ZoneOffset.UTC).toLocalDateTime();
            sendMessageStatement.setTimestamp(1, Timestamp.valueOf(nowUTC));
            sendMessageStatement.setInt(2, uID);
            sendMessageStatement.setInt(3, fID);
            sendMessageStatement.setClob(4, new SerialClob(message.toCharArray()));
            success = sendMessageStatement.executeUpdate() != 0;
        } catch (SQLException e)
        {
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
