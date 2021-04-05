package com.speakeasy.client.ui;

import javax.swing.*;
import java.awt.*;
import com.speakeasy.core.models.Friend;
import com.speakeasy.utils.ChatConstants;

public class FriendLabel extends JPanel
{
    private final   Friend friend;
    private final   JLabel label;
    private         JLabel icon;
    private         ImageIcon imageIcon;

    public static int ICON_A = 32;

    public FriendLabel(Friend friend)
    {
        this.friend = friend;
        setLayout(new BorderLayout());
        setBackground(UIManager.getColor("focus"));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)));
        setFont(new Font("Lato", Font.BOLD, 18));

        label = new JLabel(friend.getNickname());

        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(Color.GRAY);
        add(label, BorderLayout.CENTER);

        if (friend.getAvatar() == null)
            imageIcon = new ImageIcon(ChatConstants.RESOURCE_LOCATION + "images/noimage.png");
        else
            imageIcon = new ImageIcon(friend.getAvatar().getImage()
                    .getScaledInstance(ICON_A, ICON_A, Image.SCALE_DEFAULT));
        icon = new AvatarLabel(imageIcon, ICON_A);
        add(icon, BorderLayout.WEST);
    }

    public JLabel getLabel()
    {
        return label;
    }

    public JLabel getIcon()
    {
        return icon;
    }

    public Friend getFriend()
    {
        return friend;
    }

}
