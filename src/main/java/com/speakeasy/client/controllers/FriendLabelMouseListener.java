package com.speakeasy.client.controllers;

import com.speakeasy.client.ui.FriendListItem;
import com.speakeasy.core.models.Friend;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FriendLabelMouseListener extends MouseAdapter
{
    private final FriendListItem friendListItem;
    private FriendsController friendsController;
    private ChatController chatController;

    public FriendLabelMouseListener(FriendListItem friendListItem)
    {
        this.friendListItem = friendListItem;
        this.friendsController = friendListItem.getFriendsListController();
        this.chatController = friendsController.getChatController();
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if (e.getButton() != MouseEvent.BUTTON1)
            return;
        super.mouseReleased(e);
        friendListItem.getLabel().setForeground(Color.GRAY);
        Friend prevSelectedFriend = chatController.getFriend();
        friendsController.getFriendLabel(prevSelectedFriend)
                .setForeground(Color.GRAY);
        chatController.setFriend(friendListItem.getFriend());
        friendListItem.getLabel().setForeground(Color.WHITE);
        friendListItem.revalidate();
        friendListItem.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        super.mouseEntered(e);
        friendListItem.getLabel().setForeground(Color.WHITE);
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        super.mouseExited(e);
        if (chatController.getFriend() != friendListItem.getFriend())
            friendListItem.getLabel().setForeground(Color.GRAY);
    }

}
