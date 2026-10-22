package com.makesquare.shared.protocol;

/**
 * One move: the two dots a player joined with a line.
 *
 * <p>The dots are numbered from one, row by row, which is the same numbering the board uses on
 * the client. A move is only a claim: the server decides whether the claim is legal.
 *
 * @param gameId the game the move belongs to
 * @param dot1 the first dot, numbered from one
 * @param dot2 the second dot, numbered from one
 * @param playerId the player who made the move
 */
public record MoveMessage(long gameId, int dot1, int dot2, long playerId) {
}
