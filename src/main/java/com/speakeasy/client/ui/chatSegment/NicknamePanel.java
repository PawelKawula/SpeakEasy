package com.speakeasy.client.ui.chatSegment;

import com.speakeasy.client.models.ChatModel;
import com.speakeasy.client.ui.main.SpeakEasyFrame;
import com.speakeasy.client.ui.friendSegment.AvatarLabel;
import com.speakeasy.core.models.Friend;

import javax.swing.*;
import java.awt.*;

public class NicknamePanel extends JPanel
{
    private final JLabel friendLabel;
    private AvatarLabel iconLabel;
    private final ChatModel chatModel;
    private final static int ICON_A = 52;

    public NicknamePanel(ChatModel chatModel)
    {
        this.chatModel = chatModel;
        setLayout(new BorderLayout());
        setBackground(SpeakEasyFrame.purple);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(-1, 0, 0, 0),
                        BorderFactory.createRaisedSoftBevelBorder()),
                BorderFactory.createEmptyBorder(5, 5, 7, 5)));

        friendLabel = new JLabel("No friend Selected");
        friendLabel.setHorizontalAlignment(SwingConstants.CENTER);
        friendLabel.setFont(new Font("Lato", Font.PLAIN, 23));
        friendLabel.setBorder(null);
        add(friendLabel, BorderLayout.CENTER);
    }

    public void updateView()
    {
        Friend friend = chatModel.getFriend();
        friendLabel.setText(friend.getNickname());
        if (iconLabel != null)
            remove(iconLabel);
        iconLabel = new AvatarLabel(friend.getAvatar(), ICON_A);
        add(iconLabel, BorderLayout.WEST);
        revalidate();
        repaint();
    }

}
