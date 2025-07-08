package com.example.p2p.resourceServer.controller.checkController;


import com.example.p2p.resourceServer.config.TestSecurityConfig;
import com.example.p2p.resourceServer.controller.CheckController;
import com.example.p2p.resourceServer.controller.ExceptionController;
import com.example.p2p.resourceServer.dto.CheckDTO;
import com.example.p2p.resourceServer.exeption.InvalidCheckTypeException;
import com.example.p2p.resourceServer.service.CheckService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CheckController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import({TestSecurityConfig.class})
public class CheckControllerCreateTest {

    @MockitoBean
    private CheckService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Test
    void contextLoads() throws Exception {
    }

    @Test
    void testCreateCheckSuccess() throws Exception{
        CheckDTO checkDTO = new CheckDTO("USD", "100.00123");

        doNothing().when(service).createCheck(checkDTO);

        mockMvc.perform(post("/checks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(checkDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"message\":\"Check created successfully\"}"));
    }

    @Test
    void testCreateCheckBlankType() throws Exception{
        CheckDTO checkDTO = new CheckDTO("", "100.00123");

        mockMvc.perform(post("/checks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(checkDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateCheckBlankBalance() throws Exception{
        CheckDTO checkDTO = new CheckDTO("USD", "");

        mockMvc.perform(post("/checks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(checkDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateCheckInvalidBalance() throws Exception{
        CheckDTO checkDTO = new CheckDTO("USD", "invalid");

        mockMvc.perform(post("/checks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(checkDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateCheckNegativeBalance() throws Exception{
        CheckDTO checkDTO = new CheckDTO("USD", "-100.90");

        mockMvc.perform(post("/checks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(checkDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateCheckZeroBalance() throws Exception{
        CheckDTO checkDTO = new CheckDTO("USD", "0.0");

        mockMvc.perform(post("/checks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(checkDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateCheckNullBalance() throws Exception{
        CheckDTO checkDTO = new CheckDTO("USD", null);

        mockMvc.perform(post("/checks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(checkDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateCheckNullType() throws Exception{
        CheckDTO checkDTO = new CheckDTO(null, "-100.90");

        mockMvc.perform(post("/checks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(checkDTO)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testCreateCheckInvalidType() throws Exception {
        CheckDTO checkDTO = new CheckDTO("UA", "100.00");

        Mockito.doThrow(new InvalidCheckTypeException())
                .when(service)
                .createCheck(any(CheckDTO.class));

        mockMvc.perform(post("/checks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(checkDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Неверный тип чека!"));
    }



}
