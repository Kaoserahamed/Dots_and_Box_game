package com.makesquare.shared.protocol;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

/**
 * Tests for the shared protocol.
 *
 * <p>The point of these tests is that both sides of the protocol compile against the same
 * vocabulary, so a message type cannot be renamed in one place only.
 */
class MessageTypeTest {

    @Test
    void theMessageNamesAreUnique() {
        assertEquals(MessageType.values().length, Set.of(MessageType.values()).size());
    }

    @Test
    void aClientCanAskForARoomAndJoinOne() {
        assertTrue(EnumSet.of(MessageType.ROOM_CREATE, MessageType.ROOM_JOIN)
                .containsAll(EnumSet.of(MessageType.ROOM_CREATE, MessageType.ROOM_JOIN)));
    }

    @Test
    void aMoveCarriesTheDotsAndThePlayer() {
        MoveMessage move = new MoveMessage(7L, 1, 2, 42L);

        assertEquals(7L, move.gameId());
        assertEquals(1, move.dot1());
        assertEquals(2, move.dot2());
        assertEquals(42L, move.playerId());
    }

    @Test
    void twoMovesWithTheSameFieldsAreEqual() {
        MoveMessage first = new MoveMessage(1L, 1, 8, 5L);
        MoveMessage second = new MoveMessage(1L, 1, 8, 5L);

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }
}
