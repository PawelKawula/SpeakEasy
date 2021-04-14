package com.speakeasy.client.net;

import com.speakeasy.core.models.Friend;
import com.speakeasy.server.ChatServer;
import com.speakeasy.server.requests.Request;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.time.*;

public class MessagesRefreshHandler
{
    private final int token;
    private final Friend friend;
    private DataOutputStream out;
    private DataInputStream in;

    public MessagesRefreshHandler(Friend friend, int token)
    {
        this.token = token;
        this.friend = friend;
    }

    public void execute()
    {
        try (Socket s = new Socket(InetAddress.getLocalHost(), ChatServer.chatPort))
        {
            in = new DataInputStream(s.getInputStream());
            out = new DataOutputStream(s.getOutputStream());

            out.writeInt(Request.MESSAGES_REFRESH);
            out.writeInt(token);

            if (in.readInt() == Handler.QUERY_FAILURE)
                return;

            LocalDateTime time = friend.getCombinedMessages().keySet().stream()
                    .reduce(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC),
                            (val1, val2) -> val1.isAfter(val2) ? val1 : val2);
            System.out.println(time);
            out.writeLong(time.atZone(ZoneOffset.UTC).toEpochSecond());
            out.writeInt(time.getNano());
            out.writeUTF(friend.getNickname());
            getMessages();

        }
        catch (IOException e)
        {
            System.out.println("Refresh Action encountered a problem");
            e.printStackTrace();
        }
    }

    private void getMessages() throws IOException
    {
        if (in.readInt() == Handler.SUCCESS)
        {
            int unread = in.readInt();
            for (int i = 0; i < unread; ++i)
            {
                LocalDateTime time = getTimestamp();
                String msg = in.readUTF();
                if (in.readBoolean())
                    friend.addMyMessage(time, msg);
                else
                    friend.addFriendMessage(time, msg);
            }
        }
    }

    private LocalDateTime getTimestamp() throws IOException
    {
        long epochSecond = in.readLong();
        int nano = in.readInt();
        return LocalDateTime.ofEpochSecond(epochSecond, nano,
                ZoneId.systemDefault().getRules().getOffset(Instant.now()));
    }
}
