package com.speakeasy.gui;

import com.speakeasy.logic.Friend;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.Map;
import java.util.TreeMap;

public class ChatBoxPanel extends JPanel
{
    private Friend currentFriend;
    private final GridBagConstraints gbc;
    private JPanel chatPanel;
    private JPanel nicknamePanel;
    private JLabel friendLabel;
    private JLabel emptyLabel;

    public ChatBoxPanel()
    {
        setBorder(null);
        setLayout(new BorderLayout());

        emptyLabel = new JLabel(new ImageIcon("emptyIcon.png"));

        chatPanel = new JPanel();
        chatPanel.setBackground(SpeakEasyFrame.purple);
        chatPanel.setLayout(new BorderLayout());
        chatPanel.add(emptyLabel, BorderLayout.CENTER);
        chatPanel.setBorder(null);
        JScrollPane chatScrollPane = new JScrollPane(chatPanel);
        chatScrollPane.setBorder(null);
        add(chatScrollPane, BorderLayout.CENTER);
        gbc = new GridBagConstraints();

        nicknamePanel = new JPanel();
        nicknamePanel.setBackground(SpeakEasyFrame.purple);
        nicknamePanel.setBorder(BorderFactory.createEtchedBorder());
        friendLabel = new JLabel("No friend Selected");
        friendLabel.setFont(new Font("Lato", Font.PLAIN, 23));
        nicknamePanel.add(friendLabel);
        add(nicknamePanel, BorderLayout.NORTH);
    }

    private Bubble makeBubble(LocalDateTime time, String message)
    {
        Bubble bubble = new Bubble(message, time, getParent().getWidth() / 5 * 3);
        bubble.getMessageDialog().addActionListener((event) ->
            {
                chatPanel.remove(bubble);
                revalidate();
                repaint();
            });
        if (chatPanel.getLayout() instanceof BorderLayout)
        {
            chatPanel.removeAll();
            initializeGridBagLayout();
        }
        ++gbc.gridy;
        gbc.ipadx = 4;
        gbc.ipady = 4;

        return bubble;
    }

    public void addMyMessage(LocalDateTime time, String message)
    {
        currentFriend.addMyMessage(time, message);
        Bubble bubble = makeBubble(time, message);
        bubble.getBubbleTimestamp().getjLabel().setHorizontalAlignment(JLabel.RIGHT);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 2;
        ++gbc.gridy;
        chatPanel.add(bubble, gbc);
        revalidate();
        repaint();
    }

    public void addFriendMessage(LocalDateTime time, String message)
    {
        currentFriend.addFriendMessage(time, message);
        Bubble bubble = makeBubble(time, message);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy++;
        chatPanel.add(bubble, gbc);
        revalidate();
        repaint();
    }

    public void setCurrentFriend(Friend currentFriend)
    {
        if (currentFriend != null && this.currentFriend != currentFriend)
        {
            friendLabel.setText(currentFriend.getNickname());
            friendLabel.revalidate();
            chatPanel.removeAll();
            this.currentFriend = currentFriend;

            if (currentFriend.getFriendMessages().isEmpty() && currentFriend.getMyMessages().isEmpty())
            {
                chatPanel.setLayout(new BorderLayout());
                chatPanel.add(emptyLabel, BorderLayout.CENTER);
                return;
            }

            initializeGridBagLayout();

            gbc.insets = new Insets(10, 10, 10, 10);

            Map<LocalDateTime, String> myMessages = currentFriend.getMyMessages();
            Map<LocalDateTime, String> friendMessages = currentFriend.getFriendMessages();
            Map<LocalDateTime, Map.Entry<Boolean, String>> combinedMessages = new TreeMap<>();

            myMessages.forEach((key, value) ->
                    combinedMessages.put(key, new AbstractMap.SimpleEntry<>(true, value)));

            friendMessages.forEach((key, value) ->
                    combinedMessages.put(key, new AbstractMap.SimpleEntry<>(false, value)));

            for (Map.Entry<LocalDateTime, Map.Entry<Boolean, String>> entry : combinedMessages.entrySet())
            {
                LocalDateTime key = entry.getKey();
                Map.Entry<Boolean, String> value = entry.getValue();

                if (value.getKey())
                {
                    gbc.gridx = 2;
                    gbc.anchor = GridBagConstraints.EAST;
                    Bubble bubble = makeBubble(key, value.getValue());
                    bubble.getBubbleTimestamp().getjLabel().setHorizontalAlignment(JLabel.RIGHT);
                    chatPanel.add(bubble, gbc);
                }
                else
                {
                    gbc.gridx = 0;
                    gbc.anchor = GridBagConstraints.WEST;
                    Bubble bubble = makeBubble(key, value.getValue());
                    chatPanel.add(bubble, gbc);
                }
            }
            chatPanel.revalidate();
            chatPanel.repaint();
        }
        else if (currentFriend == null)
        {
            this.currentFriend = null;
            chatPanel.setLayout(new BorderLayout());
            chatPanel.removeAll();
            chatPanel.add(emptyLabel, BorderLayout.CENTER);
            friendLabel.setText("No friend Selected");
            friendLabel.revalidate();
        }
    }

    public void initializeGridBagLayout()
    {
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

    public Friend getCurrentFriend()
    {
        return currentFriend;
    }

}
