package com.speakeasy.server;

import com.speakeasy.client.net.Handler;
import com.speakeasy.core.models.Credentials;
import com.speakeasy.server.net.FriendsRefreshRequest;
import com.speakeasy.server.net.LoginRequest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

import static com.speakeasy.server.net.Request.*;

public class ChatServer
{
    public static int chatPort = 8189;
    public static final ConcurrentHashMap<Integer, String> hostToUserMap =
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
                                String uname = in.readUTF();
                                String passwd = in.readUTF();
                                Credentials credentials = new Credentials(uname, passwd);
                                int answer = new LoginRequest(credentials).execute();
                                if (answer == Handler.DATABASE_FAILURE)
                                {
                                    System.out.println("failur");
                                    return;
                                }
                                if (answer == Handler.SUCCESS)
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
                            case FRIENDS_REFRESH:
                                long start = System.currentTimeMillis();
                                int token = in.readInt();
                                String user = hostToUserMap.getOrDefault(token, null);
                                System.out.println("user: " + user);
                                if (user != null)
                                {
                                    System.out.println("Autoryzacja uzytkownika");
                                    out.writeInt(Handler.SUCCESS);
                                    FriendsRefreshRequest request = new FriendsRefreshRequest(out, user);
                                    if (request.execute() != Handler.DATABASE_FAILURE)
                                        request.send();
                                }
                                System.out.println(System.currentTimeMillis());
                                break;
                            case MESSAGES_REFRESH:
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
