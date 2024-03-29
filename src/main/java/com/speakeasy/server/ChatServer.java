package com.speakeasy.server;

import com.speakeasy.server.requests.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.speakeasy.server.requests.Request.*;

public class ChatServer
{
    public static int chatPort = 8189;
    private static final Map<Integer, String> hostMap =
            new ConcurrentHashMap<>();

    public static void main(String[] args)
    {
        try (ServerSocket serverSocket = new ServerSocket(chatPort))
        {
            while (true)
            {
                System.out.println("Server waiting for request");
                Socket s = serverSocket.accept();
                System.out.println("Got a request!");
                new Thread(() ->
                {
                    try
                    {
                        DataInputStream in = new DataInputStream(s.getInputStream());
                        DataOutputStream out = new DataOutputStream(s.getOutputStream());
                        int requestType = in.readInt();
                        switch (requestType)
                        {
                            case LOGIN_REQUEST: new LoginRequest(in, out, hostMap).execute(); break;
                            case FRIENDS_REFRESH: new FriendsRefreshRequest(in, out, hostMap).execute(); break;
                            case MESSAGES_REFRESH: new MessagesRefreshRequest(in, out, hostMap).execute(); break;
                            case FRIEND_REMOVE: new FriendRemoveRequest(in, out, hostMap).execute(); break;
                            case FRIEND_ACCEPT: new FriendAcceptRequest(in, out, hostMap).execute(); break;
                            case FRIEND_ADD: new FriendAddRequest(in, out, hostMap).execute(); break;
                            case MESSAGE_SEND: new MessageSendRequest(in, out, hostMap).execute(); break;
                            default: System.out.println("Zly requestType");
                        }
                    }
                    catch (IOException e)
                    {
                        System.out.println("Utracono polaczenie z hostem");
                        e.printStackTrace();
                    }

                }).start();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
