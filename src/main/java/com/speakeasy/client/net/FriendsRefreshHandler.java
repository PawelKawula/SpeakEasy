package com.speakeasy.client.net;

import com.speakeasy.client.controllers.FriendsController;
import com.speakeasy.core.models.Friend;
import com.speakeasy.server.ChatServer;
import com.speakeasy.server.requests.Request;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class FriendsRefreshHandler
{
    private FriendsController controller;
    private final Set<Friend> friends;

    public FriendsRefreshHandler(FriendsController controller)
    {
        this.controller = controller;
        friends = new HashSet<>();
    }

    public FriendsRefreshHandler execute()
    {
        try (Socket s = new Socket(InetAddress.getLocalHost(), ChatServer.chatPort))
        {
            System.out.println("Polaczono sie z serwerem");
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            out.writeInt(Request.FRIENDS_REFRESH);
            out.writeInt(controller.getToken());

            DataInputStream in = new DataInputStream(s.getInputStream());
            if (in.readInt() != Handler.SUCCESS)
                return this;
            if (in.readInt() == Handler.SUCCESS)
            {
                System.out.println("Udalo sie autoryzowac");
                int friendCount = in.readInt();
                for (int i = 0; i < friendCount; ++i)
                {
                    String name = in.readUTF();
                    int bytesCount = in.readInt();
                    //byte[] avatarBytes = in.readNBytes(bytesCount);
                    byte[] avatarBytes = new byte[bytesCount];
                    in.readFully(avatarBytes);
                    File imgFile = new File("cache/images/" + name + ".jpg");
                    boolean pending = in.readBoolean();
                    boolean meActive = in.readBoolean();
                    if (!imgFile.exists())
                    {
                        FileOutputStream fout = new FileOutputStream(imgFile);
                        fout.write(avatarBytes, 0, bytesCount);
                    }
                    friends.add(new Friend(name, imgFile.getPath(), pending, meActive));
                }
                controller.refreshFriends(friends);
            }
        }
        catch (IOException e)
        {
            System.out.println("Friends Refresh unsucessful");
            e.printStackTrace();
        }
        return this;
    }

    public Set<Friend> getFriends()
    {
        return friends;
    }
}
