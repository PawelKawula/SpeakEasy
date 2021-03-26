package com.speakeasy.gui;

import javax.swing.*;
import java.awt.*;

public class AvatarLabel extends JLabel
{
    private int icon_a;

    public AvatarLabel(ImageIcon icon, int icon_a)
    {
        super(icon);
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
        Stroke stroke = g2.getStroke();
        Color color = g2.getColor();
        g2.setStroke(new BasicStroke((float) offset));
        g2.setColor(getParent().getBackground());
        g2.drawRoundRect((int) (x - offset / 2), (int) (y - offset / 2),
                (int) (w + offset), (int) (w + offset), (int) (w + offset), (int) (w + offset));
        g2.setStroke(stroke);
        g2.setColor(color);
    }
}
