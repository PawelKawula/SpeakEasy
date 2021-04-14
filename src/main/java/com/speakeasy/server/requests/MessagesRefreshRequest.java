package com.speakeasy.server.requests;

import com.speakeasy.client.net.Handler;
import com.speakeasy.core.database.DatabaseConnection;

import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.AbstractMap;
import java.util.Map;
import java.util.TreeMap;

public class MessagesRefreshRequest
{
    private final Map<Integer, String> hostMap;
    private final DataInputStream in;
    private final DataOutputStream out;
    private final Map<LocalDateTime, AbstractMap.SimpleEntry<Boolean, String>> newMessages;

    private final static String query =
            "SELECT recipient_ID, creator_id, Messages.create_date, message_body, subject, uID.id, fID.id " +
    "FROM Messages, Users, (Select id FROM Users WHERE username = ?) AS uID, " +
            	"(SELECT id FROM Users WHERE username = ?) as fID WHERE " +
        "((Messages.creator_ID = fID.id AND Messages.recipient_ID = uID.id) OR " +
            	"(Messages.creator_ID = uID.id AND Messages.recipient_id = fID.id) )  AND " +
        "(Users.id <> Messages.recipient_ID AND Users.id <> Messages.creator_id) AND " +
    "Messages.create_date > ?";


    public MessagesRefreshRequest(DataInputStream in, DataOutputStream out, Map<Integer, String> hostMap) throws IOException
    {
        this.hostMap = hostMap;
        this.newMessages = new TreeMap<>();
        this.in = in;
        this.out = out;
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

        long epoch = in.readLong();
        int nano = in.readInt();
        Timestamp timestamp =
                Timestamp.valueOf(LocalDateTime.ofEpochSecond(epoch, nano, ZoneOffset.UTC));
        String friendName = in.readUTF();
        System.out.println(friendName);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stat = conn.prepareStatement(query))
        {
            stat.setString(1, username);
            stat.setString(2, friendName);
            stat.setTimestamp(3, timestamp);
            try (ResultSet set = stat.executeQuery())
            {
                while (set.next())
                    fetchMessage(set);
                success = true;
            }
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
            out.writeInt(newMessages.size());
            for (Map.Entry<LocalDateTime, AbstractMap.SimpleEntry<Boolean, String>> f : newMessages.entrySet())
            {
                out.writeLong(f.getKey().atZone(ZoneOffset.UTC).toEpochSecond());
                out.writeInt(f.getKey().getNano());
                out.writeUTF(f.getValue().getValue());
                out.writeBoolean(f.getValue().getKey());
            }
        }
        else
            out.writeInt(Handler.QUERY_FAILURE);
    }

    private void fetchMessage(ResultSet set) throws SQLException, IOException
    {
        LocalDateTime timestamp =
                set.getTimestamp("create_date").toLocalDateTime();
        Clob body = set.getClob("message_body");
        String msg_body = new String(body.getAsciiStream().readAllBytes());
        boolean me = set.getInt("creator_id") == set.getInt("id");
        newMessages.put(timestamp, new AbstractMap.SimpleEntry<>(me, msg_body));
    }
}
