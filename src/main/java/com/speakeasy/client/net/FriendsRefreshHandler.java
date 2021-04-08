package com.speakeasy.client.net;

import com.speakeasy.core.database.DatabaseConnection;
import com.speakeasy.core.models.Friend;
import com.speakeasy.server.ChatServer;
import com.speakeasy.server.net.Request;
import com.speakeasy.utils.ChatConstants;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class FriendsRefreshHandler
{
    private int token;
    private ArrayList<Friend> friends;

    public FriendsRefreshHandler(int token)
    {
        this.token = token;
        friends = new ArrayList<>();
    }

    public int execute()
    {
        try (Socket s = new Socket(InetAddress.getLocalHost(), ChatServer.chatPort))
        {
            System.out.println("Polaczono sie z serwerem");
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            out.writeInt(Request.FRIENDS_REFRESH);
            out.writeInt(token);

            DataInputStream in = new DataInputStream(s.getInputStream());
            if (in.readInt() == Handler.SUCCESS)
            {
                System.out.println("Udalo sie autoryzowac");
                int friendCount = in.readInt();
                System.out.println("Liczba znajomych: " + friendCount);
                for (int i = 0; i < friendCount; ++i)
                {
                    String name = in.readUTF();
                    int bytesCount = in.readInt();
                    System.out.println("Pobrano imie oraz rozmiar byte[] znajomego");
                    byte[] avatarBytes = in.readNBytes(bytesCount);
                    System.out.println("Pobrano avatar znajomego");
                    File imgFile = new File("cache/images/" + name + ".jpg");
                    if (!imgFile.exists())
                    {
                        FileOutputStream fout = new FileOutputStream(imgFile);
                        fout.write(avatarBytes, 0, bytesCount);
                    }
                    friends.add(new Friend(name, imgFile.getPath()));
                }
                return Handler.SUCCESS;
            } else
                return Handler.QUERY_FAILURE;
        }
        catch (IOException e)
        {
            return Handler.QUERY_FAILURE;
        }
    }

    public ArrayList<Friend> getFriends()
    {
        return friends;
    }

    public static void main(String[] args) throws SQLException, IOException
    {
        InputStream in = new FileInputStream(ChatConstants.RESOURCE_LOCATION + "images/womanface.jpg");
        String uname = "Ewa";
        String passwd = "secret";
        String query = "INSERT INTO Users(username, password, avatar) VALUES(?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stat = conn.prepareStatement(query))
        {
            stat.setString(1, uname);
            stat.setString(2, passwd);
            stat.setBlob(3, in);
            stat.executeUpdate();
        }

//        String query = "SELECT * FROM Users";
//        try (Connection conn = DatabaseConnection.getConnection();
//                PreparedStatement stat = conn.prepareStatement(query))
//        {
//            try (ResultSet result = stat.executeQuery())
//            {
//                int i = 1;
//                while (result.next())
//                {
//                    Blob blob = result.getBlob("avatar");
//                    int streamLength = (int) blob.length();
//                    byte[] in = blob.getBinaryStream().readAllBytes();
//                    FileOutputStream out;
//                    if (i == 1)
//                       out = new FileOutputStream("cache/test" + i + ".jpg");
//                    else
//                        out = new FileOutputStream("cache/test" + i + ".png");
//                    out.write(in, 0, streamLength);
//                }
//            }
//        }
//        JFrame frame = new JFrame("Picture test");
//        JPanel panel = new JPanel();
//        panel.setLayout(new BorderLayout());
//        JLabel label1 = new JLabel(new ImageIcon("cache/test1.jpg"));
//        JLabel label2 = new JLabel(new ImageIcon("cache/test2.png"));
//        frame.add(panel);
//        panel.add(label1, BorderLayout.WEST);
//        panel.add(label2, BorderLayout.EAST);
//        frame.pack();
//        frame.setVisible(true);
    }
}
