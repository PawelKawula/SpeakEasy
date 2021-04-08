package com.speakeasy.client.views;

import com.speakeasy.client.models.ChatModel;
import com.speakeasy.client.ui.Bubble;
import com.speakeasy.client.ui.NicknamePanel;
import com.speakeasy.client.ui.SpeakEasyFrame;
import com.speakeasy.utils.ChatConstants;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public class ChatView extends JPanel
{
    private final   GridBagConstraints gbc;
    private final   JPanel chatPanel;
    private final   JLabel emptyLabel;
    private final   ChatModel model;
    private final NicknamePanel nicknamePanel;

    public ChatView(ChatModel model)
    {
        this.model = model;
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

        JScrollPane chatScrollPane = new JScrollPane(chatPanel);
        add(chatScrollPane, BorderLayout.CENTER);

        nicknamePanel = new NicknamePanel(model);
        add(nicknamePanel, BorderLayout.NORTH);
    }

    private Bubble makeBubble(LocalDateTime time, String message)
    {
        Bubble bubble = new Bubble(message, time, getParent().getWidth() / 2);
        ++gbc.gridy;
        gbc.ipadx = 4;
        gbc.ipady = 4;
        return bubble;
    }

    public void addMyBubble(LocalDateTime time, String message)
    {
        Bubble bubble = makeBubble(time, message);
        bubble.getBubbleTimestamp().getTimeLabel()
                .setHorizontalAlignment(JLabel.RIGHT);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 2;
        chatPanel.add(bubble, gbc);
    }

    public void addFriendBubble(LocalDateTime time, String message)
    {
        Bubble bubble = makeBubble(time, message);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        chatPanel.add(bubble, gbc);
    }

    public void updateView()
    {
        if (model.friendChangedNotNull())
            reloadBox();
        else if (model.getFriend() == null)
            emptyBox();
    }

    private void initializeGridBagLayout()
    {
        chatPanel.removeAll();
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
        if (model.getCombinedMessages().size() < 1)
        {
            chatPanel.setLayout(new BorderLayout());
            chatPanel.add(emptyLabel, BorderLayout.CENTER);
            return;
        }

        nicknamePanel.updateView();
        initializeGridBagLayout();

        gbc.insets = new Insets(10, 10, 10, 10);

        Set<Map.Entry<LocalDateTime, Map.Entry<Boolean, String>>> combinedMessages =
                model.getCombinedMessages();

        for (Map.Entry<LocalDateTime, Map.Entry<Boolean, String>> entry : combinedMessages)
        {
            LocalDateTime key = entry.getKey();
            Map.Entry<Boolean, String> value = entry.getValue();

            if (value.getKey())
                addMyBubble(key, value.getValue());
            else
                addFriendBubble(key, value.getValue());
        }
    }

    private void emptyBox()
    {
        chatPanel.removeAll();
        chatPanel.setLayout(new BorderLayout());
        chatPanel.add(emptyLabel, BorderLayout.CENTER);
    }
}
