package com.makesquare.server;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

/**
 * Tests that the server starts.
 *
 * <p>A context that will not load is the most common way a server breaks, and it is worth
 * catching before anything else is built on top of it.
 */
@SpringBootTest
class ServerApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Test
    void theContextLoads() {
        assertNotNull(context);
    }

    @Test
    void theStatusControllerIsRegistered() {
        assertNotNull(context.getBean(StatusController.class));
    }
}
