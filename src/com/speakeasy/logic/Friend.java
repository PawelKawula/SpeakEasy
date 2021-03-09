package com.speakeasy.logic;

import java.time.LocalDate;
import java.util.*;

public class Friend
{
    String nickname;

    Map<LocalDate, String> myMessages;
    Map<LocalDate, String> friendMessages;

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

    public Map<LocalDate, String> getMyMessages()
    {
        return myMessages;
    }

    public Map<LocalDate, String> getFriendMessages()
    {
        return friendMessages;
    }

    public void addMyMessage(String message)
    {
        myMessages.put(LocalDate.now(), message);
    }

    public void addFriendMessage(String message)
    {
        friendMessages.put(LocalDate.now(), message);
    }
}
