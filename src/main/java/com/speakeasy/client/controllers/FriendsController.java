package com.speakeasy.client.controllers;

import com.speakeasy.client.models.FriendsModel;
import com.speakeasy.client.ui.FriendListItem;
import com.speakeasy.client.views.FriendsView;
import com.speakeasy.core.models.Friend;

import java.awt.*;

public class FriendsController
{
    private final FriendsModel model;
    private final FriendsView view;
    private ChatController chatController;

    public FriendsController(FriendsModel model, FriendsView view)
    {
        this.model = model;
        this.view = view;
        this.chatController = null;
    }

    public void setChatController(ChatController controller)
    {
        this.chatController = controller;
    }

    public ChatController getChatController()
    {
        return chatController;
    }

    public FriendListItem getFriendLabel(Friend friend)
    {
        Component[] comps = view.getComponents();
        for (Component comp : comps)
        {
            if (comp instanceof FriendListItem)
            {
                FriendListItem friendListItem = (FriendListItem) comp;
                if (friendListItem.getFriend() == friend)
                    return friendListItem;
            }
        }
        return null;
    }

    public void addFriend(Friend friend)
    {
        model.addFriend(friend);
        view.addFriendPanel(friend, this);
        view.revalidate();
        view.repaint();
    }

    public void removeFriend(Friend friend)
    {
        model.removeFriend(friend);
        view.removeFriendPanel(friend);
        view.revalidate();
        view.repaint();
    }

    public Component getSegment()
    {
        return view.getParent();
    }
}
