package com.speakeasy.ui;

import javax.swing.*;
import java.awt.MenuBar;
import java.awt.*;
import com.speakeasy.core.models.Friend;

public class SpeakEasyFrame extends JFrame
{
    private final ChatBoxPanel chatBoxPanel;
    private final FriendsPanel friendsPanel;

    static final Color purple = new Color(37, 43, 50);

    public SpeakEasyFrame()
    {
        chatBoxPanel = new ChatBoxPanel();

        setLayout(new BorderLayout());
        setIconImage(new ImageIcon("src/main/resources/images/programicon.png").getImage());

        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BorderLayout());
//        chatPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        chatPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        chatPanel.add(chatBoxPanel, BorderLayout.CENTER);

        ChatInputPanel chatInputPanel = new ChatInputPanel(chatBoxPanel);
        chatInputPanel.setOpaque(false);
        chatPanel.add(chatInputPanel, BorderLayout.SOUTH);

        add(chatPanel, BorderLayout.CENTER);

        friendsPanel = new FriendsPanel(chatBoxPanel);
        add(friendsPanel, BorderLayout.WEST);

        setJMenuBar(new ChatMenuBar(chatBoxPanel, friendsPanel));

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
