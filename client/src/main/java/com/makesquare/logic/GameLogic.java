package com.makesquare.logic;

import com.makesquare.model.GameState;

/**
 * Core game logic for checking valid moves and detecting completed squares
 */
public class GameLogic {
    private static final int DOTS_PER_ROW = 7;
    
    public boolean isValidMove(int dot1, int dot2, GameState state) {
        if (dot1 <= 0 || dot2 <= 0 || dot1 >= 35 || dot2 >= 36) {
            return false;
        }
        
        if (state.isLineDrawn(dot1, dot2)) {
            return false;
        }
        
        int diff = dot2 - dot1;
        return diff == 1 || diff == DOTS_PER_ROW;
    }
    
    public boolean checkForCompletedSquare(int dot1, int dot2, GameState state) {
        boolean squareCompleted = false;
        
        for (int i = 1; i < 28; i++) {
            if (i % DOTS_PER_ROW == 0) {
                continue;
            }
            
            int top = i;
            int topRight = i + 1;
            int bottomLeft = i + DOTS_PER_ROW;
            int bottomRight = i + DOTS_PER_ROW + 1;
            
            if (isSquareComplete(top, topRight, bottomLeft, bottomRight, state) &&
                !isSquareVisited(top, topRight, bottomLeft, bottomRight, state)) {
                
                markSquareVisited(top, topRight, bottomLeft, bottomRight, state);
                squareCompleted = true;
                break;
            }
        }
        
        return squareCompleted;
    }
    
    private boolean isSquareComplete(int top, int topRight, int bottomLeft, int bottomRight, GameState state) {
        return state.isLineDrawn(top, topRight) &&
               state.isLineDrawn(top, bottomLeft) &&
               state.isLineDrawn(topRight, bottomRight) &&
               state.isLineDrawn(bottomLeft, bottomRight);
    }
    
    private boolean isSquareVisited(int top, int topRight, int bottomLeft, int bottomRight, GameState state) {
        return state.isBoxVisited(top, topRight) &&
               state.isBoxVisited(top, bottomLeft) &&
               state.isBoxVisited(topRight, bottomRight) &&
               state.isBoxVisited(bottomLeft, bottomRight);
    }
    
    private void markSquareVisited(int top, int topRight, int bottomLeft, int bottomRight, GameState state) {
        state.markBoxVisited(top, topRight);
        state.markBoxVisited(top, bottomLeft);
        state.markBoxVisited(topRight, bottomRight);
        state.markBoxVisited(bottomLeft, bottomRight);
    }
    
    public int getSquareIndex(GameState state) {
        for (int i = 1; i < 28; i++) {
            if (i % DOTS_PER_ROW == 0) {
                continue;
            }
            
            int top = i;
            int topRight = i + 1;
            int bottomLeft = i + DOTS_PER_ROW;
            int bottomRight = i + DOTS_PER_ROW + 1;
            
            if (isSquareComplete(top, topRight, bottomLeft, bottomRight, state) &&
                !isSquareVisited(top, topRight, bottomLeft, bottomRight, state)) {
                return i;
            }
        }
        return -1;
    }
}
