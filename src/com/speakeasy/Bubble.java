package com.speakeasy;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Bubble extends JPanel
{
    String message;
    ArrayList<Line> lines;
    double maxWidth;
    boolean update;

    public Bubble(String message, double maxWidth)
    {
        this.message = message;
        lines = new ArrayList<>();
        this.maxWidth = maxWidth;
        this.update = true;

        setBorder(new TitledBorder("Bubble"));
        setPreferredSize(new Dimension(200, 200));

    }

    public void breakLine(Font f, FontRenderContext fontRenderContext)
    {
        lines = new ArrayList<>();
        String[] words = message.split("\\s+");

        int i = 0;
        Rectangle2D bounds = new Rectangle2D.Double();
        StringBuilder lineBuilder;
        double widestLine = 0;
        double bubbleHeight = 0;
        boolean finished = false;

        while (!finished)
        {
            double lineHeight;
            Line line;
            lineBuilder = new StringBuilder();
            int wordWidth = 0;
            do
            {
                lineBuilder.append(words[i++]);
                lineBuilder.append(" ");
                Rectangle2D proposedBounds = f.getStringBounds(lineBuilder.toString(), fontRenderContext);
                if (wordWidth > 0)
                    wordWidth = (int)(proposedBounds.getWidth() - bounds.getWidth());
                bounds = f.getStringBounds(lineBuilder.toString(), fontRenderContext);

            } while (bounds.getWidth() < maxWidth && i < words.length);

            lineHeight = bounds.getHeight();
            bubbleHeight += lineHeight;

            if (i < words.length)
            {
                if (bounds.getWidth() - wordWidth > widestLine)
                    widestLine = bounds.getWidth() - wordWidth;
                int lineCharCount = lineBuilder.length();
                --i;
                lineBuilder = lineBuilder.delete(lineCharCount - words[i].length(), lineCharCount - 1);
                lineBuilder.append("\n");
            }
            else
            {
                if (bounds.getWidth() > widestLine)
                    widestLine = bounds.getWidth();
                finished = true;
            }
            line = new Line(lineBuilder.toString(), bubbleHeight - lineHeight);
            lines.add(line);
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if (update)
        {
            update = false;
            Font f = g2.getFont();
            FontRenderContext fontRenderContext = g2.getFontRenderContext();
            breakLine(f, fontRenderContext);
        }

        int yPos = 20;
        for (Line line : lines)
        {
            int lineHeight = (int) line.getHeight();
            g2.drawString(line.getMessage(), 10, 10 + yPos);
            System.out.println(yPos);
            yPos += lineHeight;
        }
    }

}

class Line
{
    double height;
    String message;

    public Line(String message, double height)
    {
        this.height = height;
        this.message = message;
    }

    public double getHeight()
    {
        return height;
    }

    public void setHeight(double height)
    {
        this.height = height;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}