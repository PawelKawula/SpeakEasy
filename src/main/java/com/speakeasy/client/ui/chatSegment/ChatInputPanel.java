package com.speakeasy.client.ui.chatSegment;

import com.speakeasy.client.controllers.ChatController;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class ChatInputPanel extends JPanel
{
    private final ChatController chatController;

    public ChatInputPanel(ChatController chatController)
    {
        this.chatController= chatController;
        setLayout(new BorderLayout());
//      setBorder(BorderFactory.createCompoundBorder(
//              BorderFactory.createRaisedSoftBevelBorder(),
//              BorderFactory.createEmptyBorder(6, 4, 4, 4)));
        setBorder(BorderFactory.createEmptyBorder(5, 2, 5, 5));

        JTextField chatInput = new JTextField(60);
        JPanel chatTextFieldPanel = new JPanel();
        chatTextFieldPanel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 5));
        chatTextFieldPanel.setLayout(new BorderLayout());
        chatTextFieldPanel.add(chatInput, BorderLayout.CENTER);
        add(chatTextFieldPanel, BorderLayout.CENTER);

        ImageIcon sendIcon = new ImageIcon("src/main/resources/com/speakeasy/images/send.png");
        sendIcon.setImage(sendIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        JButton chatSubmit = new JButton(sendIcon);
        chatSubmit.setVerticalAlignment(SwingConstants.CENTER);
        chatSubmit.setBorderPainted(false);
        chatSubmit.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(-3, 15, -3, 15), chatSubmit.getBorder()));

        chatSubmit.addActionListener((event) ->
        {
            if (chatController.getFriend() == null)
                return;

            String message = chatInput.getText().trim();

            if (message.trim().equals(""))
                return;

            if (message.charAt(0) == 'o')
                chatController.addFriendMessage(LocalDateTime.now(), message.substring(1));
            else
                chatController.addMyMessage(LocalDateTime.now(), message);
        });

        add(chatSubmit, BorderLayout.EAST);
    }
}