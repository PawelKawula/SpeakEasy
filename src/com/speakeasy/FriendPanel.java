package com.speakeasy;

import com.speakeasy.logic.Friend;

import javax.swing.*;
import java.awt.*;

public class FriendPanel extends JPanel
{
    JButton deleteButton;
    JLabel friendLabel;
    Friend friend;

    public FriendPanel(String nickname)
    {
        setLayout(new BorderLayout());
        friend = new Friend(nickname);
        deleteButton = new JButton("X");
        friendLabel = new JLabel(nickname);
        deleteButton.addActionListener((event) ->
            {
                JPanel friendPanelList = (JPanel) ((JButton) (event.getSource())).getParent().getParent();
                friendPanelList.remove(FriendPanel.this);
                friendPanelList.revalidate();
                friendPanelList.repaint();
            });
        add(friendLabel, BorderLayout.CENTER);
        add(deleteButton, BorderLayout.EAST);
    }

    public String getNickname()
    {
        return friend.getNickname();
    }

    public void setNickname(String nickname)
    {
        friend.setNickname(nickname);
        friendLabel.setText(nickname);
    }

}
