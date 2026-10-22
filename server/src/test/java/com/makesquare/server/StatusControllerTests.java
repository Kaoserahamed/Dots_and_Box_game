package com.makesquare.server;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Tests for the status endpoint.
 *
 * <p>The whole application context is loaded rather than a web slice, because the
 * authentication middleware is part of the filter chain and it needs the service that resolves
 * a token.
 */
@SpringBootTest
@AutoConfigureMockMvc
class StatusControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void theStatusIsPublic() throws Exception {
        mockMvc.perform(get("/api/v1/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ready"));
    }

    @Test
    void theStatusNamesTheService() throws Exception {
        mockMvc.perform(get("/api/v1/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.service").value("dots-and-boxes-server"));
    }
}
