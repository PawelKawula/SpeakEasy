package com.speakeasy.gui;

import com.speakeasy.logic.Friend;

import javax.swing.*;
import java.awt.*;

public class SpeakEasyFrame extends JFrame
{
    private ChatBoxPanel chatBoxPanel;
    private FriendsPanel friendsPanel;

    static final Color purple = new Color(37, 43, 50);

    public SpeakEasyFrame()
    {
        chatBoxPanel = new ChatBoxPanel();

        setLayout(new BorderLayout());
        setIconImage(new ImageIcon("programicon.png").getImage());

        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BorderLayout());
//        chatPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        chatPanel.add(chatBoxPanel, BorderLayout.CENTER);

        ChatInputPanel chatInputPanel = new ChatInputPanel(chatBoxPanel);
        chatInputPanel.setOpaque(false);
        chatPanel.add(chatInputPanel, BorderLayout.SOUTH);

        add(chatPanel, BorderLayout.CENTER);

        friendsPanel = new FriendsPanel(chatBoxPanel);
        add(friendsPanel, BorderLayout.WEST);

        setJMenuBar(new MenuBar(chatBoxPanel, friendsPanel));

        setPreferredSize(new Dimension(1200, 800));
        pack();
    }

    @Override
    public void revalidate()
    {
        super.revalidate();
        getJMenuBar().revalidate();
    }

    public static void createChatBoxFrame(Friend friend)
    {
        JFrame frame = new JFrame("import");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ChatBoxPanel chatBoxPanel = new ChatBoxPanel();
        frame.setPreferredSize(new Dimension(600, 400));
        frame.add(chatBoxPanel);
        frame.setVisible(true);
        frame.pack();
        chatBoxPanel.setCurrentFriend(friend);
    }

    public ChatBoxPanel getChatBoxPanel()
    {
        return chatBoxPanel;
    }

    public FriendsPanel getFriendsPanel()
    {
        return friendsPanel;
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(800, 600);
    }
}