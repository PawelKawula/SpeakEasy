package com.speakeasy.client.net;

import com.speakeasy.core.models.Friend;
import com.speakeasy.server.ChatServer;
import com.speakeasy.server.requests.Request;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class FriendRemoveHandler
{
    int token;
    boolean success;
    Friend friend;

    public FriendRemoveHandler(Friend friend, int token)
    {
        this.token = token;
        this.friend = friend;
        this.success = false;
    }

    public FriendRemoveHandler execute()
    {
        try (Socket s = new Socket(InetAddress.getLocalHost(), ChatServer.chatPort))
        {
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            out.writeInt(Request.FRIEND_REMOVE);
            out.writeInt(token);

            if (in.readInt() != Handler.SUCCESS)
                return this;

            out.writeUTF(friend.getNickname());

            if (in.readInt() == Handler.SUCCESS)
            {
                success = true;
                new File(friend.getIconFile()).delete();

            }

        }  catch (IOException e)
        {
            e.printStackTrace();
        }
        return this;
    }

    public boolean isSuccess()
    {
        return this.success;
    }

}
