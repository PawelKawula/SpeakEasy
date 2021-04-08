package com.speakeasy.client.ui;

import com.speakeasy.client.controllers.ChatController;
import com.speakeasy.client.models.ChatModel;
import com.speakeasy.client.views.ChatView;

import javax.swing.*;
import java.awt.*;

public class ChatSegment extends JPanel
{
    private final ChatController chatController;
    public ChatSegment()
    {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));

        ChatModel chatModel = new ChatModel();
        ChatView chatView = new ChatView(chatModel);
        chatController = new ChatController(chatView, chatModel);
        add(chatView, BorderLayout.CENTER);

        ChatInputPanel chatInputPanel = new ChatInputPanel(chatController);
        chatInputPanel.setOpaque(false);
        add(chatInputPanel, BorderLayout.SOUTH);
    }

    public ChatController getChatController()
    {
        return chatController;
    }
}
