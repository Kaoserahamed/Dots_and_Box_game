package com.makesquare.shared.protocol;

/**
 * The message types the client and the server exchange.
 *
 * <p>The two halves of the protocol live in one place on purpose. A client that invents a
 * message type and a server that invents a different one are two bugs, and the only way to
 * prevent that is for both to compile against the same enum.
 *
 * <p>The names ending in a verb are sent by the client, and the ones ending in a noun are sent
 * by the server.
 */
public enum MessageType {

    /** The client asks for a new room. */
    ROOM_CREATE,

    /** The client asks to join a room. */
    ROOM_JOIN,

    /** The client claims a line by joining two dots. */
    MOVE,

    /** The client asks for another game against the same opponent. */
    REMATCH,

    /** The client sends a message to the room chat. */
    CHAT,

    /** The client asks to watch a game without playing it. */
    SPECTATE,

    /** The server sends the current state of a room. */
    GAME_STATE,

    /** The server broadcasts a move to everyone in the room. */
    MOVE_BROADCAST,

    /** The server sends an updated leaderboard. */
    RATING_UPDATE,

    /** The server reports a problem with the request. */
    ERROR
}
