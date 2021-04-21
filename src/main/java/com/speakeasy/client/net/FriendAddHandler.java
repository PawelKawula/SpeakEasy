package com.speakeasy.client.net;

import com.speakeasy.core.models.Friend;
import com.speakeasy.server.ChatServer;
import com.speakeasy.server.requests.Request;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class FriendAddHandler
{
    private final int token;
    private final String name;
    private Friend friend;
    private boolean success;

    public FriendAddHandler(int token, String name)
    {
        this.token = token;
        this.name = name;
        this.success = false;
    }

    public FriendAddHandler execute()
    {
        try (Socket s = new Socket(InetAddress.getLocalHost(), ChatServer.chatPort))
        {
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            out.writeInt(Request.FRIEND_ADD);
            out.writeInt(token);

            if (in.readInt() != Handler.SUCCESS)
                return this;
            out.writeUTF(name);
            if (in.readInt() == Handler.SUCCESS)
                success = true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return this;
    }

    public boolean isSuccess()
    {
        return success;
    }
}
