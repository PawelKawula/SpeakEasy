package com.speakeasy.server;

import com.speakeasy.client.net.Handler;
import com.speakeasy.core.models.Credentials;
import com.speakeasy.server.net.LoginRequest;
import com.speakeasy.server.net.Request;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

public class ChatServer
{
    public static int chatPort = 8189;
    public static final ConcurrentHashMap<Integer, String> hostToUserMap =
            new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException, ClassNotFoundException
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
                        Request requestType = Request.values()[in.readInt()];
                        switch (requestType)
                        {
                            case LOGIN_REQUEST:
                                String uname = in.readUTF();
                                String passwd = in.readUTF();
                                Credentials credentials = new Credentials(uname, passwd);
                                int answer = new LoginRequest(credentials).execute();
                                if (answer == Handler.DATABASE_FAILURE)
                                    return;
                                if (answer == Handler.SUCCESFUL_LOGIN)
                                {
                                    int id = ThreadLocalRandom.current().
                                            nextInt(Integer.MAX_VALUE - 1) + 1;
                                    hostToUserMap.put(id, uname);
                                    out.writeBoolean(true);
                                    out.writeInt(id);
                                }
                                else
                                    out.writeBoolean(false);
                                break;
                            case FRIEND_SELECT_REQUEST:
                                break;
                            case MESSAGES_SELECT_REQUEST:
                                break;
                        }
                    }
                    catch (IOException | SQLException | ExecutionException | InterruptedException e)
                    {
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
