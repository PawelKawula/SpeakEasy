package com.speakeasy.server;

import com.speakeasy.core.models.Credentials;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer
{
    public static int chatPort = 8189;

    public static void main(String[] args) throws IOException
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
                        System.out.println("Getting new DataInputStream...");
                        DataInputStream dIn = new DataInputStream(s.getInputStream());
                        System.out.println("Done!");

                        System.out.println("Getting credentials");
                        String givenUname = dIn.readUTF();
                        String givenPasswd = dIn.readUTF();
                        System.out.println("Credentials received");

                        Credentials givenCredentials = new Credentials(
                                givenUname, givenPasswd);
                        Credentials credentials = new Credentials("user",
                            "secret");

                        DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
                        dOut.writeBoolean(givenCredentials.equals(credentials));
                    }
                    catch (IOException e)
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
