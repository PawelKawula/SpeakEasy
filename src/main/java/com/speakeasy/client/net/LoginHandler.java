package com.speakeasy.client.net;

import com.speakeasy.core.models.Credentials;
import com.speakeasy.server.ChatServer;
import com.speakeasy.server.net.Request;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class LoginHandler
{
    Credentials credentials;

    public LoginHandler(Credentials credentials)
    {
        this.credentials = credentials;
    }

    public FutureTask<Integer> login() throws ExecutionException, InterruptedException
    {
        FutureTask<Integer> success = new FutureTask<>(() ->
            {
            try (Socket s = new Socket(InetAddress.getLocalHost(), ChatServer.chatPort))
                {
                    System.out.println("Polaczono z serwerem");
                    DataOutputStream out = new DataOutputStream(s.getOutputStream());

                    out.writeInt(Request.LOGIN_REQUEST.getValue());
                    out.writeUTF(credentials.getUserName());
                    out.writeUTF(credentials.getPassword());

                    DataInputStream in = new DataInputStream(s.getInputStream());
                    if (in.readBoolean())
                        return in.readInt();
                    else
                        return Handler.FAILED_LOGIN;
                }
            });
        new Thread(success).start();
        return success;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException
    {
        new LoginHandler(new Credentials("user", "secret")).login();
    }
}
