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
    private final JPasswordField repeatPasswordField;

    public LoginDialog(SpeakEasyFrame parent, FriendsController controller)
    {
        super(parent);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel(SpeakEasyFrame.iconImage);
        gbc.insets = new Insets(15, 5, 5, 5);
        gbc.gridwidth = 3;
        add(titleLabel, gbc);

        JPanel modePanel = new JPanel();
        modePanel.setOpaque(false);
        gbc.gridy = 1;
        add(modePanel, gbc);

        JButton loginModeButton = new JButton("Login");
        modePanel.add(loginModeButton);

        JButton registerModeButton = new JButton("Register");
        modePanel.add(registerModeButton);

        JLabel loginLabel = new JLabel("Username:");
        gbc.insets = new Insets(0, 5, 0, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        add(loginLabel, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridy = 3;
        add(passwordLabel, gbc);

        JLabel repeatPasswordLabel = new JLabel("Repeat Password:");
        gbc.gridy = 4;
        gbc.gridx = 0;
        add(repeatPasswordLabel, gbc);
        repeatPasswordLabel.setVisible(false);

        loginField = new JTextField("test");
        gbc.insets = new Insets(5, 5, 5, 20);
        loginField.setColumns(16);
        gbc.gridx = 2;
        gbc.gridy = 2;
        add(loginField, gbc);

        passwordField = new JPasswordField("secret");
        passwordField.setColumns(16);
        gbc.gridy = 3;
        gbc.gridx = 2;
        add(passwordField, gbc);

        repeatPasswordField = new JPasswordField("secret");
        repeatPasswordField.setColumns(16);
        gbc.gridy = 4;
        gbc.gridx = 2;
        add(repeatPasswordField, gbc);
        repeatPasswordField.setVisible(false);

        JPanel buttonPanel = new JPanel();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0;
        gbc.gridwidth = 3;
        gbc.gridy = 5;
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
                    controller.getChatController().setToken(token);
                    new FriendsRefreshHandler(controller).execute();
                }
            }).start();
        });
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        registerModeButton.addActionListener((ev) ->
        {
            repeatPasswordLabel.setVisible(true);
            repeatPasswordField.setVisible(true);
            confirmButton.setText("Register");
            revalidate();
            pack();
        });

        loginModeButton.addActionListener((ev) ->
            {
                repeatPasswordLabel.setVisible(false);
                repeatPasswordField.setVisible(false);
                confirmButton.setText("Login");
                revalidate();
                pack();
            });
        pack();
    }

    public Credentials getCredentials()
    {
        return new Credentials(loginField.getText().trim(),
                new String(passwordField.getPassword()));
    }
}
