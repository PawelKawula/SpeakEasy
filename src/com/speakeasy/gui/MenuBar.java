package com.speakeasy.gui;

import com.speakeasy.fileIO.XMLChatReadWrite;
import com.speakeasy.logic.Friend;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.stream.XMLStreamException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import static com.speakeasy.gui.SpeakEasyFrame.createChatBoxFrame;

public class MenuBar extends JMenuBar
{

    JMenuItem exportItem;
    ChatBoxPanel chatBoxPanel;
    FriendsPanel friendsPanel;

    public MenuBar(ChatBoxPanel cBP, FriendsPanel fP)
    {
        chatBoxPanel = cBP;
        friendsPanel = fP;

        JMenu jMenu = new JMenu("File");
        add(jMenu);

        setBorder(BorderFactory.createEtchedBorder());

        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setCurrentDirectory(new File("."));
        jFileChooser.setFileFilter(new FileNameExtensionFilter("XML files", "xml"));

        exportItem = new JMenuItem("Export Chat");
        exportItem.setEnabled(false);
        exportItem.addActionListener((event) ->
        {
            int result = jFileChooser.showSaveDialog(this);
            Friend currentFriend = chatBoxPanel.getCurrentFriend();
            File file = jFileChooser.getSelectedFile();
            if (result == JFileChooser.APPROVE_OPTION)
            {
                try
                {
                    XMLChatReadWrite.writeChat(currentFriend, file);
                } catch (XMLStreamException | IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
        jMenu.add(exportItem);
        JMenuItem importItem = new JMenuItem("Import Chat");
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
                } catch (IOException e)
                {
                    e.printStackTrace();
                } catch (XMLStreamException e)
                {
                    e.printStackTrace();
                }
            }
        });
        jMenu.add(importItem);

        JMenu viewMenu = new JMenu("View");
        JMenuItem friendsView = new JMenuItem("Friends/Groups");
        friendsView.addActionListener((event) ->
            {
                revalidate();
                if (friendsPanel.isVisible())
                    friendsPanel.setVisible(false);
                else
                    friendsPanel.setVisible(true);
                friendsPanel.getParent().revalidate();
                friendsPanel.getParent().repaint();
            });
        viewMenu.add(friendsView);
        add(viewMenu);
    }

    @Override
    public void revalidate()
    {
        if (exportItem != null)
        {
            if (chatBoxPanel != null && chatBoxPanel.getCurrentFriend() != null)
                exportItem.setEnabled(true);
            else
                exportItem.setEnabled(false);
        }
        super.revalidate();
        repaint();
    }
}
