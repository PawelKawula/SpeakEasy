package com.speakeasy.client.ui;

import com.speakeasy.client.controllers.FriendsController;
import com.speakeasy.core.models.Friend;
import com.speakeasy.utils.ChatConstants;

import javax.swing.*;
import java.awt.*;

public class FriendsInputPanel extends JPanel
{
    JTextField friendsInput;
    JButton friendsSubmit;
    FriendsController friendsController;

    public FriendsInputPanel(FriendsController friendsController)
    {
        this.friendsController = friendsController;
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        setLayout(new BorderLayout());

        friendsInput = new JTextField(12);
        friendsInput.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 0, 0, 6),
                friendsInput.getBorder()));
        add(friendsInput, BorderLayout.CENTER);

        ImageIcon addIcon = new ImageIcon(ChatConstants.RESOURCE_LOCATION + "images/plus.png");
        addIcon.setImage(addIcon.getImage().getScaledInstance(16,16, Image.SCALE_SMOOTH));

        friendsSubmit = new JButton(addIcon);
        friendsSubmit.setBorderPainted(false);
        friendsSubmit.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(-3, 15, -3, 15),
                friendsSubmit.getBorder()));
        friendsSubmit.addActionListener((event) ->
            {
                if (!friendsInput.getText().equals(""))
                    friendsController.addFriend(new Friend(friendsInput.getText(), null));
            });
        add(friendsSubmit, BorderLayout.EAST);
    }
}
