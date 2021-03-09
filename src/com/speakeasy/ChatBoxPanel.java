package com.speakeasy;

import com.speakeasy.logic.Friend;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ChatBoxPanel extends JPanel
{
    Friend currentFriend;

    public ChatBoxPanel()
    {
        setLayout(new GridBagLayout());

//        gbc.gridx = 3;
//        gbc.ipadx = 10;
//        addBubble("", gbc);
//        revalidate();
    }

    public void addBubble(String message, GridBagConstraints gbc)
    {
        Bubble bubble = new Bubble(message, LocalDate.now(), getWidth() / 3 * 2);
        bubble.getMessageDialog().addActionListener((event) ->
            {
                bubble.remove(bubble.getMessageDialog());
                bubble.remove(bubble.getTimestampLabel());
                remove(bubble);
                revalidate();
                repaint();
            });
        bubble.setBorder(BorderFactory.createTitledBorder("Bubble"));
        add(bubble, gbc);
    }

    public void setBubbles()
    {
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
        addBubble("\n" +
                "\n" +
                "Lorem ipsum dolor sit amet,  adipiscing elit. Nullam at pharetra risus, nec egestas massa. Nullam faucibus tortor pharetra consectetur rutrum. Pellentesque erat neque, porttitor at rhoncus nec, cursus nec turpis. Interdum et malesuada fames ac ante ipsum primis in faucibus. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent commodo laoreet neque, vestibulum rutrum sem mollis quis. Donec ornare pellentesque efficitur. In consequat ipsum eu lacus vehicula ultricies. Maecenas nulla risus, viverra nec accumsan eget, fringilla nec nisi. Cras nec porttitor nisl.\n" +
                "\n" +
                "Quisque rutrum nulla justo, quis sagittis quam tincidunt id. In hac habitasse platea dictumst. Duis quis efficitur ex, eu tristique eros. Curabitur enim tellus, eleifend sit amet congue vel, vestibulum ut sem. Donec tempus lacus nunc, vitae dictum nisi vestibulum id. Interdum et malesuada fames ac ante ipsum primis in faucibus. In consequat turpis erat, gravida congue ipsum pellentesque ac. Integer rutrum pretium massa id interdum. Praesent quis faucibus turpis. Fusce convallis leo in nibh porta, nec pharetra felis imperdiet.\n" +
                "\n" +
                "Mauris non metus tempus, efficitur elit sed, vehicula nulla. In et pellentesque nulla, quis hendrerit magna. Sed eu augue id turpis varius posuere. Duis tempus vehicula accumsan. Pellentesque aliquet placerat laoreet. In imperdiet et dui efficitur interdum. ", gbc);
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        addBubble("A no czesc", gbc);
    }

    public static void main(String[] args)
    {

    }

}
