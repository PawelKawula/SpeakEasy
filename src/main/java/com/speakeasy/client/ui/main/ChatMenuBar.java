package com.speakeasy.client.ui.main;

import com.speakeasy.client.controllers.FriendsController;
import com.speakeasy.utils.fileIO.XMLChatReadWrite;
import com.speakeasy.core.models.Friend;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.stream.XMLStreamException;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ChatMenuBar extends JMenuBar
{
    public ChatMenuBar(FriendsController friendsController)
    {
        JMenu jMenu = new JMenu("File");
        add(jMenu);

        setBorder(BorderFactory.createEtchedBorder());

        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setCurrentDirectory(new File("."));
        jFileChooser.setFileFilter(new FileNameExtensionFilter("XML files", "xml"));

        JMenuItem exportItem = new JMenuItem("Export Chat");
        exportItem.setEnabled(false);
        exportItem.addActionListener((event) ->
        {
            int result = jFileChooser.showSaveDialog(this);
            Friend currentFriend = friendsController.getChatController().getFriend();
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
        importItem.addActionListener((event) ->
        {
            int result = jFileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION)
            {
                File file = jFileChooser.getSelectedFile();
                try
                {
                    Friend friend = XMLChatReadWrite.readChat(file);
                    SpeakEasyFrame.createChatBoxFrame(friend);
                } catch (XMLStreamException | IOException e)
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
            Component friendsSegment = friendsController.getSegment();
            friendsSegment.setVisible(!friendsSegment.isVisible());
            friendsSegment.getParent().revalidate();
            friendsSegment.getParent().repaint();
        });
        viewMenu.add(friendsView);
        add(viewMenu);
    }
}
