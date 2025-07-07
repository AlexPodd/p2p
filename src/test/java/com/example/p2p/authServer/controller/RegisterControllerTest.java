package com.example.p2p.authServer.controller;

import com.example.p2p.authServer.dto.UserDTO;
import com.example.p2p.authServer.service.UserService;
import com.example.p2p.resourceServer.controller.CheckController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistrationController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RegisterControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private final ObjectMapper mapper = new ObjectMapper();


    @Test
    void testSuccessfulRegistration() throws Exception {
        UserDTO dto = new UserDTO("newUser", "password123");

        when(userService.register(any(UserDTO.class))).thenReturn(true);

        mockMvc.perform(post("/register"))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json("{\"message\":\"User registered successfully\"}"));
        verify(userService.register(dto));
    }

    @Test
    void testConflictWhenUserExists() throws Exception {
        UserDTO dto = new UserDTO("existingUser", "password123");

        // userService.register(...) → false
        when(userService.register(any(UserDTO.class))).thenReturn(false);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect((ResultMatcher) content().json("{\"error\":\"User already exists\"}"));
    }

    @Test
    void testInvalidJsonInput() throws Exception {
        // Некорректный JSON (пропущено поле password)
        String invalidJson = "{\"username\":\"user\"}";

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}
