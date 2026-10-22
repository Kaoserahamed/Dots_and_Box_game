package com.makesquare.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.makesquare.model.GameState;
import org.junit.jupiter.api.Test;

/**
 * Tests for the core move validation and square detection.
 *
 * <p>The board is a seven-dot-wide grid numbered from one, so a square is bounded by
 * four lines: the top edge, the bottom edge, the left edge and the right edge.
 */
class GameLogicTest {

    private static final int DOTS_PER_ROW = 7;

    private final GameLogic logic = new GameLogic();
    private final GameState state = new GameState();

    @Test
    void acceptsAHorizontalMove() {
        assertTrue(logic.isValidMove(1, 2, state));
    }

    @Test
    void acceptsAVerticalMove() {
        assertTrue(logic.isValidMove(1, 1 + DOTS_PER_ROW, state));
    }

    @Test
    void rejectsANonAdjacentMove() {
        assertFalse(logic.isValidMove(1, 3, state));
    }

    @Test
    void rejectsAMoveBetweenDistantRows() {
        assertFalse(logic.isValidMove(1, 1 + (DOTS_PER_ROW * 2), state));
    }

    @Test
    void rejectsAMoveThatIsAlreadyDrawn() {
        state.drawLine(1, 2);

        assertFalse(logic.isValidMove(1, 2, state));
    }

    @Test
    void rejectsADotOutsideTheBoard() {
        assertFalse(logic.isValidMove(0, 1, state));
        assertFalse(logic.isValidMove(1, 0, state));
        assertFalse(logic.isValidMove(35, 36, state));
    }

    @Test
    void rejectsAMoveWhoseDotsRunBackwards() {
        assertFalse(logic.isValidMove(2, 1, state));
    }

    @Test
    void detectsNoSquareOnAnEmptyBoard() {
        assertFalse(logic.checkForCompletedSquare(1, 2, state));
        assertEquals(-1, logic.getSquareIndex(state));
    }

    @Test
    void detectsACompletedSquare() {
        completeSquare(1);

        assertTrue(logic.checkForCompletedSquare(1, 2, state));
    }

    @Test
    void reportsTheIndexOfACompletedSquare() {
        completeSquare(1);

        assertEquals(1, logic.getSquareIndex(state));
    }

    @Test
    void doesNotReportTheSameSquareTwice() {
        completeSquare(1);

        assertTrue(logic.checkForCompletedSquare(1, 2, state));
        assertFalse(logic.checkForCompletedSquare(1, 2, state));
        assertEquals(-1, logic.getSquareIndex(state));
    }

    @Test
    void detectsASquareInTheLastRow() {
        // The final square of a six-row board is bounded by dots 28 and 29.
        completeSquare(27);

        assertTrue(logic.checkForCompletedSquare(28, 29, state));
        assertEquals(27, logic.getSquareIndex(state));
    }

    @Test
    void aSquareNeedsAllFourEdges() {
        state.drawLine(1, 2);
        state.drawLine(1, 8);
        state.drawLine(8, 9);

        assertFalse(logic.checkForCompletedSquare(1, 2, state));
    }

    /**
     * Draw the four edges of the square whose top-left dot is {@code top}.
     */
    private void completeSquare(int top) {
        state.drawLine(top, top + 1);
        state.drawLine(top, top + DOTS_PER_ROW);
        state.drawLine(top + 1, top + DOTS_PER_ROW + 1);
        state.drawLine(top + DOTS_PER_ROW, top + DOTS_PER_ROW + 1);
    }
}
