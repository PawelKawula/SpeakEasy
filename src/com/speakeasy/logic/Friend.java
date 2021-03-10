package com.speakeasy.logic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Friend
{
    String nickname;

    Map<LocalDateTime, String> myMessages;
    Map<LocalDateTime, String> friendMessages;

    public Friend(String nickname)
    {
        this.nickname = nickname;
        this.myMessages = new TreeMap<>();
        this.friendMessages = new TreeMap<>();
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public Map<LocalDateTime, String> getMyMessages()
    {
        return myMessages;
    }

    public Map<LocalDateTime, String> getFriendMessages()
    {
        return friendMessages;
    }

    public void addMyMessage(LocalDateTime time ,String message)
    {
        myMessages.put(time, message);
    }

    public void addFriendMessage(LocalDateTime time, String message)
    {
        friendMessages.put(time, message);
    }
}
