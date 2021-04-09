package com.speakeasy.client.net;

import com.speakeasy.core.models.Credentials;
import com.speakeasy.server.ChatServer;
import com.speakeasy.server.net.Request;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class LoginHandler
{
    Credentials credentials;
    int token;

    public LoginHandler(Credentials credentials)
    {
        this.credentials = credentials;
        token = Handler.QUERY_FAILURE;
    }

    public void execute()
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
                    token = in.readInt();
            }
        catch (IOException e)
        {
            System.out.println("Logging in not successful due to net error");
            e.printStackTrace();
        }
    }

    public int getToken()
    {
        return token;
    }
}
