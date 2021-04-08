package com.speakeasy.client.net;

import com.speakeasy.core.models.Credentials;
import com.speakeasy.server.ChatServer;
import com.speakeasy.server.net.Request;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

public class LoginHandler
{
    Credentials credentials;

    public LoginHandler(Credentials credentials)
    {
        this.credentials = credentials;
    }

    public int login() throws ExecutionException, InterruptedException, IOException
    {
        try (Socket s = new Socket(InetAddress.getLocalHost(), ChatServer.chatPort))
            {
                DataOutputStream out = new DataOutputStream(s.getOutputStream());

                out.writeInt(Request.LOGIN_REQUEST);
                out.writeUTF(credentials.getUserName());
                out.writeUTF(credentials.getPassword());
                System.out.println("wyslano");

                DataInputStream in = new DataInputStream(s.getInputStream());
                if (in.readBoolean())
                    return in.readInt();
                else
                    return Handler.QUERY_FAILURE;
            }
    }

    public static void main(String[] args)
    {
        new LoginHandler(new Credentials("user", "secret"));
    }
}
