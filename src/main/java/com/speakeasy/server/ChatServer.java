package com.speakeasy.server;

import com.speakeasy.server.net.FriendsRefreshRequest;
import com.speakeasy.server.net.LoginRequest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.speakeasy.server.net.Request.*;

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
                            case LOGIN_REQUEST:
                                new LoginRequest(in, out, hostMap).execute();
                                break;
                            case FRIENDS_REFRESH:
                                new FriendsRefreshRequest(in, out, hostMap).execute();
                                break;
                            case MESSAGES_REFRESH:
                                break;
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