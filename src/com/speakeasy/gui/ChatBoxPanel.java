package com.speakeasy.gui;

import com.speakeasy.logic.Friend;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public class ChatBoxPanel extends JPanel
{
    public static int ICON_A = 38;

    private Friend currentFriend;
    private final GridBagConstraints gbc;
    private JPanel chatPanel;
    private JPanel nicknamePanel;
    private JLabel friendLabel;
    private JLabel friendIcon;
    private JLabel emptyLabel;
    private JScrollPane chatScrollPane;

    public ChatBoxPanel()
    {
        setLayout(new BorderLayout());

        emptyLabel = new JLabel(new ImageIcon("emptyIcon.png"));

        chatPanel = new JPanel();
        chatPanel.setBackground(SpeakEasyFrame.purple);
        chatPanel.setLayout(new BorderLayout());
        chatPanel.add(emptyLabel, BorderLayout.CENTER);
        chatPanel.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        chatScrollPane = new JScrollPane(chatPanel);
        chatScrollPane.setBorder(null);
        add(chatScrollPane, BorderLayout.CENTER);
        gbc = new GridBagConstraints();

        nicknamePanel = new JPanel();
        nicknamePanel.setLayout(new BorderLayout());
        nicknamePanel.setBackground(SpeakEasyFrame.purple);
        nicknamePanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 0));

        friendLabel = new JLabel("No friend Selected");
        friendLabel.setHorizontalAlignment(SwingConstants.CENTER);
        friendLabel.setFont(new Font("Lato", Font.PLAIN, 23));
        friendLabel.setBorder(null);
        nicknamePanel.add(friendLabel, BorderLayout.CENTER);

        friendIcon = new JLabel("");

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
        bubble.getMessageDialog().setOpaque(true);
        bubble.getMessageDialog().setContentAreaFilled(true);
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
            nicknamePanel.remove(friendIcon);
            this.currentFriend = currentFriend;
            ImageIcon avatar = new ImageIcon(currentFriend.getAvatar().getImage().
                    getScaledInstance(ICON_A, ICON_A, Image.SCALE_SMOOTH));
            friendIcon = new AvatarLabel(
                    avatar, ICON_A);
            friendIcon.setBorder(null);
            nicknamePanel.add(friendIcon, BorderLayout.WEST);

            if (currentFriend.getFriendMessages().isEmpty() && currentFriend.getMyMessages().isEmpty())
            {
                chatPanel.setLayout(new BorderLayout());
                chatPanel.add(emptyLabel, BorderLayout.CENTER);
                return;
            }

            initializeGridBagLayout();

            gbc.insets = new Insets(10, 10, 10, 10);

            Set<Map.Entry<LocalDateTime, Map.Entry<Boolean, String>>> combinedMessages =
                    currentFriend.getCombinedMessages().entrySet();

            for (Map.Entry<LocalDateTime, Map.Entry<Boolean, String>> entry : combinedMessages)
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
            nicknamePanel.remove(friendIcon);
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
