package com.speakeasy.client.views;

import com.speakeasy.client.controllers.FriendsController;
import com.speakeasy.client.models.FriendsModel;
import com.speakeasy.client.ui.friendSegment.FriendListItem;
import com.speakeasy.client.ui.main.SpeakEasyFrame;
import com.speakeasy.core.models.Friend;

import javax.swing.*;
import java.awt.*;

public class FriendsView extends JPanel
{
    GridBagConstraints gbc;
    FriendsModel model;

    public FriendsView(FriendsModel model)
    {
        this.model = model;
        setBackground(SpeakEasyFrame.purple);
        setLayout(new GridBagLayout());

        gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.SOUTH;
    }

    public void addFriendPanel(Friend friend, FriendsController friendsController)
    {
        ++gbc.gridy;
        FriendListItem friendListItem = new FriendListItem(friend, friendsController);
        add(friendListItem, gbc);
    }

    public void removeFriendPanel(Friend friend)
    {
        Component[] comps = getComponents();
        for (Component comp : comps)
        {
            if (comp instanceof FriendListItem)
            {
                FriendListItem friendListItem = (FriendListItem) comp;
                if (friendListItem.getFriend() == friend)
                    remove(comp);
            }
        }
    }
}
