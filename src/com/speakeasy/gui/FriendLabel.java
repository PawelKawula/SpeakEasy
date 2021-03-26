package com.speakeasy.gui;

import com.speakeasy.logic.Friend;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class FriendLabel extends JPanel
{
    private Friend friend;
    private JLabel label;
    private JLabel icon;
    private ImageIcon imageIcon;

    public FriendLabel(Friend friend)
    {
        this.friend = friend;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createRaisedSoftBevelBorder());
        setFont(new Font("Lato", Font.BOLD, 18));

        label = new JLabel(friend.getNickname());

        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(Color.GRAY);
        add(label, BorderLayout.CENTER);

        imageIcon = new ImageIcon(new ImageIcon("manface.jpg").getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
        icon = new JLabel(imageIcon)
        {
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);

                int w = imageIcon.getIconWidth();
                int h = imageIcon.getIconHeight();
                int x = 0;
                int y = 0;
                double offset = w * (Math.sqrt(2) - 1);

                Graphics2D g2 = (Graphics2D) g;
                Stroke stroke = g2.getStroke();
                Color color = g2.getColor();
                g2.setStroke(new BasicStroke((float) offset));
                g2.setColor(label.getBackground());
                g2.drawRoundRect((int) (x - offset / 2), (int) (y - offset / 2),
                        (int) (w + offset), (int) (w + offset), (int) (w + offset), (int) (w + offset));
                g2.setStroke(stroke);
                g2.setColor(color);
            }
        };
        add(icon, BorderLayout.WEST);
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
