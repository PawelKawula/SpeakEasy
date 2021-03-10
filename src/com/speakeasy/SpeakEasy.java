package com.speakeasy;


import com.speakeasy.logic.Friend;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Locale;

public class SpeakEasy
{
    public static void main(String[] args)
    {
        try
        {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        JFrame frame = new SpeakEasyFrame();
        frame.setTitle("SpeakEasy");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

class SpeakEasyFrame extends JFrame
{
    JPanel chatPanel;
    ChatBoxPanel chatBoxPanel;
    JPanel chatInputPanel;
    JPanel friendsPanel;
    JPanel friendsListPanel;
    JPanel friendsInputPanel;

    JTextArea chatInput;
    JButton chatSubmit;

    JTextArea friendsInput;
    JButton friendsSubmit;

    public SpeakEasyFrame()
    {
        setLayout(new BorderLayout());

        chatPanel = new JPanel();
        add(chatPanel, BorderLayout.CENTER);
        chatPanel.setLayout(new BorderLayout());
        chatPanel.setBorder(BorderFactory.createTitledBorder("chatPanel"));

        chatBoxPanel = new ChatBoxPanel();
        chatPanel.add(new JScrollPane(chatBoxPanel), BorderLayout.CENTER);

        chatInputPanel = new JPanel();
        chatPanel.add(chatInputPanel, BorderLayout.SOUTH);
        chatInputPanel.setLayout(new BorderLayout());
        chatInput = new JTextArea(5, 30);
        chatInputPanel.add(chatInput, BorderLayout.CENTER);
        chatSubmit = new JButton("Wyślij");
        chatInputPanel.add(chatSubmit, BorderLayout.EAST);

        chatSubmit.addActionListener((event) ->
            {
                String message = chatInput.getText();
                if (message.substring(0,1).toLowerCase(Locale.ROOT).contains("o"))
                {
                    message = message.substring(1);
                    chatBoxPanel.addFriendMessage(LocalDateTime.now(), message);
                }
                else
                    chatBoxPanel.addMyMessage(LocalDateTime.now(), message);
            });

        friendsPanel = new JPanel();
        add(friendsPanel, BorderLayout.WEST);
        friendsPanel.setLayout(new BorderLayout());
        friendsPanel.setBorder(BorderFactory.createTitledBorder("friendsPanel"));

        friendsListPanel = new JPanel();
        JScrollPane friendsListScrollPane = new JScrollPane(friendsListPanel);
        friendsPanel.add(friendsListScrollPane, BorderLayout.CENTER);
        friendsListPanel.setLayout(new GridBagLayout());
        friendsListPanel.setBorder(BorderFactory.createTitledBorder("friendsListPanel"));
        GridBagConstraints labelGBC = new GridBagConstraints();
        labelGBC.weighty = 1;
        labelGBC.weightx = 0.01;
        friendsListPanel.add(new JLabel(""), labelGBC);

        friendsInputPanel = new JPanel();
        friendsPanel.add(friendsInputPanel, BorderLayout.SOUTH);
        friendsInputPanel.setLayout(new BorderLayout());

        friendsInput = new JTextArea(1, 20);
        friendsInputPanel.add(friendsInput, BorderLayout.CENTER);

        friendsSubmit = new JButton("Dodaj");
        friendsInputPanel.add(friendsSubmit, BorderLayout.EAST);
        friendsSubmit.addActionListener((event) -> addFriend(new Friend(friendsInput.getText())));
        pack();
        Friend friend = new Friend("Pawel");
        friend.addMyMessage(LocalDateTime.now(),"Hej co tam?");
        friend.addFriendMessage(LocalDateTime.now(),"Spierdalaj cwelu");
        addFriend(friend);
        chatBoxPanel.setBubbles();
    }

    public void addFriend(Friend friend)
    {
        FriendPanel friendPanel = new FriendPanel(friend);
        friendPanel.addDeleteButtonActionListener((deleteButtonEvent) ->
            {
                if (friendPanel.getFriend() == chatBoxPanel.getCurrentFriend())
                {
                    chatBoxPanel.setCurrentFriend(null);
                    chatBoxPanel.revalidate();
                    chatBoxPanel.repaint();
                }
                friendsListPanel.remove(friendPanel);
                friendsListPanel.revalidate();
                friendsListPanel.repaint();
            });
        friendPanel.addFriendButtonActionListener((friendButtonEvent) ->
            {
                chatBoxPanel.setCurrentFriend(friendPanel.getFriend());
                chatBoxPanel.revalidate();
                chatBoxPanel.repaint();
            });
        friendPanel.setBorder(BorderFactory.createTitledBorder("friend"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = friendsListPanel.getComponentCount() + 1;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        friendsListPanel.add(friendPanel, gbc);
        friendsListPanel.revalidate();
        friendsListPanel.repaint();
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(800, 600);
    }
}
