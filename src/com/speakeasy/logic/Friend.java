package com.speakeasy.logic;

import java.util.ArrayList;

public class Friend
{
    String nickname;

    ArrayList<String> myMessages;
    ArrayList<String> friendMessages;

    public Friend(String nickname)
    {
        this.nickname = nickname;
        this.myMessages = new ArrayList<>();
        this.friendMessages = new ArrayList<>();
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public ArrayList<String> getMyMessages()
    {
        return myMessages;
    }

    public ArrayList<String> getFriendMessages()
    {
        return friendMessages;
    }

    public void addMyMessage(String message)
    {
        myMessages.add(message);
    }

    public void addFriendMessage(String message)
    {
        friendMessages.add(message);
    }
}
