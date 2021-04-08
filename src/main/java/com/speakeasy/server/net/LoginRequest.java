package com.speakeasy.server.net;

import com.speakeasy.client.net.Handler;
import com.speakeasy.core.database.DatabaseConnection;
import com.speakeasy.core.models.Credentials;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class LoginRequest
{
    private final   DataInputStream in;
    private final   DataOutputStream out;
    private final   Map<Integer, String> map;
    private         Credentials cred;

    private static final String loginQuery =
            "SELECT * FROM Users WHERE username = ? AND password = ?";

    public LoginRequest(DataInputStream in, DataOutputStream out,
                        ConcurrentHashMap<Integer, String> map) throws IOException
    {
        this.in = in;
        this.out = out;
        this.map = map;
    }

    public void execute() throws SQLException, IOException
    {
        cred = new Credentials(in.readUTF(), in.readUTF());
        int token = Handler.DATABASE_FAILURE;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stat = conn.prepareStatement(loginQuery))
        {
            stat.setString(1, cred.getUserName());
            stat.setString(2, cred.getPassword());
            try (ResultSet result = stat.executeQuery())
            {
                if (result.next())
                    token = ThreadLocalRandom.current().
                            nextInt(Integer.MAX_VALUE - 20) + 20;
                else
                    token = Handler.QUERY_FAILURE;
            }
        }
        finally
        {
            send(token);
        }
    }

    private void send(int token) throws IOException
    {
        if (token != Handler.QUERY_FAILURE)
        {
            map.put(token, cred.getUserName());
            out.writeBoolean(true);
            out.writeInt(token);
        }
        else
            out.writeBoolean(false);
    }
}
