package com.makesquare.model;

/**
 * Manages the game state including board, scores, and current player
 */
public class GameState {
    private final int[][] lineState;
    private final int[][] boxVisited;
    private int player1Score;
    private int player2Score;
    private int currentPlayer;
    private int moveCount;
    
    public GameState() {
        lineState = new int[200][200];
        boxVisited = new int[200][200];
        reset();
    }
    
    public void reset() {
        for (int i = 0; i < 200; i++) {
            for (int j = 0; j < 200; j++) {
                lineState[i][j] = 0;
                boxVisited[i][j] = 0;
            }
        }
        player1Score = 0;
        player2Score = 0;
        currentPlayer = (int) (Math.random() * 2 + 1);
        moveCount = currentPlayer;
    }
    
    public boolean isLineDrawn(int from, int to) {
        return lineState[from][to] == 1;
    }
    
    public void drawLine(int from, int to) {
        lineState[from][to] = 1;
    }
    
    public boolean isBoxVisited(int from, int to) {
        return boxVisited[from][to] == 1;
    }
    
    public void markBoxVisited(int from, int to) {
        boxVisited[from][to] = 1;
    }
    
    public int getPlayer1Score() {
        return player1Score;
    }
    
    public int getPlayer2Score() {
        return player2Score;
    }
    
    public void incrementPlayer1Score() {
        player1Score++;
    }
    
    public void incrementPlayer2Score() {
        player2Score++;
    }
    
    public int getCurrentPlayer() {
        return currentPlayer;
    }
    
    public boolean isPlayer1Turn() {
        return moveCount % 2 == 1;
    }
    
    public void nextTurn() {
        moveCount++;
    }
    
    public void undoTurn() {
        moveCount--;
    }
    
    public boolean isGameOver() {
        return player1Score + player2Score == 24;
    }
    
    public String getWinner() {
        if (player1Score > player2Score) {
            return "Gopal Wins!";
        } else if (player2Score > player1Score) {
            return "Madhav Wins!";
        } else {
            return "It's a Draw!";
        }
    }
}
