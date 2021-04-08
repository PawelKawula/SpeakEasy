package com.speakeasy.client.models;

import com.speakeasy.core.models.Friend;

import java.util.ArrayList;

public class FriendsModel
{
    private final ArrayList<Friend> friends;

    public FriendsModel()
    {
        friends = new ArrayList<>();
    }

    public void removeFriend(Friend friend)
    {
        friends.remove(friend);
    }

    public void addFriend(Friend friend)
    {
        friends.add(friend);
    }

    public ArrayList<Friend> getFriends()
    {
        return friends;
    }
}
