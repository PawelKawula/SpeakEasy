package com.speakeasy.server.net;

import com.speakeasy.client.net.Handler;
import com.speakeasy.core.database.DatabaseConnection;
import com.speakeasy.core.models.Credentials;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class LoginRequest extends SelectRequest
{
    public LoginRequest(Credentials credentials)
    {
        super(credentials);
    }

    public int execute() throws SQLException, ExecutionException, InterruptedException
    {
        Credentials cred = getCredentials();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stat = conn.prepareStatement(
                     "SELECT * FROM Users WHERE username = ? AND password = ?"))
        {
            stat.setString(1, cred.getUserName());
            stat.setString(2, cred.getPassword());
            try (ResultSet result = stat.executeQuery())
            {
                if (result.next())
                    return Handler.SUCCESS;
                else
                    return Handler.QUERY_FAILURE;
            }
        }
        catch (SQLException e)
        {
            return Handler.DATABASE_FAILURE;
        }
    }
}
