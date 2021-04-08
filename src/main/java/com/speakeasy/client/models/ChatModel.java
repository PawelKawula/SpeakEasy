package com.speakeasy.client.models;

import com.speakeasy.core.models.Friend;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public class ChatModel
{
    Friend previousFriend;
    Friend friend;

    public ChatModel()
    {
        previousFriend = friend = null;
    }

    public void setFriend(Friend friend)
    {
        this.previousFriend = this.friend;
        this.friend = friend;
    }

    public Friend getFriend()
    {
        return friend;
    }

    public Friend getPreviousFriend()
    {
        return previousFriend;
    }

    public Set<Map.Entry<LocalDateTime, Map.Entry<Boolean, String>>> getCombinedMessages()
    {
        return friend.getCombinedMessages().entrySet();
    }

    public boolean friendChanged()
    {
        return friend != previousFriend;
    }

    public boolean friendChangedNotNull()
    {
        return friend != null && friendChanged();
    }
}
