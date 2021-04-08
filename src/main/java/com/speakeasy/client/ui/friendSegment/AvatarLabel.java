package com.speakeasy.client.ui.friendSegment;

import javax.swing.*;
import java.awt.*;

public class AvatarLabel extends JLabel
{
    private final int icon_a;

    public AvatarLabel(ImageIcon icon, int icon_a)
    {
        super(new ImageIcon(icon.getImage().getScaledInstance(icon_a, icon_a, Image.SCALE_SMOOTH)));
        this.icon_a = icon_a;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        int w = icon_a;
        int x = 0;
        int y = 0;
        double offset = w * (Math.sqrt(2) - 1);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke((float) offset));
        g2.setColor(getParent().getBackground());
        g2.drawRoundRect((int) (x - offset / 2), (int) (y - offset / 2),
                (int) (w + offset), (int) (w + offset), (int) (w + offset), (int) (w + offset));
    }
}
