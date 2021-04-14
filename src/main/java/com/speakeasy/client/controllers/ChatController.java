package com.speakeasy.client.controllers;

import com.speakeasy.client.models.ChatModel;
import com.speakeasy.client.views.ChatView;
import com.speakeasy.core.models.Friend;

import java.time.LocalDateTime;
import java.util.Map;

public class ChatController
{
    ChatView view;
    ChatModel model;
    int token;

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

    public void fetchNewMessages(Map<LocalDateTime, Map.Entry<Boolean, String>> newMessages)
    {
        model.addNewMessages(newMessages);
        view.updateView();
    }

    public void updateView()
    {
        view.updateView();
    }

    public int getToken()
    {
        return token;
    }

    public void setToken(int token)
    {
        this.token = token;
    }

    public Friend getFriend()
    {
        return model.getFriend();
    }
}
