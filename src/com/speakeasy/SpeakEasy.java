package com.speakeasy;


import com.speakeasy.fileIO.XMLChatReadWrite;
import com.speakeasy.logic.Friend;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.stream.XMLStreamException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
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
    JMenuBar jMenuBar;
    JMenu jMenu;
    JMenuItem exportItem;
    JMenuItem importItem;
    JFileChooser jFileChooser;

    JTextArea chatInput;
    JButton chatSubmit;

    JTextArea friendsInput;
    JButton friendsSubmit;

    public SpeakEasyFrame()
    {
        setLayout(new BorderLayout());

        jMenuBar = new JMenuBar();
        setJMenuBar(jMenuBar);

        jMenu = new JMenu("File");
        jMenuBar.add(jMenu);

        jFileChooser = new JFileChooser();
        jFileChooser.setCurrentDirectory(new File("."));
        jFileChooser.setFileFilter(new FileNameExtensionFilter("XML files", "xml"));

        exportItem = new JMenuItem("Export Chat");
        exportItem.setEnabled(false);
        exportItem.addActionListener((event) ->
            {
                int result = jFileChooser.showSaveDialog(this);
                Friend currentFriend = chatBoxPanel.getCurrentFriend();
                File file = jFileChooser.getSelectedFile();
                if (result == JFileChooser.APPROVE_OPTION && currentFriend != null)
                {
                    try
                    {
                        XMLChatReadWrite.writeChat(currentFriend, file);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    catch (XMLStreamException e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        jMenu.add(exportItem);
        importItem = new JMenuItem("Import Chat");
        importItem.addActionListener((evnet) ->
            {
                int result = jFileChooser.showOpenDialog(this);
                if (result == JFileChooser.APPROVE_OPTION)
                {
                    File file = jFileChooser.getSelectedFile();
                    try
                    {
                        Friend friend = XMLChatReadWrite.readChat(file);
                        createChatBoxFrame(friend);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    catch (XMLStreamException e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        jMenu.add(importItem);

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
        for (int i = 0; i < 100; ++i)
        {
            friend.addMyMessage(LocalDateTime.now(),"Hej co tam?");
            friend.addFriendMessage(LocalDateTime.now(),"Spierdalaj cwelu");
        }
        addFriend(friend);
    }

    public void addFriend(Friend friend)
    {
        FriendPanel friendPanel = new FriendPanel(friend);
        friendPanel.addDeleteButtonActionListener((deleteButtonEvent) ->
            {
                if (friendPanel.getFriend() == chatBoxPanel.getCurrentFriend())
                {
                    chatBoxPanel.setCurrentFriend(null);
                    exportItem.setEnabled(false);
                    revalidate();
                    repaint();
                }
                friendsListPanel.remove(friendPanel);
                friendsListPanel.revalidate();
                friendsListPanel.repaint();
            });
        friendPanel.addFriendButtonActionListener((friendButtonEvent) ->
            {
                chatBoxPanel.setCurrentFriend(friendPanel.getFriend());
                exportItem.setEnabled(true);
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

    public static void createChatBoxFrame(Friend friend)
    {
        JFrame frame = new JFrame("import");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ChatBoxPanel chatBoxPanel = new ChatBoxPanel();
        chatBoxPanel.setPreferredSize(new Dimension(600, 400));
        chatBoxPanel.setCurrentFriend(friend);
        frame.add(new JScrollPane(chatBoxPanel));
        frame.setVisible(true);
        frame.pack();
        chatBoxPanel.setCurrentFriend(friend);
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(800, 600);
    }
}
