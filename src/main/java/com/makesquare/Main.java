package com.makesquare;

import com.makesquare.ui.StartFrame;

/**
 * Main entry point for the Make Square game
 * A two-player dots and boxes game
 */
public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new StartFrame();
        });
    }
}
