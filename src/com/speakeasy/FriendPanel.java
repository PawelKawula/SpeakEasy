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

    public FriendPanel(Friend friend)
    {
        setLayout(new BorderLayout());
        this.friend = friend;
        deleteButton = new JButton("X");
        friendButton = new JButton(friend.getNickname());

        add(friendButton, BorderLayout.CENTER);
        add(deleteButton, BorderLayout.EAST);
    }

   public void addDeleteButtonActionListener(ActionListener listener)
   {
       deleteButton.addActionListener(listener);
   }

   public void addFriendButtonActionListener(ActionListener listener)
   {
       friendButton.addActionListener(listener);
   }

   public Friend getFriend()
   {
       return friend;
   }
}
