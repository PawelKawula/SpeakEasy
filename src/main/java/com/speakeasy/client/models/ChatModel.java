package com.speakeasy.client.models;

import com.speakeasy.core.models.Friend;

import java.time.LocalDateTime;
import java.util.Map;

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

    public void addNewMessages(Map<LocalDateTime, Map.Entry<Boolean, String>> newMessages)
    {
        newMessages.forEach((key, value) ->
            {
                if (value.getKey())
                    friend.addMyMessage(key, value.getValue());
                else
                    friend.addFriendMessage(key, value.getValue());
            });
    }

    public Friend getFriend()
    {
        return friend;
    }

    public Friend getPreviousFriend()
    {
        return previousFriend;
    }

    public Map<LocalDateTime, Map.Entry<Boolean, String>> getCombinedMessages()
    {
        return friend.getCombinedMessages();
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
