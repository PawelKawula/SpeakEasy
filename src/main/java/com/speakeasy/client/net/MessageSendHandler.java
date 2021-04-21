package com.speakeasy.client.net;

import com.speakeasy.client.controllers.ChatController;
import com.speakeasy.core.models.Friend;
import com.speakeasy.server.ChatServer;
import com.speakeasy.server.requests.Request;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class MessageSendHandler
{
    private final ChatController controller;
    private boolean success;

    public MessageSendHandler(ChatController controller)
    {
        this.controller = controller;
        success = false;
    }

    public MessageSendHandler execute(Friend friend, String message)
    {
        try (Socket s = new Socket(InetAddress.getLocalHost(), ChatServer.chatPort))
        {
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            out.writeInt(Request.MESSAGE_SEND);
            out.writeInt(controller.getToken());
            if (in.readInt() != Handler.SUCCESS)
                return this;
            out.writeUTF(friend.getNickname());
            out.writeUTF(message);
            if (in.readInt() == Handler.SUCCESS)
                success = true;
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
