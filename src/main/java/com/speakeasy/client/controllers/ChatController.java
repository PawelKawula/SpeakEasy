package com.speakeasy.client.controllers;

import com.speakeasy.client.models.ChatModel;
import com.speakeasy.client.net.MessagesRefreshHandler;
import com.speakeasy.client.views.ChatView;
import com.speakeasy.core.models.Friend;

import java.time.LocalDateTime;
import java.util.Map;

public class ChatController
{
    ChatView view;
    ChatModel model;

    public ChatController(ChatView view, ChatModel model)
    {
        this.view = view;
        this.model = model;
        view.getOlderButton().addActionListener((ev) -> getOlderMessages());
    }

    public void setToken(int token)
    {
        model.setToken(token);
    }

    public int getToken()
    {
        return model.getToken();
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
        view.addMyNewBubble(time, message);
        view.revalidate();
        view.repaint();
    }

    public void fetchNewMessages(Map<LocalDateTime, Map.Entry<Boolean, String>> newMessages)
    {
        model.addMessages(newMessages);
        view.updateView();
    }

    public void updateView()
    {
        view.updateView();
    }

    public void getOlderMessages()
    {
        MessagesRefreshHandler handler = new MessagesRefreshHandler(model.getFriend(), model.getToken());
        handler.setType(MessagesRefreshHandler.REFRESH_OLD);
        handler.execute();
        Map<LocalDateTime, Map.Entry<Boolean, String>> map = handler.getMessages();
        if (map.size() == 0)
        {
            view.getOlderButton().setEnabled(false);
            view.getOlderButton().revalidate();
            view.getOlderButton().repaint();
            return;
        }
        model.addMessages(map);
        view.addOlderMessages(map);
        view.revalidateBubbles();
    }

    public Friend getFriend()
    {
        return model.getFriend();
    }
}
