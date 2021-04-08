package com.speakeasy.client.ui.main;

import com.speakeasy.client.controllers.FriendsController;
import com.speakeasy.client.net.FriendsRefreshHandler;
import com.speakeasy.client.net.Handler;
import com.speakeasy.client.net.LoginHandler;
import com.speakeasy.core.models.Credentials;
import com.speakeasy.core.models.Friend;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class LoginDialog extends JDialog
{
    private final JTextField loginField;
    private final JPasswordField passwordField;

    public LoginDialog(SpeakEasyFrame parent, FriendsController controller)
    {
        super(parent);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        setFont(new Font("Lato", Font.BOLD, 22));

        JLabel titleLabel = new JLabel(SpeakEasyFrame.iconImage);
        gbc.insets = new Insets(15, 5, 5, 5);
        titleLabel.setFont(new Font("Lato", Font.BOLD, 22));
        gbc.gridwidth = 3;
        add(titleLabel, gbc);

        JLabel loginLabel = new JLabel("Username: ");
        loginLabel.setFont(new Font("Lato", Font.BOLD, 22));
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 20, 5, 20);
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        add(loginLabel, gbc);

        JLabel passwordLabel = new JLabel("Password: ");
        passwordLabel.setFont(new Font("Lato", Font.BOLD, 22));
        gbc.gridy = 2;
        add(passwordLabel, gbc);

        loginField = new JTextField();
        gbc.insets = new Insets(5, 5, 5, 20);
        loginField.setFont(new Font("Lato", Font.BOLD, 22));
        loginField.setColumns(16);
        gbc.gridx = 2;
        gbc.gridy = 1;
        add(loginField, gbc);

        passwordField = new JPasswordField("password");
        passwordField.setFont(new Font("Lato", Font.BOLD, 22));
        passwordField.setColumns(16);
        gbc.gridy = 2;
        gbc.gridx = 2;
        add(passwordField, gbc);

        JPanel buttonPanel = new JPanel();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0;
        gbc.gridwidth = 3;
        gbc.gridy = 3;
        gbc.gridx = 0;
        add(buttonPanel, gbc);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Lato", Font.BOLD, 22));
        cancelButton.addActionListener((ev) ->
        {
            if (!parent.isVisible())
                System.exit(0);
        });

        JButton confirmButton = new JButton("Login");
        confirmButton.setFont(new Font("Lato", Font.BOLD, 22));
        confirmButton.addActionListener((ev) ->
        {
            System.out.println("klik");
            if (loginField.getText().trim().equals("") || passwordField.getPassword().length == 0)
                return;
            new Thread(() ->
            {
                System.out.println("Query logowania");
                int token = Handler.QUERY_FAILURE;
                try
                {
                    token = new LoginHandler(getCredentials()).execute();
                    if (token != Handler.QUERY_FAILURE)
                    {
                        parent.setVisible(true);
                        dispose();
                    }
                }
                catch (IOException e)
                {
                    System.out.println("Błąd połączenia przy logowaniu");
                    e.printStackTrace();
                }

                if (token != Handler.QUERY_FAILURE)
                {
                    try
                    {
                        FriendsRefreshHandler refreshHandler = new FriendsRefreshHandler(token);
                        if (refreshHandler.execute() != Handler.DATABASE_FAILURE)
                        {
                            ArrayList<Friend> friends = refreshHandler.getFriends();
                            for (Friend f : friends)
                                controller.addFriend(f);
                        }
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                dispose();
                parent.revalidate();
                parent.setVisible(true);
            }).start();
        });
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        pack();
    }

    public Credentials getCredentials()
    {
        return new Credentials(loginField.getText().trim(), new String(passwordField.getPassword()));
    }
}
