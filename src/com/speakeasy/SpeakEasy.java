package com.speakeasy;


import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class SpeakEasy
{
    public static void main(String[] args)
    {
        try
        {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        JFrame frame = new SpeakEasyFrame();
        frame.setTitle("SpeakEasy");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

class SpeakEasyFrame extends JFrame
{
    JPanel chatPanel;
    JPanel chatBoxPanel;
    JPanel chatInputPanel;
    JPanel friendsPanel;
    JPanel friendsListPanel;
    JPanel friendsInputPanel;

    JTextArea chatInput;
    JButton chatSubmit;

    JTextArea friendsInput;
    JButton friendsSubmit;

    public SpeakEasyFrame()
    {
        setLayout(new BorderLayout());

        chatPanel = new JPanel();
        add(chatPanel, BorderLayout.CENTER);
        chatPanel.setLayout(new BorderLayout());
        chatPanel.setBorder(BorderFactory.createTitledBorder("chatPanel"));

        chatBoxPanel = new JPanel();
        chatPanel.add(chatBoxPanel, BorderLayout.CENTER);

        chatInputPanel = new JPanel();
        chatPanel.add(chatInputPanel, BorderLayout.SOUTH);
        chatInputPanel.setLayout(new BorderLayout());
        chatInput = new JTextArea(5, 30);
        chatInputPanel.add(chatInput, BorderLayout.CENTER);
        chatSubmit = new JButton("Wyślij");
        chatInputPanel.add(chatSubmit, BorderLayout.EAST);

        chatSubmit.addActionListener((event) ->
            {

            });

        friendsPanel = new JPanel();
        add(friendsPanel, BorderLayout.WEST);
        friendsPanel.setLayout(new BorderLayout());
        friendsPanel.setBorder(BorderFactory.createTitledBorder("friendsPanel"));

        friendsListPanel = new JPanel();
        JScrollPane friendsListScrollPane = new JScrollPane(friendsListPanel);
        friendsPanel.add(friendsListScrollPane, BorderLayout.CENTER);
        GridBagConstraints friendsListPanelGBC = new GridBagConstraints();
        friendsListPanelGBC.anchor = GridBagConstraints.FIRST_LINE_START;
        GridBagLayout friendsListPanelGBL = new GridBagLayout();
        friendsListPanelGBL.setConstraints(friendsListPanel, friendsListPanelGBC);
        friendsListPanel.setLayout(friendsListPanelGBL);
        friendsListPanel.setBorder(BorderFactory.createTitledBorder("friendsListPanel"));

        friendsInputPanel = new JPanel();
        friendsPanel.add(friendsInputPanel, BorderLayout.SOUTH);
        friendsInputPanel.setLayout(new BorderLayout());

        friendsInput = new JTextArea(1, 20);
        friendsInputPanel.add(friendsInput, BorderLayout.CENTER);

        friendsSubmit = new JButton("Dodaj");
        friendsInputPanel.add(friendsSubmit, BorderLayout.EAST);
        friendsSubmit.addActionListener((event) ->
            {
                FriendPanel friendPanel = new FriendPanel(friendsInput.getText());
                friendPanel.setBorder(BorderFactory.createTitledBorder("friend"));
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridy = friendsListPanel.getComponentCount();
                gbc.gridheight = 1;
                gbc.gridx = 0;
                gbc.weightx = 1;
                gbc.insets = new Insets(5, 5, 5, 5);
                gbc.anchor = GridBagConstraints.NORTH;
                gbc.fill = GridBagConstraints.CENTER;

                friendsListPanel.add(friendPanel, gbc);
                friendsPanel.revalidate();
                friendsPanel.repaint();
            });

        pack();
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(800, 600);
    }
}
