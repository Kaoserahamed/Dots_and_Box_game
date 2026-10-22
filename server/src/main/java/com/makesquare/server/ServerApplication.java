package com.makesquare.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The GridBattle server.
 *
 * <p>The server is authoritative: it owns the rooms, the games and the players, and it decides
 * whether a move is legal before it is broadcast to the room.
 */
@SpringBootApplication
public class ServerApplication {

    /**
     * Start the server.
     *
     * @param args the command line arguments, passed through to Spring Boot
     */
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
