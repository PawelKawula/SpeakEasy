package com.speakeasy.client.controllers;

import com.speakeasy.client.ui.FriendListItem;
import com.speakeasy.utils.ChatConstants;

import javax.swing.*;
import java.awt.*;

public class FriendLabelPopupMenu extends JPopupMenu
{
    public FriendLabelPopupMenu(FriendListItem friendListItem)
    {
        FriendsController friendsController = friendListItem.getFriendsListController();
        ChatController chatController = friendsController.getChatController();

        JMenuItem deleteItem = new JMenuItem("Delete Friend", new ImageIcon(
                new ImageIcon(ChatConstants.RESOURCE_LOCATION + "images/deleteicon.png")
                        .getImage().getScaledInstance(14, 14, Image.SCALE_DEFAULT)));

        deleteItem.addActionListener((event) ->
            {
                if (friendListItem.getFriend() == chatController.getFriend())
                    chatController.setFriend(null);
                friendsController.removeFriend(friendListItem.getFriend());

                friendListItem.revalidate();
                friendListItem.repaint();
            });

        add(deleteItem);
    }
}
