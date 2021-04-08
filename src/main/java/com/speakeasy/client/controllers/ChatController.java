package com.speakeasy.client.controllers;

import com.speakeasy.client.models.ChatModel;
import com.speakeasy.client.views.ChatView;
import com.speakeasy.core.models.Friend;

import java.time.LocalDateTime;

public class ChatController
{
    ChatView view;
    ChatModel model;

    public ChatController(ChatView view, ChatModel model)
    {
        this.view = view;
        this.model = model;
    }

    public void setFriend(Friend friend)
    {
        model.setFriend(friend);
        view.updateView();
        view.revalidate();
        view.repaint();
    }

    public void addMyMessage(LocalDateTime time, String message)
    {
        model.getFriend().addMyMessage(time, message);
        view.addMyBubble(time, message);
        view.revalidate();
        view.repaint();
    }

    public void addFriendMessage(LocalDateTime time, String message)
    {
        model.getFriend().addFriendMessage(time, message);
        view.addFriendBubble(time, message);
        view.revalidate();
        view.repaint();
    }

    public Friend getFriend()
    {
        return model.getFriend();
    }
}
