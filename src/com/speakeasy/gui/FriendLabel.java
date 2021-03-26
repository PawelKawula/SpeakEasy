package com.speakeasy.gui;

import com.speakeasy.logic.Friend;

import javax.swing.*;
import java.awt.*;

public class FriendLabel extends JPanel
{
    private Friend friend;
    private JLabel label;
    private JLabel icon;
    private ImageIcon imageIcon;

    public static int ICON_A = 32;

    public FriendLabel(Friend friend)
    {
        this.friend = friend;
        setLayout(new BorderLayout());
        setBackground(new Color(0x003566));
        setBorder(BorderFactory.createEtchedBorder());
        setFont(new Font("Lato", Font.BOLD, 18));

        label = new JLabel(friend.getNickname());

        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(Color.GRAY);
        add(label, BorderLayout.CENTER);

        if (friend.getAvatar() == null)
            return;
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
