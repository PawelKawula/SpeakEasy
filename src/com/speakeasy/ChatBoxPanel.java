package com.speakeasy;

import com.speakeasy.logic.Friend;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;

public class ChatBoxPanel extends JPanel
{

    public ChatBoxPanel()
    {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.ipadx = 10;
        gbc.ipady = 10;
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.gridy = 1000000;
        gbc.gridx = 1;
        gbc.weighty = 1;
        add(new JLabel(""), gbc);
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.weighty = 0;
        add(new JLabel(""), gbc);
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 10, 10, 0);
        gbc.anchor = GridBagConstraints.LINE_START;
        addBubble("Witaj swiecie", gbc);
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        addBubble("A no czesc", gbc);
//        gbc.gridx = 3;
//        gbc.ipadx = 10;
//        addBubble("", gbc);
//        revalidate();
    }

    public void addBubble(String message, GridBagConstraints gbc)
    {
        JLabel jLabel = new JLabel(message);
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Bubble");
        Insets borderInsets = titledBorder.getBorderInsets(jLabel, gbc.insets);
        jLabel.setBorder(titledBorder);
        add(jLabel, gbc);
    }

    public static void main(String[] args)
    {

    }

}
