package com.speakeasy.server.net;

import com.speakeasy.client.net.Handler;
import com.speakeasy.core.database.DatabaseConnection;
import com.speakeasy.core.models.Credentials;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class LoginRequest extends SelectRequest
{
    public LoginRequest(Credentials credentials)
    {
        super(credentials);
    }

    public int execute() throws SQLException, ExecutionException, InterruptedException
    {
        Credentials cred = getCredentials();
        FutureTask<Integer> task = new FutureTask<>(() ->
            {
                int answer;
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stat = conn.prepareStatement(
                             "SELECT * FROM Users WHERE username = ? AND passwd = ?"))
                {
                    stat.setString(1, cred.getUserName());
                    stat.setString(2, cred.getPassword());
                    try (ResultSet result = stat.executeQuery())
                    {
                        if (result.next())
                            answer = Handler.SUCCESFUL_LOGIN;
                        else
                            answer = Handler.FAILED_LOGIN;
                    }
                }
                switch (answer)
                {
                    case Handler.FAILED_LOGIN -> System.out.println("Nieudane logowanie");
                    case Handler.SUCCESFUL_LOGIN -> System.out.println("Logowanie udane!");
                }
                return answer;
            });
        new Thread(task).start();
        return task.get();
    }
}
