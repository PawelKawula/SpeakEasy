package com.speakeasy.core.database;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection
{
    public void processRequest()
    {

    }

    public static Connection getConnection() throws SQLException
    {
        Properties props = new Properties();
        try (InputStream in =
                     Files.newInputStream(Paths.get("database.properties")))
        {
            props.load(in);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        String drivers = props.getProperty("jdbc.drivers");
        if (drivers != null) System.setProperty("jdbc.drivers", drivers);
        System.out.println(props.getProperty("jdbc.drivers"));
        String url = props.getProperty("jdbc.url");
        String username = props.getProperty("jdbc.username");
        String password = props.getProperty("jdbc.password");

        return DriverManager.getConnection(url, username, password);
    }
}
