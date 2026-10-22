package com.makesquare.server;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Reports what the server is and which version of it is running.
 *
 * <p>It exists so that a client, a container orchestrator or a person can tell a running
 * server from a server that has not started yet, without needing a credential.
 */
@RestController
@RequestMapping("/api/v1")
public class StatusController {

    /**
     * Report the service status.
     *
     * @return the service name, its version and whether it is ready to serve
     */
    @GetMapping("/status")
    public Map<String, Object> status() {
        return Map.of(
                "service", "dots-and-boxes-server",
                "version", "1.0.0",
                "status", "ready");
    }
}
