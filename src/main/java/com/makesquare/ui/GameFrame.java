package com.makesquare.ui;

import com.makesquare.constants.GameConstants;
import com.makesquare.logic.GameLogic;
import com.makesquare.model.GameState;
import com.makesquare.util.AudioPlayer;
import com.makesquare.util.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Main game window containing the game board and controls
 */
public class GameFrame extends JFrame {
    private final GameState gameState;
    private final GameLogic gameLogic;
    
    private final JLabel[] dots;
    private final JLabel[][] lines;
    private final JLabel[] boxes;
    
    private JLabel turnLabel;
    private JLabel player1Arrow;
    private JLabel player2Arrow;
    private JLabel player1Score;
    private JLabel player2Score;
    
    private int selectedDot1 = 0;
    private int selectedDot2 = 0;
    private boolean firstDotSelected = false;
    
    public GameFrame() {
        gameState = new GameState();
        gameLogic = new GameLogic();
        
        dots = new JLabel[36];
        lines = new JLabel[200][200];
        boxes = new JLabel[28];
        
        initializeUI();
        setVisible(true);
    }
    
    private void initializeUI() {
        setTitle("Make Square - Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(null);
        getContentPane().setBackground(GameConstants.BACKGROUND_COLOR);
        
        createDots();
        createLines();
        createBoxes();
        createScoreLabels();
        createPlayerImages();
        createArrows();
        createTurnLabel();
        createButtons();
        createGameTitle();
    }
    
    private void createDots() {
        int x = GameConstants.GRID_START_X;
        int y = GameConstants.GRID_START_Y;
        
        for (int i = 1; i <= GameConstants.TOTAL_DOTS; i++) {
            dots[i] = new JLabel();
            dots[i].setBackground(GameConstants.DOT_COLOR);
            dots[i].setOpaque(true);
            dots[i].setBounds(x, y, GameConstants.DOT_SIZE, GameConstants.DOT_SIZE);
            dots[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            final int dotIndex = i;
            dots[i].addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    handleDotClick(dotIndex);
                }
            });
            
            add(dots[i]);
            
            x += GameConstants.DOT_SPACING;
            if (i % GameConstants.DOTS_PER_ROW == 0) {
                y += GameConstants.DOT_SPACING;
                x = GameConstants.GRID_START_X;
            }
        }
    }
    
    private void createLines() {
        // Horizontal lines
        int x = GameConstants.GRID_START_X + 20;
        int y = GameConstants.GRID_START_Y + 9;
        
        for (int i = 1, j = 2; i <= 34; i++, j++) {
            if (i % GameConstants.DOTS_PER_ROW == 0) {
                i++;
                j++;
                y += GameConstants.DOT_SPACING;
                x = GameConstants.GRID_START_X + 20;
            }
            
            lines[i][j] = new JLabel();
            lines[i][j].setBackground(GameConstants.BACKGROUND_COLOR);
            lines[i][j].setOpaque(true);
            lines[i][j].setBounds(x, y, GameConstants.LINE_LENGTH, GameConstants.LINE_THICKNESS);
            add(lines[i][j]);
            
            x += GameConstants.DOT_SPACING;
        }
        
        // Vertical lines
        x = GameConstants.GRID_START_X + 9;
        y = GameConstants.GRID_START_Y + 20;
        
        for (int i = 1, j = 8; i <= 28; i++, j++) {
            lines[i][j] = new JLabel();
            lines[i][j].setBackground(GameConstants.BACKGROUND_COLOR);
            lines[i][j].setOpaque(true);
            lines[i][j].setBounds(x, y, GameConstants.LINE_THICKNESS, GameConstants.LINE_LENGTH);
            add(lines[i][j]);
            
            x += GameConstants.DOT_SPACING;
            if (i % GameConstants.DOTS_PER_ROW == 0) {
                y += GameConstants.DOT_SPACING;
                x = GameConstants.GRID_START_X + 9;
            }
        }
    }
    
    private void createBoxes() {
        int x = GameConstants.BOX_START_X;
        int y = GameConstants.BOX_START_Y;
        
        for (int i = 1; i <= 27; i++) {
            if (i % GameConstants.DOTS_PER_ROW == 0) {
                x = GameConstants.BOX_START_X;
                y += GameConstants.DOT_SPACING;
                i++;
            }
            
            boxes[i] = new JLabel();
            boxes[i].setBounds(x, y, GameConstants.BOX_SIZE, GameConstants.BOX_SIZE);
            boxes[i].setOpaque(true);
            boxes[i].setBackground(GameConstants.BACKGROUND_COLOR);
            boxes[i].setHorizontalAlignment(SwingConstants.CENTER);
            boxes[i].setFont(new Font("Arial", Font.BOLD, 20));
            add(boxes[i]);
            
            x += GameConstants.DOT_SPACING;
        }
    }
    
    private void createScoreLabels() {
        Font scoreFont = new Font("Arial", Font.BOLD, 25);
        
        player1Score = new JLabel(GameConstants.PLAYER1_NAME + "   : 0");
        player1Score.setBounds(270, 500, 200, 40);
        player1Score.setFont(scoreFont);
        player1Score.setForeground(GameConstants.PLAYER1_COLOR);
        add(player1Score);
        
        player2Score = new JLabel(GameConstants.PLAYER2_NAME + " : 0");
        player2Score.setBounds(1120, 500, 200, 40);
        player2Score.setFont(scoreFont);
        player2Score.setForeground(GameConstants.PLAYER2_COLOR);
        add(player2Score);
    }
    
    private void createPlayerImages() {
        JLabel player1Image = new JLabel();
        player1Image.setIcon(ResourceLoader.loadImage(GameConstants.IMAGE_PLAYER1));
        player1Image.setBounds(250, 250, 159, 225);
        add(player1Image);
        
        JLabel player2Image = new JLabel();
        player2Image.setIcon(ResourceLoader.loadImage(GameConstants.IMAGE_PLAYER2));
        player2Image.setBounds(1100, 250, 159, 225);
        add(player2Image);
        
        JLabel centerImage = new JLabel();
        centerImage.setIcon(ResourceLoader.loadImage(GameConstants.IMAGE_CENTER));
        centerImage.setBounds(810, 560, 200, 200);
        add(centerImage);
    }
    
    private void createArrows() {
        ImageIcon arrowIcon = ResourceLoader.loadImage(GameConstants.IMAGE_ARROW);
        
        player1Arrow = new JLabel(arrowIcon);
        player1Arrow.setBounds(335, 220, 30, 30);
        player1Arrow.setVisible(gameState.isPlayer1Turn());
        add(player1Arrow);
        
        player2Arrow = new JLabel(arrowIcon);
        player2Arrow.setBounds(1185, 220, 30, 30);
        player2Arrow.setVisible(!gameState.isPlayer1Turn());
        add(player2Arrow);
    }
    
    private void createTurnLabel() {
        turnLabel = new JLabel();
        turnLabel.setBounds(460, 660, 130, 40);
        turnLabel.setOpaque(true);
        turnLabel.setBackground(GameConstants.BUTTON_BG);
        turnLabel.setForeground(GameConstants.BUTTON_FG);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 15));
        updateTurnLabel();
        add(turnLabel);
    }
    
    private void createButtons() {
        // Restart button
        JButton restartButton = new JButton("RESTART");
        restartButton.setBounds(600, 660, 100, 40);
        restartButton.setOpaque(true);
        restartButton.setBackground(GameConstants.BUTTON_BG);
        restartButton.setForeground(GameConstants.BUTTON_FG);
        restartButton.setBorderPainted(false);
        restartButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        restartButton.addActionListener(e -> resetGame());
        add(restartButton);
        
        // Exit button
        JButton exitButton = new JButton("   EXIT  ");
        exitButton.setBounds(710, 660, 100, 40);
        exitButton.setOpaque(true);
        exitButton.setBackground(GameConstants.BUTTON_BG);
        exitButton.setForeground(GameConstants.BUTTON_FG);
        exitButton.setBorderPainted(false);
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitButton.addActionListener(e -> System.exit(0));
        add(exitButton);
    }
    
    private void createGameTitle() {
        JLabel title = new JLabel("Make Square");
        title.setBounds(610, 100, 300, 100);
        title.setForeground(Color.MAGENTA);
        title.setFont(new Font("Arial", Font.BOLD, 40));
        add(title);
    }
    
    private void handleDotClick(int dotIndex) {
        if (!firstDotSelected) {
            selectedDot1 = dotIndex;
            firstDotSelected = true;
            dots[dotIndex].setBackground(GameConstants.PLAYER1_COLOR);
        } else {
            selectedDot2 = dotIndex;
            dots[dotIndex].setBackground(GameConstants.PLAYER1_COLOR);
            processMove();
        }
    }
    
    private void processMove() {
        int from = Math.min(selectedDot1, selectedDot2);
        int to = Math.max(selectedDot1, selectedDot2);
        
        if (gameLogic.isValidMove(from, to, gameState)) {
            gameState.drawLine(from, to);
            
            Color lineColor = gameState.isPlayer1Turn() ? 
                GameConstants.PLAYER1_COLOR : GameConstants.PLAYER2_COLOR;
            lines[from][to].setBackground(lineColor);
            
            boolean squareCompleted = gameLogic.checkForCompletedSquare(from, to, gameState);
            
            if (squareCompleted) {
                handleSquareCompletion();
            } else {
                gameState.nextTurn();
            }
            
            updateUI();
            checkGameOver();
        }
        
        resetDotSelection();
    }
    
    private void handleSquareCompletion() {
        String audioPath = ResourceLoader.getAudioPath(GameConstants.AUDIO_SQUARE);
        if (!audioPath.isEmpty()) {
            AudioPlayer.playSound(audioPath);
        }
        
        int squareIndex = gameLogic.getSquareIndex(gameState);
        if (squareIndex != -1) {
            if (gameState.isPlayer1Turn()) {
                gameState.incrementPlayer1Score();
                boxes[squareIndex].setBackground(GameConstants.PLAYER1_COLOR);
                boxes[squareIndex].setText(GameConstants.PLAYER1_INITIAL);
                boxes[squareIndex].setForeground(Color.WHITE);
            } else {
                gameState.incrementPlayer2Score();
                boxes[squareIndex].setBackground(GameConstants.PLAYER2_COLOR);
                boxes[squareIndex].setText(GameConstants.PLAYER2_INITIAL);
                boxes[squareIndex].setForeground(Color.WHITE);
            }
            
            gameState.markBoxVisited(squareIndex, squareIndex + 1);
            gameState.markBoxVisited(squareIndex, squareIndex + 7);
            gameState.markBoxVisited(squareIndex + 1, squareIndex + 8);
            gameState.markBoxVisited(squareIndex + 7, squareIndex + 8);
        }
    }
    
    private void resetDotSelection() {
        if (selectedDot1 > 0 && selectedDot1 < dots.length) {
            dots[selectedDot1].setBackground(GameConstants.DOT_COLOR);
        }
        if (selectedDot2 > 0 && selectedDot2 < dots.length) {
            dots[selectedDot2].setBackground(GameConstants.DOT_COLOR);
        }
        selectedDot1 = 0;
        selectedDot2 = 0;
        firstDotSelected = false;
    }
    
    private void updateUI() {
        player1Score.setText(GameConstants.PLAYER1_NAME + "   : " + gameState.getPlayer1Score());
        player2Score.setText(GameConstants.PLAYER2_NAME + " : " + gameState.getPlayer2Score());
        updateTurnLabel();
        updateArrows();
    }
    
    private void updateTurnLabel() {
        if (gameState.isPlayer1Turn()) {
            turnLabel.setText("   " + GameConstants.PLAYER1_NAME + "'s turn");
        } else {
            turnLabel.setText("  " + GameConstants.PLAYER2_NAME + "'s turn");
        }
    }
    
    private void updateArrows() {
        player1Arrow.setVisible(gameState.isPlayer1Turn());
        player2Arrow.setVisible(!gameState.isPlayer1Turn());
    }
    
    private void checkGameOver() {
        if (gameState.isGameOver()) {
            ImageIcon winnerIcon = ResourceLoader.loadImage(GameConstants.IMAGE_WINNER);
            JOptionPane.showMessageDialog(this, gameState.getWinner(), 
                "Game Over", JOptionPane.INFORMATION_MESSAGE, winnerIcon);
            resetGame();
        }
    }
    
    private void resetGame() {
        gameState.reset();
        
        // Reset all lines
        for (int i = 1; i <= 34; i++) {
            int j = i + 1;
            if (i % GameConstants.DOTS_PER_ROW == 0) continue;
            if (lines[i][j] != null) {
                lines[i][j].setBackground(GameConstants.BACKGROUND_COLOR);
            }
        }
        
        for (int i = 1; i <= 28; i++) {
            int j = i + 7;
            if (lines[i][j] != null) {
                lines[i][j].setBackground(GameConstants.BACKGROUND_COLOR);
            }
        }
        
        // Reset all boxes
        for (int i = 1; i <= 27; i++) {
            if (i % GameConstants.DOTS_PER_ROW == 0) continue;
            if (boxes[i] != null) {
                boxes[i].setBackground(GameConstants.BACKGROUND_COLOR);
                boxes[i].setText("");
            }
        }
        
        resetDotSelection();
        updateUI();
    }
}
