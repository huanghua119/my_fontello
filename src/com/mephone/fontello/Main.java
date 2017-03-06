package com.mephone.fontello;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.skin.BusinessBlackSteelSkin;
import org.jvnet.substance.skin.SubstanceBusinessBlueSteelLookAndFeel;
import org.jvnet.substance.title.FlatTitlePainter;

import com.mephone.fontello.ui.FontelloFrame;

public class Main {

    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        // JDialog.setDefaultLookAndFeelDecorated(true);
        try {
            UIManager
                    .setLookAndFeel(new SubstanceBusinessBlueSteelLookAndFeel());
            SubstanceLookAndFeel.setSkin(new BusinessBlackSteelSkin());
            SubstanceLookAndFeel.setCurrentTitlePainter(new FlatTitlePainter());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FontelloFrame fontello = new FontelloFrame();
                fontello.setVisible(true);
            }
        });
    }
}
