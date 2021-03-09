package com.speakeasy;

import com.speakeasy.logic.Friend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class FriendPanel extends JPanel
{
    JButton deleteButton;
    JButton friendButton;
    Friend friend;

    public FriendPanel(String nickname)
    {
        setLayout(new BorderLayout());
        friend = new Friend(nickname);
        deleteButton = new JButton("X");
        friendButton = new JButton(nickname);

        add(friendButton, BorderLayout.CENTER);
        add(deleteButton, BorderLayout.EAST);
    }

   public void addDeleteButtonActionListener(ActionListener listener)
   {
       deleteButton.addActionListener(listener);
   }

    public String getNickname()
    {
        return friend.getNickname();
    }

    public void setNickname(String nickname)
    {
        friend.setNickname(nickname);
        friendButton.setText(nickname);
    }

}
