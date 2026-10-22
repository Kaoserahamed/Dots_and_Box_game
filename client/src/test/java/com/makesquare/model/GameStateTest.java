package com.makesquare.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests for the board state: drawn lines, visited boxes, scores and turn order.
 */
class GameStateTest {

    private static final int TOTAL_BOXES = 24;

    private final GameState state = new GameState();

    @Test
    void startsWithNoLinesDrawn() {
        assertFalse(state.isLineDrawn(1, 2));
    }

    @Test
    void startsWithNoBoxesVisited() {
        assertFalse(state.isBoxVisited(1, 2));
    }

    @Test
    void startsWithNoScore() {
        assertEquals(0, state.getPlayer1Score());
        assertEquals(0, state.getPlayer2Score());
    }

    @Test
    void remembersADrawnLine() {
        state.drawLine(4, 5);

        assertTrue(state.isLineDrawn(4, 5));
    }

    @Test
    void remembersAVisitedBox() {
        state.markBoxVisited(4, 5);

        assertTrue(state.isBoxVisited(4, 5));
    }

    @Test
    void countsScoresSeparately() {
        state.incrementPlayer1Score();
        state.incrementPlayer1Score();
        state.incrementPlayer2Score();

        assertEquals(2, state.getPlayer1Score());
        assertEquals(1, state.getPlayer2Score());
    }

    @Test
    void theTurnAlternates() {
        boolean before = state.isPlayer1Turn();

        state.nextTurn();

        assertNotEquals(before, state.isPlayer1Turn());
    }

    @Test
    void undoingATurnRestoresThePreviousPlayer() {
        boolean before = state.isPlayer1Turn();

        state.nextTurn();
        state.undoTurn();

        assertEquals(before, state.isPlayer1Turn());
    }

    @Test
    void theGameIsNotOverOnAnEmptyBoard() {
        assertFalse(state.isGameOver());
    }

    @Test
    void theGameIsOverOnceEveryBoxIsScored() {
        for (int i = 0; i < TOTAL_BOXES; i++) {
            state.incrementPlayer1Score();
        }

        assertTrue(state.isGameOver());
    }

    @Test
    void theFirstPlayerWinsWithMoreBoxes() {
        state.incrementPlayer1Score();

        assertEquals("Gopal Wins!", state.getWinner());
    }

    @Test
    void theSecondPlayerWinsWithMoreBoxes() {
        state.incrementPlayer2Score();

        assertEquals("Madhav Wins!", state.getWinner());
    }

    @Test
    void anEqualScoreIsADraw() {
        assertEquals("It's a Draw!", state.getWinner());
    }

    @Test
    void resettingClearsTheBoard() {
        state.drawLine(4, 5);
        state.incrementPlayer1Score();

        state.reset();

        assertFalse(state.isLineDrawn(4, 5));
        assertEquals(0, state.getPlayer1Score());
        assertEquals(0, state.getPlayer2Score());
    }
}
