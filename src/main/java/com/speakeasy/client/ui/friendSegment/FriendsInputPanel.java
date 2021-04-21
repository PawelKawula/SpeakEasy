package com.speakeasy.client.ui.friendSegment;

import com.speakeasy.client.controllers.FriendsController;
import com.speakeasy.client.net.FriendAddHandler;
import com.speakeasy.client.net.FriendsRefreshHandler;
import com.speakeasy.core.models.Friend;
import com.speakeasy.server.requests.FriendAddRequest;
import com.speakeasy.server.requests.FriendsRefreshRequest;
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
                String fName = friendsInput.getText().trim();
                if (fName.equals(""))
                    return;
                FriendAddHandler handler = new FriendAddHandler(friendsController.getToken(), fName);
                if (handler.execute().isSuccess())
                    new FriendsRefreshHandler(friendsController).execute();
            });
        add(friendsSubmit, BorderLayout.EAST);
    }
}
