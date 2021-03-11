package com.speakeasy;

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
    GridBagConstraints gbc;

    public ChatBoxPanel()
    {
        setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
    }

    private Bubble addBubble(String title, LocalDateTime time, String message)
    {
        Bubble bubble = new Bubble(message, time, getWidth() / 9 * 8);
        bubble.getMessageDialog().addActionListener((event) ->
            {
                bubble.remove(bubble.getBubbleTimestamp().getjLabel());
                remove(bubble);
                revalidate();
                repaint();
            });
        bubble.setBorder(BorderFactory.createTitledBorder(title));
        ++gbc.gridy;
        add(bubble, gbc);
        return bubble;
    }

    public void addMyMessage(LocalDateTime time, String message)
    {
        if (currentFriend != null)
        {
            currentFriend.addMyMessage(time, message);
            gbc.gridx = 2;
            ++gbc.gridy;
            gbc.anchor = GridBagConstraints.EAST;
            addBubble("Me", time, message);
            revalidate();
            repaint();
        }
    }

    public void addFriendMessage(LocalDateTime time, String message)
    {
        if (currentFriend != null)
        {
            currentFriend.addFriendMessage(time, message);
            gbc.gridx = 0;
            gbc.gridy++;
            gbc.anchor = GridBagConstraints.WEST;
            addBubble(currentFriend.getNickname(), time, message);
            revalidate();
            repaint();
        }
    }

    public void setCurrentFriend(Friend currentFriend)
    {
        if (currentFriend != null && this.currentFriend != currentFriend)
        {
            this.currentFriend = currentFriend;
            removeAll();

            gbc.ipadx = 10;
            gbc.ipady = 10;
            gbc.anchor = GridBagConstraints.PAGE_START;
            gbc.gridy = 1000000;
            gbc.gridx = 1;
            gbc.weighty = 1;
            add(new JLabel(""), gbc);

            gbc.gridy = 0;
            gbc.gridx = 0;
            gbc.weightx = 1;
            gbc.weighty = 0;
            add(new JLabel(""), gbc);

            gbc.insets = new Insets(0, 10, 10, 10);

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
                    addBubble("Me", key, value.getValue());
                }
                else
                {
                    gbc.gridx = 0;
                    gbc.anchor = GridBagConstraints.WEST;
                    addBubble(currentFriend.getNickname(), key, value.getValue());
                }
            }

            super.revalidate();
        }
        else
            removeAll();
    }

    public Friend getCurrentFriend()
    {
        return currentFriend;
    }

}
