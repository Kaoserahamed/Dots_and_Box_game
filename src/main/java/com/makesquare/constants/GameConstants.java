package com.makesquare.constants;

import java.awt.Color;

/**
 * Constants used throughout the game
 */
public class GameConstants {
    // Grid dimensions
    public static final int GRID_SIZE = 6;
    public static final int TOTAL_DOTS = 35;
    public static final int TOTAL_BOXES = 24;
    public static final int DOTS_PER_ROW = 7;
    
    // UI dimensions
    public static final int DOT_SIZE = 20;
    public static final int DOT_SPACING = 95;
    public static final int LINE_THICKNESS = 3;
    public static final int LINE_LENGTH = 75;
    public static final int BOX_SIZE = 40;
    
    // Starting positions
    public static final int GRID_START_X = 460;
    public static final int GRID_START_Y = 207;
    public static final int BOX_START_X = 500;
    public static final int BOX_START_Y = 245;
    
    // Colors
    public static final Color BACKGROUND_COLOR = Color.CYAN;
    public static final Color DOT_COLOR = Color.BLUE;
    public static final Color PLAYER1_COLOR = Color.RED;
    public static final Color PLAYER2_COLOR = Color.GREEN;
    public static final Color BUTTON_BG = new Color(50, 50, 50);
    public static final Color BUTTON_FG = Color.YELLOW;
    
    // Player names
    public static final String PLAYER1_NAME = "GOPAL";
    public static final String PLAYER2_NAME = "MADHAV";
    public static final String PLAYER1_INITIAL = "G";
    public static final String PLAYER2_INITIAL = "M";
    
    // Resource paths
    public static final String RESOURCE_PATH = "src/main/resources/";
    public static final String IMAGE_PLAYER1 = "gopu3.jpg";
    public static final String IMAGE_PLAYER2 = "madhab1.jpg";
    public static final String IMAGE_BACKGROUND = "Mainba (1).jpg";
    public static final String IMAGE_CENTER = "vas2.jpg";
    public static final String IMAGE_WINNER = "vas1.jpg";
    public static final String IMAGE_ARROW = "down-arrow.png";
    public static final String AUDIO_SQUARE = "audio3.wav";
    
    private GameConstants() {
        // Prevent instantiation
    }
}
