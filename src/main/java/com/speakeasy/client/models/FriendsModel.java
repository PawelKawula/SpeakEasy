package com.speakeasy.client.models;

import com.speakeasy.core.models.Friend;

import java.util.HashSet;
import java.util.Set;

public class FriendsModel
{
    private final Set<Friend> friends;

    public FriendsModel()
    {
        friends = new HashSet<>();
    }

    public void removeFriend(Friend friend)
    {
        friends.remove(friend);
    }

    public void addFriend(Friend friend)
    {
        friends.add(friend);
    }

    public Set<Friend> getFriends()
    {
        return friends;
    }
}
