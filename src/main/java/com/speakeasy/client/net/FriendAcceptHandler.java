package com.speakeasy.client.net;

import com.speakeasy.client.controllers.FriendsController;
import com.speakeasy.core.models.Friend;
import com.speakeasy.server.ChatServer;
import com.speakeasy.server.requests.Request;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class FriendAcceptHandler
{
    private final FriendsController controller;
    private final Friend friend;
    private boolean success;

    public FriendAcceptHandler(FriendsController controller, Friend friend)
    {
        this.controller = controller;
        this.friend = friend;
        this.success = false;
    }

    public FriendAcceptHandler execute()
    {
        try (Socket s = new Socket(InetAddress.getLocalHost(), ChatServer.chatPort))
        {
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            out.writeInt(Request.FRIEND_ACCEPT);
            out.writeInt(controller.getToken());

            if (in.readInt() != Handler.SUCCESS)
                return this;
            out.writeUTF(friend.getNickname());
            if (in.readInt() == Handler.SUCCESS)
            {
                success = true;
                friend.setPending(false);
            }
        } catch (IOException e)
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
