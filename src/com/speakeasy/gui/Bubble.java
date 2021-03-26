package com.speakeasy.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

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
        setBorder(null);
        setOpaque(false);
        messageDialog = new JButton(message);
        add(messageDialog, BorderLayout.CENTER);
        update = true;
        this.unformattedMessage = message;
        this.maxWidth = maxWidth;
        this.bubbleTimestamp = new BubbleTimestamp(timestamp);
        add(this.bubbleTimestamp.getjLabel(), BorderLayout.SOUTH);
        messageDialog.setFont(new Font("Deja Vu Sans", Font.BOLD, 12));
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
        Graphics2D g2 = (Graphics2D) getGraphics();
        if (update)
        {
            Font f = messageDialog.getFont();
            FontRenderContext fontRenderContext = g2.getFontRenderContext();
            breakLine(f, fontRenderContext);
            update = false;
        }
    }

    public BubbleTimestamp getBubbleTimestamp()
    {
        return bubbleTimestamp;
    }

    public static void main(String[] args)
    {
        Optional<String> biolinum = Arrays.stream(GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getAvailableFontFamilyNames()).filter((font) -> font.contains("Biolinum")).findFirst();
        biolinum.ifPresent(System.out::println);
    }
}

class BubbleTimestamp
{
    private JLabel jLabel;
    private LocalDateTime time;

    public BubbleTimestamp(LocalDateTime time)
    {
        this.time = time;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(" yyyy-MM-dd E HH:mm:ss ", Locale.forLanguageTag("pl"));
        jLabel = new JLabel(time.format(dateTimeFormatter));
        jLabel.setFont(new Font(Font.DIALOG, Font.ITALIC, 8));
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