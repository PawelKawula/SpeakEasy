package com.speakeasy.client.ui;

import com.speakeasy.client.controllers.ChatController;
import com.speakeasy.client.controllers.FriendsController;
import com.speakeasy.client.models.FriendsModel;
import com.speakeasy.client.views.FriendsView;

import javax.swing.*;
import java.awt.*;

public class FriendsSegment extends JPanel
{
    private FriendsController friendsController;
    public FriendsSegment(ChatController chatController)
    {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));

        FriendsModel friendsModel = new FriendsModel();
        FriendsView friendsListPanel = new FriendsView(friendsModel);
        friendsController = new FriendsController(friendsModel, friendsListPanel);

        friendsController.setChatController(chatController);
        friendsListPanel.setBackground(SpeakEasyFrame.purple);
        friendsListPanel.setLayout(new GridBagLayout());

        JScrollPane friendsListScrollPane = new JScrollPane(friendsListPanel);
        friendsListScrollPane.setBorder(null);
        add(friendsListScrollPane, BorderLayout.CENTER);

        GridBagConstraints labelGBC = new GridBagConstraints();
        labelGBC.weighty = 1;
        labelGBC.weightx = 0.01;
        labelGBC.fill = GridBagConstraints.BOTH;
        friendsListPanel.add(new JLabel(""), labelGBC);

        add(new FriendsInputPanel(friendsController), BorderLayout.SOUTH);
    }

    public FriendsController getFriendsController()
    {
        return friendsController;
    }
}
