package com.speakeasy.client.views;

import com.speakeasy.client.controllers.FriendsController;
import com.speakeasy.client.models.FriendsModel;
import com.speakeasy.client.ui.FriendListItem;
import com.speakeasy.client.ui.SpeakEasyFrame;
import com.speakeasy.core.models.Friend;

import javax.swing.*;
import java.awt.*;

public class FriendsView extends JPanel
{
    GridBagConstraints gbc;

    public FriendsView(FriendsModel friendsModel)
    {
        setBackground(SpeakEasyFrame.purple);
        setLayout(new GridBagLayout());

        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
    }

    public void addFriendPanel(Friend friend, FriendsController friendsController)
    {
        FriendListItem friendListItem = new FriendListItem(friend, friendsController);
        friendListItem.setBorder(BorderFactory.createTitledBorder("FriendListItem"));
        add(friendListItem, gbc);
        ++gbc.gridy;
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
