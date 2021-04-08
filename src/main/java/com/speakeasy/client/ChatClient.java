package com.speakeasy.client;

import com.speakeasy.client.ui.SpeakEasyFrame;

import javax.swing.*;
import java.awt.*;

public class ChatClient
{
    public static void main(String[] args)
    {
        EventQueue.invokeLater(() ->
        {
            try
            {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                System.setProperty("awt.useSystemAAFontSettings", "on");
                System.setProperty("swing.aatext", "true");

                SpeakEasyFrame frame = new SpeakEasyFrame();
                frame.setTitle("SpeakEasy");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        });
    }
}

