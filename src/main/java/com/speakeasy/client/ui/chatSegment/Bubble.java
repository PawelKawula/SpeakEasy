package com.speakeasy.client.ui.chatSegment;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Bubble extends JPanel
{
    private final   int maxWidth;
    private final   String rawMessage;
    private boolean update;
    private final   JButton messageDialog;
    private final   BubbleTimestamp bubbleTimestamp;

    public Bubble(String message, LocalDateTime timestamp, int maxWidth)
    {
        setLayout(new BorderLayout());
        setBorder(null);
        setOpaque(false);
        messageDialog = new JButton(message);
        add(messageDialog, BorderLayout.CENTER);
        update = true;
        this.rawMessage = message.replaceAll("\n", "<br>");
        this.maxWidth = maxWidth;
        this.bubbleTimestamp = new BubbleTimestamp(timestamp);
        add(this.bubbleTimestamp.getTimeLabel(), BorderLayout.SOUTH);
        messageDialog.setFont(new Font("Deja Vu Sans", Font.BOLD, 12));
    }

    public void setUpdate(boolean update)
    {
        this.update = update;
    }

    public void breakLine()
    {
        Font f = getFont();
        String[] words = rawMessage.split("\\s+");
        int i = 0;
        StringBuilder messageBuilder = new StringBuilder();
        StringBuilder lineBuilder;
        StringBuilder formattedLineBuilder;

        int maxWidthCharCount = 0;

        double myWidth;
        do
        {
            maxWidthCharCount += 4;
            messageBuilder.append("MMMM");
            myWidth = f.getSize() * messageBuilder.length();
        } while (myWidth < maxWidth);

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
                        formattedPartBuilder.append(part).append("<br>");
                        j = end;
                    } else
                    {
                        lineBuilder.append(part).append(" ");
                        formattedLineBuilder.append(part).append(" ");
                        j = end;
                    }
                } while (j < wordLength);

                messageBuilder.append(formattedPartBuilder);
                ++i;
            } else
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
                lineBuilder.append(" ").append(words[i].replaceAll("<.*?>", ""));
                formattedLineBuilder.append(" ").append(words[i++]);
                lineLength = lineBuilder.length();
                if (i < words.length)
                    wordLength = words[i].length();
            }

            formattedLineBuilder.append("<br>");
            messageBuilder.append(formattedLineBuilder);

        } while (i < words.length);

        messageDialog.setText(messageBuilder + "</html>");
    }

    public JButton getMessageDialog()
    {
        return messageDialog;
    }

//    @Override
//    protected void paintComponent(Graphics g)
//    {
//        super.paintComponent(g);
//        Graphics2D g2 = (Graphics2D) getGraphics();
//        if (update)
//        {
//            Font f = messageDialog.getFont();
//            FontRenderContext fontRenderContext = g2.getFontRenderContext();
//            breakLine(f, fontRenderContext);
//            update = false;
//        }
//    }

    @Override
    public void revalidate()
    {
        super.revalidate();
        if (update)
        {
            breakLine();
            update = false;
        }
    }

    public BubbleTimestamp getBubbleTimestamp()
    {
        return bubbleTimestamp;
    }

    public class BubbleTimestamp
    {
        private final JLabel timeLabel;
        private final LocalDateTime time;

        public BubbleTimestamp(LocalDateTime time)
        {
            this.time = time;
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(" yyyy-MM-dd E HH:mm:ss ", Locale.forLanguageTag("pl"));
            timeLabel = new JLabel(time.format(dateTimeFormatter));
            timeLabel.setFont(new Font(Font.DIALOG, Font.ITALIC, 10));
        }

        public JLabel getTimeLabel()
        {
            return timeLabel;
        }

        public LocalDateTime getTime()
        {
            return time;
        }
    }
}
