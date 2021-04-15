package com.speakeasy.client.net;

import com.speakeasy.core.models.Friend;
import com.speakeasy.server.ChatServer;
import com.speakeasy.server.requests.Request;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.*;
import java.time.*;
import java.util.AbstractMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

public class MessagesRefreshHandler
{
    public final static int REFRESH_NEW = 0;
    public final static int REFRESH_OLD = 1;

    private final int token;
    private int type;
    private final Friend friend;
    private DataOutputStream out;
    private DataInputStream in;
    private Map<LocalDateTime, Map.Entry<Boolean, String>> fetchedMessages;

    public MessagesRefreshHandler(Friend friend, int token)
    {
        this.token = token;
        this.friend = friend;
        type = REFRESH_NEW;
        this.fetchedMessages = new TreeMap<>();
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public MessagesRefreshHandler execute()
    {
        try (Socket s = new Socket(InetAddress.getLocalHost(), ChatServer.chatPort))
        {
            in = new DataInputStream(s.getInputStream());
            out = new DataOutputStream(s.getOutputStream());

            out.writeInt(Request.MESSAGES_REFRESH);
            out.writeInt(token);

            if (in.readInt() == Handler.QUERY_FAILURE)
                return this;

            out.writeInt(type);
            LocalDateTime time;
            Stream<LocalDateTime> stream = friend.getCombinedMessages().keySet().stream();
            if (type == REFRESH_NEW)
                time = stream.reduce(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC),
                        (v1, v2) -> v1.isAfter(v2) ? v1 : v2);
            else
                time = stream.reduce(LocalDateTime.of(3000, 1, 1, 1, 1),
                        (v1, v2) -> v1.isBefore(v2) ? v1 : v2);
            ZonedDateTime zonedTime =
                    time.atZone(ZoneId.systemDefault());
            out.writeLong(zonedTime.toEpochSecond());
            out.writeInt(zonedTime.getNano());
            out.writeUTF(friend.getNickname());
            fetchMessages();
        }
        catch (IOException e)
        {
            System.out.println("Refresh Action encountered a problem");
            e.printStackTrace();
        }
        return this;
    }

    public Map<LocalDateTime, Map.Entry<Boolean, String>> getMessages()
    {
        return fetchedMessages;
    }

    private void fetchMessages() throws IOException
    {
        if (in.readInt() == Handler.SUCCESS)
        {
            int unread = in.readInt();
            for (int i = 0; i < unread; ++i)
            {
                LocalDateTime time = getTimestamp();
                String msg = in.readUTF();
                fetchedMessages.put(time,
                        new AbstractMap.SimpleEntry<>(in.readBoolean(), msg));
            }
        }
    }

    private LocalDateTime getTimestamp() throws IOException
    {
        long epochSecond = in.readLong();
        int nano = in.readInt();
        ZonedDateTime time = ZonedDateTime.ofInstant(Instant.ofEpochSecond(epochSecond, nano), ZoneId.systemDefault());
        return time.toLocalDateTime();
    }

    public static void main(String[] args) throws SQLException, FileNotFoundException
    {
//        String query = "INSERT INTO Messages(subject, create_date, creator_id, recipient_id, message_body) VALUES (?, ?, ?, ? , ?)";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement stat = conn.prepareStatement(query))
//        {
//            Scanner in = new Scanner(new FileInputStream("lorem.txt"));
//            LocalDateTime localTime = LocalDateTime.of(2009, 12, 14, 12, 14);
//            while (in.hasNext())
//            {
//                int id_1 = ThreadLocalRandom.current().nextInt(2) + 1;
//                int id_2 = id_1 == 1 ? 2 : 1;
//                String body = in.nextLine();
//                localTime = localTime.plusSeconds(ThreadLocalRandom.current().nextInt(3600 * 24) + 10);
//                stat.setString(1, "None");
//                stat.setTimestamp(2, Timestamp.from(localTime.toInstant(ZoneOffset.UTC)));
//                stat.setInt(3, id_1);
//                stat.setInt(4, id_2);
//                stat.setClob(5, new SerialClob(body.toCharArray()));
//                stat.execute();
//            }
//        }
//        ZonedDateTime nowUTC = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC"));
//        System.out.println(nowUTC);
        LocalDateTime time = LocalDateTime.now();
        ZonedDateTime ztime = time.atZone(ZoneId.systemDefault());
        System.out.println(ztime);
    }
}
