package com.makesquare.server.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Tests the authentication endpoints end to end, through the real filter chain.
 *
 * <p>Every test registers its own account, because the store is shared by the whole test
 * context and a name can only be taken once.
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    private static final String PASSWORD = "correct-horse-battery";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String registrationBody(String username) throws Exception {
        return objectMapper.writeValueAsString(Map.of(
                "username", username,
                "email", username + "@example.com",
                "password", PASSWORD));
    }

    private String loginBody(String username) throws Exception {
        return objectMapper.writeValueAsString(Map.of(
                "username", username,
                "password", PASSWORD));
    }

    private String tokenFor(String username) throws Exception {
        String response = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationBody(username)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return objectMapper.readTree(response).get("token").asText();
    }

    @Test
    void registeringReturnsAnAccountAndAToken() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationBody("gopal-new")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user.username").value("gopal-new"))
                .andExpect(jsonPath("$.user.role").value("PLAYER"))
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.user.passwordHash").doesNotExist());
    }

    @Test
    void registeringTheSameUsernameTwiceIsAConflict() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registrationBody("madhav-conflict")))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationBody("madhav-conflict")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("username_taken"));
    }

    @Test
    void signingInWithTheRightPasswordReturnsAToken() throws Exception {
        tokenFor("gopal-login");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody("gopal-login")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.username").value("gopal-login"))
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void signingInWithTheWrongPasswordIsRefused() throws Exception {
        tokenFor("gopal-wrong");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "username", "gopal-wrong",
                                "password", "wrong-horse-battery"))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("invalid_credentials"));
    }

    @Test
    void aProtectedEndpointNeedsAToken() throws Exception {
        mockMvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("authentication_required"));
    }

    @Test
    void aTokenIdentifiesTheAccount() throws Exception {
        String token = tokenFor("gopal-me");

        mockMvc.perform(get("/api/v1/auth/me").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("gopal-me"));
    }

    @Test
    void aTokenThatWasNeverIssuedIsRefused() throws Exception {
        mockMvc.perform(get("/api/v1/auth/me").header("Authorization", "Bearer not-a-token"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("invalid_token"));
    }

    @Test
    void signingOutStopsTheTokenWorking() throws Exception {
        String token = tokenFor("gopal-logout");

        mockMvc.perform(post("/api/v1/auth/logout").header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/auth/me").header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void theStatusEndpointStaysPublic() throws Exception {
        mockMvc.perform(get("/api/v1/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ready"));
    }
}
