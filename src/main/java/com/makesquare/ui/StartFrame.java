package com.makesquare.ui;

import com.makesquare.constants.GameConstants;
import com.makesquare.util.ResourceLoader;

import javax.swing.*;
import java.awt.*;

/**
 * Initial welcome screen with start and exit buttons
 */
public class StartFrame {
    private final JFrame frame;
    
    public StartFrame() {
        frame = new JFrame("Make Square");
        initializeComponents();
        frame.setVisible(true);
    }
    
    private void initializeComponents() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(null);
        
        // Game title
        JLabel title = new JLabel("MAKE  SQUARE");
        title.setBounds(540, 140, 1000, 100);
        title.setForeground(Color.MAGENTA);
        title.setFont(new Font("Arial", Font.BOLD, 60));
        
        // Start button
        JLabel startButton = new JLabel("  START");
        startButton.setBounds(660, 400, 150, 40);
        startButton.setBackground(Color.YELLOW);
        startButton.setOpaque(true);
        startButton.setFont(new Font("Arial", Font.BOLD, 30));
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                frame.dispose();
                new GameFrame();
            }
        });
        
        // Exit button
        JButton exitButton = new JButton("   EXIT  ");
        exitButton.setBounds(660, 460, 150, 40);
        exitButton.setOpaque(true);
        exitButton.setBackground(Color.YELLOW);
        exitButton.setBorderPainted(false);
        exitButton.setFont(new Font("Arial", Font.BOLD, 30));
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitButton.addActionListener(e -> System.exit(0));
        
        // Background image
        JLabel background = new JLabel();
        background.setIcon(ResourceLoader.loadImage(GameConstants.IMAGE_BACKGROUND));
        background.setBounds(0, 0, 1550, 800);
        
        // Add components
        frame.add(startButton);
        frame.add(exitButton);
        frame.add(title);
        frame.add(background);
    }
}
