package com.speakeasy.client.net;

import com.speakeasy.client.controllers.FriendsController;
import com.speakeasy.core.models.Friend;
import com.speakeasy.server.ChatServer;
import com.speakeasy.server.requests.Request;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class FriendsRefreshHandler
{
    private int token;
    private ArrayList<Friend> friends;

    public FriendsRefreshHandler(int token)
    {
        this.token = token;
        friends = new ArrayList<>();
    }

    public void execute(FriendsController controller)
    {
        try (Socket s = new Socket(InetAddress.getLocalHost(), ChatServer.chatPort))
        {
            System.out.println("Polaczono sie z serwerem");
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            out.writeInt(Request.FRIENDS_REFRESH);
            out.writeInt(token);

            DataInputStream in = new DataInputStream(s.getInputStream());
            if (in.readInt() == Handler.SUCCESS && in.readInt() == Handler.SUCCESS)
            {
                System.out.println("Udalo sie autoryzowac");
                int friendCount = in.readInt();
                System.out.println("Liczba znajomych: " + friendCount);
                for (int i = 0; i < friendCount; ++i)
                {
                    String name = in.readUTF();
                    int bytesCount = in.readInt();
                    System.out.println("Pobrano imie oraz rozmiar byte[] znajomego");
                    byte[] avatarBytes = in.readNBytes(bytesCount);
                    System.out.println("Pobrano avatar znajomego");
                    File imgFile = new File("cache/images/" + name + ".jpg");
                    if (!imgFile.exists())
                    {
                        FileOutputStream fout = new FileOutputStream(imgFile);
                        fout.write(avatarBytes, 0, bytesCount);
                    }
                    friends.add(new Friend(name, imgFile.getPath()));
                }
                refreshFriends(controller);
            }
        }
        catch (IOException e)
        {
            System.out.println("Friends Refresh unsucessful");
        }
    }

    private void refreshFriends(FriendsController controller)
    {
        for (Friend f : friends)
            controller.addFriend(f);
    }

    public ArrayList<Friend> getFriends()
    {
        return friends;
    }
}
