package com.speakeasy.logic;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.*;

public class Friend
{
    String nickname;

    static final Dimension AVATAR_MAX_SIZE = new Dimension(128, 128);

    Map<LocalDateTime, String> myMessages;
    Map<LocalDateTime, String> friendMessages;

    ImageIcon avatar;
    String iconFile;

    public Friend(String nickname, String iconFile)
    {
        this.nickname = nickname;
        this.myMessages = new TreeMap<>();
        this.friendMessages = new TreeMap<>();
        this.iconFile = iconFile;
        this.iconFile = null;
        this.avatar = null;
        if (iconFile != null)
        {
            this.avatar = new ImageIcon(iconFile);
            setAvatar(avatar);
        }
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public Map<LocalDateTime, String> getMyMessages()
    {
        return myMessages;
    }

    public Map<LocalDateTime, String> getFriendMessages()
    {
        return friendMessages;
    }

    public Map<LocalDateTime, Map.Entry<Boolean, String>> getCombinedMessages()
    {

        Map<LocalDateTime, Map.Entry<Boolean, String>> combinedMessages = new TreeMap<>();

        myMessages.forEach((key, value) ->
                combinedMessages.put(key, new AbstractMap.SimpleEntry<>(true, value)));
        friendMessages.forEach((key, value) ->
                combinedMessages.put(key, new AbstractMap.SimpleEntry<>(false, value)));

        return combinedMessages;
    }

    public void addMyMessage(LocalDateTime time ,String message)
    {
        myMessages.put(time, message);
    }

    public void addFriendMessage(LocalDateTime time, String message)
    {
        friendMessages.put(time, message);
    }

    public ImageIcon getAvatar()
    {
        return avatar;
    }

    private void setAvatar(ImageIcon avatar)
    {
        this.avatar = avatar;
        int w = Math.min(avatar.getIconWidth(), AVATAR_MAX_SIZE.width);
        int h = Math.min(avatar.getIconHeight(), AVATAR_MAX_SIZE.height);
        if (w == avatar.getIconWidth() || h == avatar.getIconHeight())
            this.avatar.setImage(
                    this.avatar.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
    }

    public String getIconFile()
    {
        return iconFile;
    }

    public void setIconFile(String iconFile)
    {
        this.iconFile = iconFile;
        this.avatar = new ImageIcon(iconFile);
        setAvatar(avatar);
    }
}
