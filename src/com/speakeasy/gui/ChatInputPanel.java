package com.speakeasy.gui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class ChatInputPanel extends JPanel
{
   private ChatBoxPanel chatBoxPanel;

   public ChatInputPanel(ChatBoxPanel cBP)
   {
      chatBoxPanel = cBP;
      setLayout(new BorderLayout());
      setBorder(BorderFactory.createRaisedSoftBevelBorder());

      JTextField chatInput = new JTextField(60);
      add(chatInput, BorderLayout.CENTER);

      JButton chatSubmit = new JButton("Send");
      chatSubmit.setVerticalAlignment(SwingConstants.CENTER);
      chatSubmit.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(-3,15,-3,15), chatSubmit.getBorder()));
      chatSubmit.setBorderPainted(false);

      chatSubmit.addActionListener((event) ->
      {
         if (chatBoxPanel.getCurrentFriend() == null)
            return;

         String message = chatInput.getText().trim();

         if (message.trim().equals(""))
            return;

         if (message.charAt(0) == 'o')
            chatBoxPanel.addFriendMessage(LocalDateTime.now(), message.substring(1));
         else
            chatBoxPanel.addMyMessage(LocalDateTime.now(), message);
      });

      add(chatSubmit, BorderLayout.EAST);
   }
}
