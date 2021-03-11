package com.speakeasy;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Bubble extends JPanel
{
    private int maxWidth;
    private String unformattedMessage;
    private boolean update;
    private JButton messageDialog;
    private BubbleTimestamp bubbleTimestamp;

    public Bubble(String message, LocalDateTime timestamp, int maxWidth)
    {
        setLayout(new BorderLayout());
        messageDialog = new JButton(message);
        add(messageDialog, BorderLayout.CENTER);
        update = true;
        this.unformattedMessage = message;
        this.maxWidth = maxWidth;
        this.bubbleTimestamp = new BubbleTimestamp(timestamp);
        add(this.bubbleTimestamp.getjLabel(), BorderLayout.SOUTH);
    }

    public void breakLine(Font f, FontRenderContext fontRenderContext)
    {
        String[] words = unformattedMessage.split("\\s+");

        int i = 0;
        StringBuilder messageBuilder = new StringBuilder();
        StringBuilder lineBuilder;
        StringBuilder formattedLineBuilder;

        int maxWidthCharCount = 0;
        Rectangle2D bounds;

        do
        {
            maxWidthCharCount += 4;
            messageBuilder.append("MMMM");
            bounds = f.getStringBounds(messageBuilder.toString(), fontRenderContext);
        } while (bounds.getWidth() < maxWidth);

        messageBuilder = new StringBuilder("<html>");

        do
        {
            lineBuilder = new StringBuilder();
            formattedLineBuilder = new StringBuilder();
            String word = words[i];
            int wordLength = word.length();

            if (words[i].length() >= maxWidthCharCount)
            {
                int j = 0;
                StringBuilder formattedPartBuilder = new StringBuilder();

                do
                {
                    int end = Math.min(j + maxWidthCharCount, wordLength);
                    String part = word.substring(j, end);
                    if (end < wordLength)
                    {
                        formattedPartBuilder.append(part + "<br>");
                        j = end;
                    }
                    else
                    {
                        lineBuilder.append(part + " ");
                        formattedLineBuilder.append(part + " ");
                        j = end;
                    }
                } while (j < wordLength);

                messageBuilder.append(formattedPartBuilder.toString());
                ++i;
            }
            else
            {
                if (++i >= words.length)
                {
                    messageBuilder.append(word);
                    break;
                }
                lineBuilder.append(word);
                formattedLineBuilder.append(word);
                word = words[i];
                wordLength = word.length();
            }

            int lineLength = lineBuilder.length();

            while (lineLength + wordLength < maxWidthCharCount && i < words.length)
            {
                lineBuilder.append(" " + words[i].replaceAll("<.*?>", ""));
                formattedLineBuilder.append(" " + words[i++]);
                lineLength = lineBuilder.length();
                if (i < words.length)
                    wordLength = words[i].length();
            }

            formattedLineBuilder.append("<br>");
            messageBuilder.append(formattedLineBuilder.toString());

        } while (i < words.length);

        messageDialog.setText(messageBuilder.toString() + "</html>");
    }

    public JButton getMessageDialog()
    {
        return messageDialog;
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

    public void setColor(Color color)
    {
        messageDialog.setOpaque(false);
        messageDialog.setContentAreaFilled(false);
        messageDialog.setBorderPainted(false);
        messageDialog.setBackground(color);
        messageDialog.setForeground(Color.WHITE);
    }

    public BubbleTimestamp getBubbleTimestamp()
    {
        return bubbleTimestamp;
    }
}

class BubbleTimestamp
{
    private JLabel jLabel;
    private LocalDateTime time;

    public BubbleTimestamp(LocalDateTime time)
    {
        this.time = time;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd E HH:mm:ss", Locale.forLanguageTag("pl"));
        jLabel = new JLabel(time.format(dateTimeFormatter));
    }

    public JLabel getjLabel()
    {
        return jLabel;
    }

    public LocalDateTime getTime()
    {
        return time;
    }
}