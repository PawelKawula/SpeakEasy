package com.speakeasy.client.ui.main;

import com.speakeasy.client.controllers.ChatController;
import com.speakeasy.client.models.ChatModel;
import com.speakeasy.client.ui.chatSegment.ChatSegment;
import com.speakeasy.client.ui.friendSegment.FriendsSegment;
import com.speakeasy.client.views.ChatView;
import com.speakeasy.core.models.Friend;
import com.speakeasy.utils.ChatConstants;

import javax.swing.*;
import java.awt.*;

public class SpeakEasyFrame extends JFrame
{
    private final JDialog loginDialog;
    public static final ImageIcon iconImage = new ImageIcon(
            ChatConstants.RESOURCE_LOCATION + "images/programicon.png");

    public static final Color purple = new Color(37, 43, 50);

    public SpeakEasyFrame()
    {

        setLayout(new BorderLayout());
        setIconImage(iconImage.getImage());

        ChatSegment chatSegment = new ChatSegment();
        add(chatSegment, BorderLayout.CENTER);

        FriendsSegment friendsSegment = new FriendsSegment(chatSegment.getChatController());
        add(friendsSegment, BorderLayout.WEST);

        loginDialog = new LoginDialog(this, friendsSegment.getFriendsController());
        loginDialog.setVisible(true);

        setJMenuBar(new ChatMenuBar(friendsSegment.getFriendsController()));

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
        ChatModel chatModel = new ChatModel();
        ChatView chatView = new ChatView(chatModel);
        ChatController controller = new ChatController(chatView, chatModel);
        frame.setPreferredSize(new Dimension(600, 400));
        frame.add(chatView);
        frame.setVisible(true);
        frame.pack();
        controller.setFriend(friend);
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(800, 600);
    }
}
