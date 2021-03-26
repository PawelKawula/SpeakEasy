package com.speakeasy;


import com.speakeasy.fileIO.XMLChatReadWrite;
import com.speakeasy.gui.SpeakEasyFrame;
import com.speakeasy.logic.Friend;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class SpeakEasy
{
    public static void main(String[] args)
    {
        EventQueue.invokeLater(() ->
        {
            try
            {
//            UIManager.put( "control", new Color( 128, 128, 128) );
//            UIManager.put( "info", new Color(128/3,128/3,128/3) );
//            UIManager.put( "nimbusBase", new Color( 18/3, 30/3, 49/3) );
//            UIManager.put( "nimbusAlertYellow", new Color( 248, 187, 0) );
//            UIManager.put( "nimbusDisabledText", new Color( 128, 128, 128) );
//            UIManager.put( "nimbusFocus", new Color(115/3,164/3,209/3) );
//            UIManager.put( "nimbusGreen", new Color(176,179,50) );
//            UIManager.put( "nimbusInfoBlue", new Color( 66, 139, 221) );
//            UIManager.put( "nimbusLightBackground", new Color( 18, 30, 49) );
//            UIManager.put( "nimbusOrange", new Color(191,98,4) );
//            UIManager.put( "nimbusRed", new Color(169,46,34) );
//            UIManager.put( "nimbusSelectedText", new Color( 255, 255, 255) );
//            UIManager.put( "nimbusSelectionBackground", new Color( 104, 93, 156) );
//            UIManager.put( "text", new Color( 230, 230, 230) );
//            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");

            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
//            UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");

//            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
//            UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");
//            UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");

            SpeakEasyFrame frame = new SpeakEasyFrame();
            frame.setTitle("SpeakEasy");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            Friend sampleFriend = XMLChatReadWrite.readChat(new File("export.xml"));
            frame.getChatBoxPanel().setCurrentFriend(sampleFriend);
            frame.getFriendsPanel().addFriend(sampleFriend);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        });
    }
}

