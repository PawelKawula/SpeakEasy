package com.speakeasy.gui;

import com.speakeasy.logic.Friend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FriendsPanel extends JPanel
{
    private ChatBoxPanel chatBoxPanel;
    private JPanel friendsListPanel;
    private FriendLabel choosenLabel;
    GridBagConstraints gbc;

    public FriendsPanel(ChatBoxPanel cBP)
    {
        chatBoxPanel = cBP;

        setLayout(new BorderLayout());
//        setBorder(BorderFactory.createLoweredBevelBorder());
        setBorder(null);
        gbc = new GridBagConstraints();
        gbc.gridy = 1;

        friendsListPanel = new JPanel();
        friendsListPanel.setBackground(SpeakEasyFrame.purple);
        friendsListPanel.setLayout(new GridBagLayout());

        GridBagConstraints labelGBC = new GridBagConstraints();
        labelGBC.weighty = 1;
        labelGBC.weightx = 0.01;
        labelGBC.fill = GridBagConstraints.BOTH;
        friendsListPanel.add(new JLabel(""), labelGBC);

        JScrollPane friendsListScrollPane = new JScrollPane(friendsListPanel);
        friendsListScrollPane.setBorder(null);
        add(friendsListScrollPane, BorderLayout.CENTER);

        JPanel friendsInputPanel = new JPanel();
//        friendsInputPanel.setBorder(BorderFactory.createCompoundBorder(
//                BorderFactory.createRaisedSoftBevelBorder(),
//                BorderFactory.createEmptyBorder(2, 4, 4, 4)));
//        friendsInputPanel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        friendsInputPanel.setLayout(new BorderLayout());
        add(friendsInputPanel, BorderLayout.SOUTH);

        JTextField friendsInput = new JTextField(12);
        friendsInputPanel.add(friendsInput, BorderLayout.CENTER);

        ImageIcon addIcon = new ImageIcon("plus.png");
        addIcon.setImage(addIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
        JButton friendsSubmit = new JButton(addIcon);
        friendsSubmit.setVerticalAlignment(SwingConstants.CENTER);
        friendsSubmit.setBorderPainted(false);
        friendsSubmit.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(-3,15,-3,15), friendsSubmit.getBorder()));
        friendsInputPanel.add(friendsSubmit, BorderLayout.EAST);
        friendsSubmit.addActionListener((event) ->
            {
                if (!friendsInput.getText().trim().equals("")) addFriend(new Friend(friendsInput.getText(), null));
            });
        choosenLabel = null;
    }

    public void addFriend(Friend friend)
    {
        FriendLabel friendLabel = new FriendLabel(friend);
        if (chatBoxPanel.getCurrentFriend() == friend)
        {
            friendLabel.getLabel().setForeground(Color.WHITE);
            choosenLabel = friendLabel;
        }

        friendLabel.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseReleased(MouseEvent e)
                {
                    super.mouseReleased(e);
                    choosenLabel.getLabel().setForeground(Color.GRAY);
                    chatBoxPanel.setCurrentFriend(friendLabel.getFriend());
                    friendLabel.getLabel().setForeground(Color.WHITE);
                    choosenLabel = friendLabel;
                    friendLabel.revalidate();
                    friendLabel.repaint();
                }

                @Override
                public void mouseEntered(MouseEvent e)
                {
                    super.mouseEntered(e);
                    friendLabel.getLabel().setForeground(Color.WHITE);
                }

                @Override
                public void mouseExited(MouseEvent e)
                {
                    super.mouseExited(e);
                    if (chatBoxPanel.getCurrentFriend() != friendLabel.getFriend())
                        friendLabel.getLabel().setForeground(Color.GRAY);
                }
            });

        JPopupMenu popup = new JPopupMenu();
        JMenuItem item = new JMenuItem("Delete Friend", new ImageIcon(
                new ImageIcon("deleteicon.png" )
                        .getImage().getScaledInstance(14, 14, Image.SCALE_DEFAULT)));
        item.addActionListener((deleteButtonEvent) ->
            {
                if (friendLabel.getFriend() == chatBoxPanel.getCurrentFriend())
                    chatBoxPanel.setCurrentFriend(null);
                friendsListPanel.remove(friendLabel);
                Component[] friendListComponents = friendsListPanel.getComponents();
                int i;
                for (i = 0; i < friendListComponents.length; ++i)
                    if (friendListComponents[i] instanceof FriendLabel)
                    {
                        FriendLabel friendLabel1 = (FriendLabel) friendListComponents[i];
                        friendLabel1.getLabel().setForeground(Color.WHITE);
                        chatBoxPanel.setCurrentFriend(friendLabel1.getFriend());
                    }
                revalidate();
                repaint();
            });
        popup.add(item);
        friendLabel.setComponentPopupMenu(popup);

        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        friendsListPanel.add(friendLabel, gbc);
        ++gbc.gridy;

        revalidate();
        repaint();
    }
}
