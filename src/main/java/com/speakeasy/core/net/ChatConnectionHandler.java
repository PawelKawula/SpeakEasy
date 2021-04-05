package com.speakeasy.core.net;

import com.speakeasy.core.models.Credentials;
import com.speakeasy.server.ChatServer;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ChatConnectionHandler
{
    Credentials credentials;

    public ChatConnectionHandler(Credentials credentials)
    {
        this.credentials = credentials;
    }

    public FutureTask<Boolean> login() throws ExecutionException, InterruptedException
    {
        FutureTask<Boolean> success = new FutureTask<>(this::loginTask);
        new Thread(success).start();
        return success;
    }

    public Boolean loginTask() throws IOException
    {
        try (Socket s = new Socket(InetAddress.getLocalHost(), ChatServer.chatPort))
        {
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            out.writeUTF(credentials.getUserName());
            out.writeUTF(credentials.getPassword());

            DataInputStream in = new DataInputStream(s.getInputStream());
            return in.readBoolean();
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException
    {
    }
}
