package com.speakeasy.client.ui.friendSegment;

import com.speakeasy.client.controllers.FriendLabelMouseListener;
import com.speakeasy.client.controllers.FriendLabelPopupMenu;
import com.speakeasy.client.controllers.FriendsController;
import com.speakeasy.client.net.FriendAcceptHandler;
import com.speakeasy.client.net.FriendRemoveHandler;
import com.speakeasy.core.models.Friend;
import com.speakeasy.utils.ChatConstants;

import javax.swing.*;
import java.awt.*;

public class FriendListItem extends JPanel
{
    private final   Friend friend;
    private final   JLabel label;
    private final   JLabel icon;

    public static int ICON_A = 32;

    private FriendsController friendsController;

    public FriendListItem(Friend friend, FriendsController friendsController)
    {
        this.friend = friend;
        this.friendsController = friendsController;
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

        ImageIcon imageIcon;
        if (friend.getAvatar() == null)
            imageIcon = new ImageIcon(ChatConstants.RESOURCE_LOCATION + "images/noimage.png");
        else
            imageIcon = new ImageIcon(friend.getAvatar().getImage()
                    .getScaledInstance(ICON_A, ICON_A, Image.SCALE_DEFAULT));
        icon = new AvatarLabel(imageIcon, ICON_A);
        add(icon, BorderLayout.WEST);

        if (friend.isPending())
        {
            if (!friend.isMeActive())
            {
                JPanel acceptPanel = new JPanel();
                acceptPanel.setOpaque(false);
                JButton acceptButton = new JButton("acc");
                JButton rejectButton = new JButton("rej");
                acceptButton.addActionListener(
                        (ev) ->
                        {
                            if (new FriendAcceptHandler(friendsController, friend).execute().isSuccess())
                                remove(acceptPanel);
                            revalidate();
                            repaint();
                        });
                rejectButton.addActionListener((ev) ->
                        {
                            FriendRemoveHandler handler =
                                    new FriendRemoveHandler(friend, friendsController.getToken());
                            if (handler.execute().isSuccess())
                                friendsController.removeFriend(friend);
                        });
                acceptPanel.add(acceptButton);
                acceptPanel.add(rejectButton);
                add(acceptPanel, BorderLayout.EAST);
            }
            else
                add(new JLabel("Pending..."), BorderLayout.EAST);
        }

        addMouseListener(new FriendLabelMouseListener(this));
        setComponentPopupMenu(new FriendLabelPopupMenu(this));
    }

    public FriendsController getFriendsListController()
    {
        return friendsController;
    }

    public void setFriendsListController(FriendsController friendsController)
    {
        this.friendsController = friendsController;
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
