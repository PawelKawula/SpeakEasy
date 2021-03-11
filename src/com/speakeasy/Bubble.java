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
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mma z");
//        this.timestampLabel = new JLabel(timestamp.format(dateTimeFormatter));
//        add(timestampLabel, BorderLayout.SOUTH);
        this.bubbleTimestamp = new BubbleTimestamp(timestamp);
        add(this.bubbleTimestamp.getjLabel(), BorderLayout.SOUTH);
    }

    //ta funkcja to porazka
    //placze evry tim
    public void breakLine(Font f, FontRenderContext fontRenderContext)
    {
        String[] words = unformattedMessage.split("\\s+");

        int i = 0;
        Rectangle2D bounds;
        StringBuilder lineBuilder, formattedLineBuilder;
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("<html>");
        double widestLine = 0;
        boolean finished = false;

        if (unformattedMessage.contains("Lorem"))
            System.out.println("");

        while (!finished)
        {
            lineBuilder = new StringBuilder();
            formattedLineBuilder = new StringBuilder();
            int wordWidth = 0;
            do
            {
                formattedLineBuilder.append(words[i]);
                if (words[i].contains("<"))
                {
                    while (!words[++i].contains("<"))
                        formattedLineBuilder.append(words[i]);
                    formattedLineBuilder.append(words[i]);
                }
                lineBuilder.append(words[i]);
                ++i;
                lineBuilder.append(" ");
                formattedLineBuilder.append(" ");
                bounds = f.getStringBounds(lineBuilder.toString(), fontRenderContext);

            } while (i != words.length && !(bounds.getWidth() >= maxWidth));

            lineBuilder = new StringBuilder(lineBuilder.substring(0, lineBuilder.length() - 1));

            if (!lineBuilder.toString().contains(" "))
            {
                int j = 0;
                StringBuilder partBuilder = new StringBuilder();
                StringBuilder formattedPartBuilder = new StringBuilder();
                Rectangle2D partBounds;

                do
                {
                    formattedLineBuilder.append(lineBuilder.charAt(j));
                    char ch = lineBuilder.charAt(j);
                    if (lineBuilder.charAt(j) == '<')
                    {
                        while (lineBuilder.charAt(++j) != '>' && j < lineBuilder.length())
                        {
                            ch = lineBuilder.charAt(j);
                            formattedLineBuilder.append(ch);
                        }

                        if (j == lineBuilder.length())
                           break;

                        formattedLineBuilder.append(ch);
                        ch = lineBuilder.charAt(++j);
                    }
                    partBuilder.append(ch);
                    formattedPartBuilder.append(ch);
                    partBounds = f.getStringBounds(partBuilder.toString(), fontRenderContext);

                    ++j;
                    if (partBounds.getWidth() >= maxWidth && j < lineBuilder.length())
                    {
                        formattedPartBuilder.append("<br>");
                        messageBuilder.append(formattedPartBuilder);
                        formattedPartBuilder = new StringBuilder();
                        partBuilder = new StringBuilder();
                    }
                    else if (j == lineBuilder.length())
                        break;

                } while (true);
                ++i;

                if (i >= words.length)
                    finished = true;
                else
                    formattedPartBuilder.append("<br>");
                    formattedLineBuilder = formattedPartBuilder;
            }
            else
            {
                if (i < words.length)
                {
                    if (bounds.getWidth() - wordWidth > widestLine)
                        widestLine = bounds.getWidth() - wordWidth;
                    int lineCharCount = lineBuilder.length();
                    --i;
                    lineBuilder.delete(lineCharCount - words[i].length(), lineCharCount - 1);
                    lineBuilder.append("<br>");
                    formattedLineBuilder.append("<br>");
                }
                else
                {
                    if (bounds.getWidth() > widestLine)
                        widestLine = bounds.getWidth();
                    finished = true;
                }
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
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("YYYY-mm-dd E HH:mm:ss", Locale.forLanguageTag("pl"));
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