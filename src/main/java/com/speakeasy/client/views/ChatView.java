package com.speakeasy.client.views;

import com.speakeasy.client.models.ChatModel;
import com.speakeasy.client.ui.chatSegment.Bubble;
import com.speakeasy.client.ui.chatSegment.NicknamePanel;
import com.speakeasy.client.ui.main.SpeakEasyFrame;
import com.speakeasy.utils.ChatConstants;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Map;

public class ChatView extends JPanel
{
    private final   GridBagConstraints gbc;
    private final   JPanel chatPanel;
    private final   JLabel emptyLabel;
    private final   ChatModel model;
    private final   NicknamePanel nicknamePanel;
    private final   JButton olderButton;
    int gridyOlder;
    int gridyNewer;

    public ChatView(ChatModel model)
    {
        this.model = model;
        gridyNewer = 25000;
        gridyOlder = gridyNewer - 1;
        setLayout(new BorderLayout());
        gbc = new GridBagConstraints();

        emptyLabel = new JLabel(new ImageIcon(
                ChatConstants.RESOURCE_LOCATION + "images/emptyIcon.png"));

        chatPanel = new JPanel();
        chatPanel.setBackground(SpeakEasyFrame.purple);
        chatPanel.setLayout(new BorderLayout());
        chatPanel.add(emptyLabel, BorderLayout.CENTER);
        chatPanel.setBorder(
                BorderFactory.createEmptyBorder(0, 0, 0, 0));

        add(new JScrollPane(chatPanel), BorderLayout.CENTER);

        nicknamePanel = new NicknamePanel(model);
        add(nicknamePanel, BorderLayout.NORTH);

        olderButton = new JButton("Get older messages");
    }

    public JButton getOlderButton()
    {
        return olderButton;
    }

    private Bubble makeBubble(LocalDateTime time, String message)
    {
        Bubble bubble = new Bubble(message, time, getParent().getWidth() / 2);
        gbc.ipadx = 4;
        gbc.ipady = 4;
        return bubble;
    }


    public void addMyNewBubble(LocalDateTime time, String message)
    {
        Bubble bubble = makeBubble(time, message);
        layoutForMyMessage(bubble);
        gbc.gridy = ++gridyNewer;
        chatPanel.add(bubble, gbc);
    }

    private void addMyOlderBubble(LocalDateTime time, String message)
    {
        Bubble bubble = makeBubble(time, message);
        layoutForMyMessage(bubble);
        gbc.gridy = --gridyOlder;
        chatPanel.add(bubble, gbc);
    }

    private void layoutForMyMessage(Bubble bubble)
    {
        bubble.getBubbleTimestamp().getTimeLabel()
                .setHorizontalAlignment(JLabel.RIGHT);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 2;
    }

    public void addFriendNewBubble(LocalDateTime time, String message)
    {
        Bubble bubble = makeBubble(time, message);
        layoutForFriendMessage();
        gbc.gridy = ++gridyNewer;
        chatPanel.add(bubble, gbc);
    }

    public void addFriendOlderBubble(LocalDateTime time, String message)
    {
        Bubble bubble = makeBubble(time, message);
        layoutForFriendMessage();
        gbc.gridy = --gridyOlder;
        chatPanel.add(bubble, gbc);
    }

    public void layoutForFriendMessage()
    {
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
    }

    public void updateView()
    {
        if (model.friendChangedNotNull())
            reloadBox();
        else if (model.getFriend() == null)
            emptyBox();
        revalidateBubbles();
        revalidate();
        repaint();
    }

    private void initializeGridBagLayout()
    {
        chatPanel.removeAll();
        gridyNewer = 25000;
        gridyOlder = gridyNewer - 1;
        chatPanel.setLayout(new GridBagLayout());

        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.gridy = 1000000;
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        chatPanel.add(new JLabel(""), gbc);

        gbc.gridy = 1;
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.weighty = 0;
        chatPanel.add(new JLabel(""), gbc);
        gbc.gridy = 1;

    }

    private void reloadBox()
    {
        Map<LocalDateTime, Map.Entry<Boolean, String>> combinedMessages =
                model.getCombinedMessages();

        if (combinedMessages.size() < 1)
        {
            chatPanel.setLayout(new BorderLayout());
            chatPanel.add(emptyLabel, BorderLayout.CENTER);
            chatPanel.remove(olderButton);
            return;
        }

        nicknamePanel.updateView();
        initializeGridBagLayout();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        Insets ins = gbc.insets;
        gbc.insets = new Insets(0, 10, 0, 10);
        chatPanel.add(olderButton, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.insets = ins;
        olderButton.setEnabled(true);

        gbc.insets = new Insets(10, 10, 10, 10);

       combinedMessages.forEach((key, value) ->
           {
               if (value.getKey())
                   addMyNewBubble(key, value.getValue());
               else
                   addFriendNewBubble(key, value.getValue());
           });
    }

    public void revalidateBubbles()
    {
        Component[] comps = chatPanel.getComponents();
        for (Component comp : comps) comp.revalidate();
    }

    private void emptyBox()
    {
        chatPanel.removeAll();
        chatPanel.setLayout(new BorderLayout());
        chatPanel.add(emptyLabel, BorderLayout.CENTER);
    }

    public void addOlderMessages(Map<LocalDateTime, Map.Entry<Boolean, String>> messages)
    {
        messages.forEach((key, value) ->
            {
                if (value.getKey())
                    addMyOlderBubble(key, value.getValue());
                else
                    addFriendOlderBubble(key, value.getValue());
            });
        revalidate();
        repaint();
    }
}
