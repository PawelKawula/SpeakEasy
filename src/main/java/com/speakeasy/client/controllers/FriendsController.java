package com.speakeasy.client.controllers;

import com.speakeasy.client.models.FriendsModel;
import com.speakeasy.client.ui.friendSegment.FriendListItem;
import com.speakeasy.client.views.FriendsView;
import com.speakeasy.core.models.Friend;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

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

    public Set<Friend> getFriends()
    {
        return model.getFriends();
    }

    public int getToken()
    {
        return chatController.getToken();
    }

    public void refreshFriends(Set<Friend> updatedFriends)
    {
        Set<Friend> newFriends = new HashSet<>();
        Set<Friend> deletedFriends = new HashSet<>();
        Set<Friend> oldFriends = model.getFriends();

        updatedFriends.forEach((f) ->
            {
                if (!oldFriends.contains(f))
                    newFriends.add(f);
            });
        oldFriends.forEach((f) ->
            {
                if (!updatedFriends.contains(f))
                    deletedFriends.add(f);
            });
        oldFriends.removeAll(deletedFriends);
        oldFriends.addAll(newFriends);

        view.removeItems(deletedFriends);
        view.addItems(newFriends, this);

        view.revalidate();
        view.repaint();
    }
}
