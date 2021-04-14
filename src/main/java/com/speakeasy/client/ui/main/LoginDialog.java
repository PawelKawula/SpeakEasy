package com.speakeasy.client.ui.main;

import com.speakeasy.client.controllers.FriendsController;
import com.speakeasy.client.net.FriendsRefreshHandler;
import com.speakeasy.client.net.Handler;
import com.speakeasy.client.net.LoginHandler;
import com.speakeasy.core.models.Credentials;

import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JDialog
{
    private final JTextField loginField;
    private final JPasswordField passwordField;

    public LoginDialog(SpeakEasyFrame parent, FriendsController controller)
    {
        super(parent);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel(SpeakEasyFrame.iconImage);
        gbc.insets = new Insets(15, 5, 5, 5);
        gbc.gridwidth = 3;
        add(titleLabel, gbc);

        JLabel loginLabel = new JLabel("Username: ");
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        add(loginLabel, gbc);

        JLabel passwordLabel = new JLabel("Password: ");
        gbc.gridy = 2;
        add(passwordLabel, gbc);

        loginField = new JTextField();
        gbc.insets = new Insets(5, 5, 5, 20);
        loginField.setColumns(16);
        gbc.gridx = 2;
        gbc.gridy = 1;
        add(loginField, gbc);

        passwordField = new JPasswordField("password");
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
        cancelButton.addActionListener((ev) ->
        {
            if (!parent.isVisible())
                System.exit(0);
        });

        JButton confirmButton = new JButton("Login");
        confirmButton.addActionListener((ev) ->
        {
            if (loginField.getText().trim().equals("") || passwordField.getPassword().length == 0)
                return;
            new Thread(() ->
            {
                LoginHandler loginHandler = new LoginHandler(getCredentials());
                loginHandler.execute();
                int token = loginHandler.getToken();
                if (token != Handler.QUERY_FAILURE)
                {
                    parent.setVisible(true);
                    dispose();
                    FriendsRefreshHandler handler = new FriendsRefreshHandler(token);
                    handler.execute(controller);
                    controller.getChatController().setToken(token);
                }




            }).start();
        });
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        pack();
    }

    public Credentials getCredentials()
    {
        return new Credentials(loginField.getText().trim(),
                new String(passwordField.getPassword()));
    }
}
