package com.speakeasy.utils.fileIO;

import com.speakeasy.core.models.Friend;

import javax.xml.stream.*;
import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;

public class XMLChatReadWrite
{
    public static Friend readChat(File file) throws IOException, XMLStreamException
    {
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        XMLStreamReader parser = XMLInputFactory.newFactory().createXMLStreamReader(inputStream);
        int found = 0;
        Friend friend;
        String name = null;
        String avatar = null;

        while (parser.hasNext() && found < 2)
        {
            int event = parser.next();
            if (event == XMLStreamConstants.START_ELEMENT)
            {
                if (parser.getLocalName().equals("name"))
                {
                    name = parser.getElementText().trim();
                    ++found;
                } else if (parser.getLocalName().equals("avatar"))
                {
                    avatar = parser.getElementText().trim();
                    ++found;
                }
            }
        }

        friend = new Friend(name, avatar);

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
                    boolean mine = mineValue.equalsIgnoreCase("yes");

                    String messageText = null;
                    LocalDateTime timestamp = null;
                    found = 0;

                    while (parser.hasNext() && found < 2)
                    {
                        event = parser.next();

                        if (event == XMLStreamConstants.START_ELEMENT)
                        {
                            if (parser.getLocalName().equals("date"))
                            {
                                timestamp = LocalDateTime.parse(parser.getElementText().trim());
                                ++found;
                            } else if (parser.getLocalName().equals("text"))
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
        XMLStreamWriter writer = XMLOutputFactory.newFactory().
                createXMLStreamWriter(Files.newOutputStream(file.toPath()));

        writer.writeStartDocument();
        writer.writeCharacters("\n");
        writer.writeDTD("<!DOCTYPE chat SYSTEM \"chat.dtd\">");
        writer.writeCharacters("\n");
        writer.writeStartElement("chat");
        writer.writeCharacters("\n\t");
        writer.writeStartElement("author");
        writer.writeCharacters("\n\t\t");
        writer.writeStartElement("name");
        writer.writeCharacters(friend.getNickname());
        writer.writeEndElement();
        writer.writeCharacters("\n\t\t");
        writer.writeStartElement("name");
        writer.writeCharacters(friend.getIconFile());
        writer.writeEndElement();
        writer.writeCharacters("\n\t");
        writer.writeEndElement();

        Set<Map.Entry<LocalDateTime, Map.Entry<Boolean, String>>> combinedMessages =
                friend.getCombinedMessages().entrySet();

        for (Map.Entry<LocalDateTime, Map.Entry<Boolean, String>> entry : combinedMessages)
        {
            LocalDateTime key = entry.getKey();
            String text = entry.getValue().getValue();
            writer.writeCharacters("\n\t");
            writer.writeStartElement("message");
            if (entry.getValue().getKey())
                writer.writeAttribute("mine", "yes");
            writer.writeCharacters("\n\t\t");
            writer.writeStartElement("date");
            writer.writeCharacters(key.format(DateTimeFormatter.ISO_DATE_TIME));
            writer.writeEndElement();
            writer.writeCharacters("\n\t\t");
            writer.writeStartElement("text");
            writer.writeCharacters(text);
            writer.writeEndElement();
            writer.writeCharacters("\n\t");
            writer.writeEndElement();
        }

        writer.writeCharacters("\n");
        writer.writeEndDocument();
        return true;
    }
}
