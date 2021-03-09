package com.speakeasy;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.time.LocalDate;

public class Bubble extends JPanel
{
    int maxWidth;
    String unformattedMessage;
    boolean update;
    JButton messageDialog;
    JLabel timestampLabel;

    public Bubble(String message, LocalDate timestamp, int maxWidth)
    {
        setLayout(new BorderLayout());
        messageDialog = new JButton(message);
        add(messageDialog, BorderLayout.CENTER);
        update = true;
        this.unformattedMessage = message;
        this.maxWidth = maxWidth;
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mma z");
//        this.timestampLabel = new JLabel(timestamp.format(dateTimeFormatter));
        this.timestampLabel = new JLabel(timestamp.toString());
//        add(timestampLabel, BorderLayout.SOUTH);
    }

    public void breakLine(Font f, FontRenderContext fontRenderContext)
    {
        String[] words = unformattedMessage.split("\\s+");

        int i = 0;
        Rectangle2D bounds = new Rectangle2D.Double();
        StringBuilder lineBuilder, formattedLineBuilder;
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("<html>");
        double widestLine = 0;
        boolean finished = false;

        while (!finished)
        {
            lineBuilder = new StringBuilder();
            formattedLineBuilder = new StringBuilder();
            int wordWidth = 0;
            do
            {
                formattedLineBuilder.append(words[i]);
                if (!words[i].contains("<"))
                    lineBuilder.append(words[i]);
                ++i;
                lineBuilder.append(" ");
                formattedLineBuilder.append(" ");
                Rectangle2D proposedBounds = f.getStringBounds(lineBuilder.toString(), fontRenderContext);

                if (wordWidth > 0)
                    wordWidth = (int)(proposedBounds.getWidth() - bounds.getWidth());
                bounds = f.getStringBounds(lineBuilder.toString(), fontRenderContext);

                if (i == words.length || bounds.getWidth() >= maxWidth)
                    break;

            } while (true);

            if (i < words.length)
            {
                if (bounds.getWidth() - wordWidth > widestLine)
                    widestLine = bounds.getWidth() - wordWidth;
                int lineCharCount = lineBuilder.length();
                --i;
                lineBuilder = lineBuilder.delete(lineCharCount - words[i].length(), lineCharCount - 1);
                lineBuilder.append("<br>");
                formattedLineBuilder.append("<br>");
            }
            else
            {
                if (bounds.getWidth() > widestLine)
                    widestLine = bounds.getWidth();
                finished = true;
            }
            messageBuilder.append(formattedLineBuilder.toString());
        }
        messageBuilder.append("</html>");
        messageDialog.setText(messageBuilder.toString());
    }

    public JButton getMessageDialog()
    {
        return messageDialog;
    }

    public JLabel getTimestampLabel()
    {
        return timestampLabel;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (update)
        {
            Graphics2D g2 = (Graphics2D) getGraphics();
            Font f = g2.getFont();
            FontRenderContext fontRenderContext = g2.getFontRenderContext();
            breakLine(f, fontRenderContext);
            update = false;
        }
    }
}
