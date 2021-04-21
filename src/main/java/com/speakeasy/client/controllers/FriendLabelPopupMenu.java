package com.speakeasy.client.controllers;

import com.speakeasy.client.net.FriendRemoveHandler;
import com.speakeasy.client.ui.friendSegment.FriendListItem;
import com.speakeasy.core.models.Friend;
import com.speakeasy.utils.ChatConstants;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

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
                Friend friend = friendListItem.getFriend();
                int token = chatController.getToken();
                if (!new FriendRemoveHandler(friend, token).execute().isSuccess())
                   return;

                if (friendListItem.getFriend() == chatController.getFriend())
                    chatController.setFriend(null);
                friendsController.removeFriend(friendListItem.getFriend());

                friendListItem.revalidate();
                friendListItem.repaint();
            });

        add(deleteItem);
    }

    public static void main(String[] args)
    {
        LocalDate date = LocalDate.of(2021, 8, 1);
        System.out.println(LocalDate.now().until(date).getMonths() + ", " + LocalDate.now().until(date).getDays());
    }
}
