package com.speakeasy.fileIO;

import com.speakeasy.ChatBoxPanel;
import com.speakeasy.logic.Friend;

import javax.swing.*;
import javax.xml.stream.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

public class XMLChatReadWrite
{
    public static Friend readChat(File file) throws IOException, XMLStreamException
    {
        InputStream inputStream = file.toURL().openStream();
        XMLStreamReader parser = XMLInputFactory.newFactory().createXMLStreamReader(inputStream);
        boolean foundAuthor = false;
        Friend friend = null;
        while (parser.hasNext() && !foundAuthor)
        {
            int event = parser.next();
            if (event == XMLStreamConstants.START_ELEMENT)
            {
                if (parser.getLocalName().equals("author"))
                {
                    friend = new Friend(parser.getElementText().trim());
                    foundAuthor = true;
                }
                if (parser.getLocalName().equals("message"))
                    return null;
            }
        }
        while (parser.hasNext())
        {
            int event = parser.next();
            if (event == XMLStreamConstants.START_ELEMENT)
            {
                if (parser.getLocalName().equals("message"))
                {
                    String mineValue = parser.getAttributeValue(null, "mine");
                    if (mineValue == null)
                        mineValue = "no";
                    boolean mine = mineValue.toLowerCase(Locale.ROOT).equals("yes") ? true : false;

                    String messageText = null;
                    LocalDateTime timestamp = null;
                    int found = 0;
                    while (parser.hasNext() && found < 2)
                    {

                        event = parser.next();
                        if (event == XMLStreamConstants.START_ELEMENT)
                        {
                            if (parser.getLocalName().equals("date"))
                            {
                                timestamp = LocalDateTime.parse(parser.getElementText().trim());
                                ++found;
                            }
                            else if (parser.getLocalName().equals("text"))
                            {
                                messageText = parser.getElementText().trim();
                                ++found;
                            }
                        }
                    }
                    if (mine)
                        friend.addMyMessage(timestamp, messageText);
                    else
                        friend.addFriendMessage(timestamp, messageText);
                }
            }
        }
        return friend;
    }

    public static boolean writeChat(Friend friend, File file) throws IOException, XMLStreamException
    {
        XMLStreamWriter writer = XMLOutputFactory.newFactory().createXMLStreamWriter(Files.newOutputStream(file.toPath()));

        writer.writeStartDocument();
        writer.writeCharacters("\n");
        writer.writeDTD("<!DOCTYPE chat SYSTEM \"chat.dtd\">");
        writer.writeCharacters("\n");
        writer.writeStartElement("chat");
        writer.writeCharacters("\n\t");
        writer.writeStartElement("author");
        writer.writeCharacters(friend.getNickname());
        writer.writeEndElement();
        for (Map.Entry<LocalDateTime, String> entry : friend.getMyMessages().entrySet())
        {
            LocalDateTime key = entry.getKey();
            String value = entry.getValue();
            writer.writeCharacters("\n\t");
            writer.writeStartElement("message");
            writer.writeAttribute("mine", "yes");
            writer.writeCharacters("\n\t\t");
            writer.writeStartElement("date");
            writer.writeCharacters(key.format(DateTimeFormatter.ISO_DATE_TIME));
            writer.writeEndElement();
            writer.writeCharacters("\n\t\t");
            writer.writeStartElement("text");
            writer.writeCharacters(value);
            writer.writeEndElement();
            writer.writeCharacters("\n\t");
            writer.writeEndElement();
        }

        for (Map.Entry<LocalDateTime, String> entry : friend.getFriendMessages().entrySet())
        {
            LocalDateTime key = entry.getKey();
            String value = entry.getValue();
            writer.writeCharacters("\n\t");
            writer.writeStartElement("message");
            writer.writeCharacters("\n\t\t");
            writer.writeStartElement("date");
            writer.writeCharacters(key.format(DateTimeFormatter.ISO_DATE_TIME));
            writer.writeEndElement();
            writer.writeCharacters("\n\t\t");
            writer.writeStartElement("text");
            writer.writeCharacters(value);
            writer.writeEndElement();
            writer.writeCharacters("\n\t");
            writer.writeEndElement();
        }
        writer.writeCharacters("\n");
        writer.writeEndDocument();
        return true;
    }

    public static void main(String[] args) throws IOException, XMLStreamException
    {
        try
        {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
//        Friend friend = readChat(new File("src/com/speakeasy/fileIO/samplechat.xml"));
        Friend friend = readChat(new File("export.xml"));
        JFrame frame = new JFrame("import");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ChatBoxPanel chatBoxPanel = new ChatBoxPanel();
        chatBoxPanel.setPreferredSize(new Dimension(600, 400));
        chatBoxPanel.setCurrentFriend(friend);
        frame.add(new JScrollPane(chatBoxPanel));
        frame.setVisible(true);
        frame.pack();
        chatBoxPanel.setCurrentFriend(friend);
    }

}
