package com.speakeasy;

import com.speakeasy.logic.Friend;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Iterator;
import java.util.Map;

public class ChatBoxPanel extends JPanel
{
    private Friend currentFriend;
    GridBagConstraints gbc;

    public ChatBoxPanel()
    {
        setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();

//        gbc.gridx = 3;
//        gbc.ipadx = 10;
//        addBubble("", gbc);
//        revalidate();
    }

    public Bubble addBubble(String title,LocalDateTime time, String message, GridBagConstraints gbc, Color color)
    {
        Bubble bubble = new Bubble(message, time, getWidth() / 3 * 2);
        bubble.getMessageDialog().addActionListener((event) ->
            {
                currentFriend.getMyMessages().remove(bubble.getBubbleTimestamp().getTime());
                currentFriend.getFriendMessages().remove(bubble.getBubbleTimestamp().getTime());
                bubble.remove(bubble.getMessageDialog());
                bubble.remove(bubble.getBubbleTimestamp().getjLabel());
                remove(bubble);
                revalidate();
                repaint();
            });
        bubble.setBorder(BorderFactory.createTitledBorder(title));
        bubble.setColor(color);
        add(bubble, gbc);
        bubble.setEnabled(false);
        return bubble;
    }

    public void setBubbles()
    {

//        gbc.gridy = 0;
//        gbc.gridx = 0;
//        gbc.insets = new Insets(0, 10, 10, 0);
//        gbc.anchor = GridBagConstraints.LINE_START;
//        addBubble("\n" +
//                "\n" +
//                "Lorem ipsum dolor sit amet,  adipiscing elit. Nullam at pharetra risus, nec egestas massa. Nullam faucibus tortor pharetra consectetur rutrum. Pellentesque erat neque, porttitor at rhoncus nec, cursus nec turpis. Interdum et malesuada fames ac ante ipsum primis in faucibus. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent commodo laoreet neque, vestibulum rutrum sem mollis quis. Donec ornare pellentesque efficitur. In consequat ipsum eu lacus vehicula ultricies. Maecenas nulla risus, viverra nec accumsan eget, fringilla nec nisi. Cras nec porttitor nisl.\n" +
//                "\n" +
//                "Quisque rutrum nulla justo, quis sagittis quam tincidunt id. In hac habitasse platea dictumst. Duis quis efficitur ex, eu tristique eros. Curabitur enim tellus, eleifend sit amet congue vel, vestibulum ut sem. Donec tempus lacus nunc, vitae dictum nisi vestibulum id. Interdum et malesuada fames ac ante ipsum primis in faucibus. In consequat turpis erat, gravida congue ipsum pellentesque ac. Integer rutrum pretium massa id interdum. Praesent quis faucibus turpis. Fusce convallis leo in nibh porta, nec pharetra felis imperdiet.\n" +
//                "\n" +
//                "Mauris non metus tempus, efficitur elit sed, vehicula nulla. In et pellentesque nulla, quis hendrerit magna. Sed eu augue id turpis varius posuere. Duis tempus vehicula accumsan. Pellentesque aliquet placerat laoreet. In imperdiet et dui efficitur interdum. ", gbc);
//        gbc.gridx = 2;
//        gbc.gridy = 1;
//        gbc.anchor = GridBagConstraints.EAST;
//        addBubble("A no czesc", gbc);
    }

    public void addMyMessage(LocalDateTime time, String message)
    {
        if (currentFriend != null)
        {
            currentFriend.addMyMessage(time, message);
            gbc.gridx = 2;
            ++gbc.gridy;
            gbc.anchor = GridBagConstraints.EAST;
            addBubble("Me", time, message, gbc, Color.BLUE);
            revalidate();
            repaint();
        }
    }

    public void addFriendMessage(LocalDateTime time, String message)
    {
        if (currentFriend != null)
        {
            currentFriend.addFriendMessage(time, message);
            gbc.gridx = 0;
            gbc.gridy++;
            gbc.anchor = GridBagConstraints.WEST;
            Bubble bubble = addBubble(currentFriend.getNickname(), time, message, gbc, Color.GRAY);
            revalidate();
            repaint();
        }
    }

    public void setCurrentFriend(Friend currentFriend)
    {
        this.currentFriend = currentFriend;
        if (currentFriend != null)
        {
            removeAll();
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
            Iterator<Map.Entry<LocalDateTime, String>> myIter = currentFriend.getMyMessages().entrySet().iterator();
            Iterator<Map.Entry<LocalDateTime, String>> friendIter = currentFriend.getFriendMessages().entrySet().iterator();

            gbc.insets = new Insets(0, 10, 10, 10);
            int row = 0;
            Map.Entry<LocalDateTime, String> myMessage = null;
            boolean unusedFriendMessage = false;
            Map.Entry<LocalDateTime, String> friendMessage = null;

            while (myIter.hasNext() || friendIter.hasNext() || unusedFriendMessage)
            {
                boolean moreMyMessages = myIter.hasNext();
                if (moreMyMessages)
                    myMessage = myIter.next();
                while (friendIter.hasNext() || unusedFriendMessage)
                {
                    if (!unusedFriendMessage)
                        friendMessage = friendIter.next();

                    long myTimestamp = 0;
                    if (moreMyMessages)
                        myTimestamp = myMessage.getKey().toEpochSecond(ZoneOffset.ofHours(0));
                    long friendTimestamp = friendMessage.getKey().toEpochSecond(ZoneOffset.ofHours(0));

                    if (!moreMyMessages || myTimestamp > friendTimestamp)
                    {
                        gbc.gridx = 0;
                        gbc.anchor = GridBagConstraints.WEST;
                        gbc.gridy = row++;
                        Bubble bubble = addBubble(currentFriend.getNickname(),friendMessage.getKey(), friendMessage.getValue(), gbc, Color.GRAY);
                        unusedFriendMessage = false;
                    }
                    else
                    {
                        unusedFriendMessage = true;
                        break;
                    }
                }
                if (moreMyMessages)
                {
                    gbc.gridx = 2;
                    gbc.gridy = row++;
                    gbc.anchor = GridBagConstraints.EAST;
                    addBubble("Me", myMessage.getKey(), myMessage.getValue(), gbc, Color.BLUE);
                }
            }
            super.revalidate();
        }
        else
            removeAll();
    }

    public Friend getCurrentFriend()
    {
        return currentFriend;
    }

    @Override
    public void revalidate()
    {
        super.revalidate();
        if (currentFriend != null)
        {
            Map<LocalDateTime, String> myMessages = currentFriend.getMyMessages();
            Map<LocalDateTime, String> friendMessages = currentFriend.getFriendMessages();
        }
    }

    public static void main(String[] args)
    {

    }

}
