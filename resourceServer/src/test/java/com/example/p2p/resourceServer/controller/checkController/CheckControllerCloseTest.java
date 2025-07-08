package com.example.p2p.resourceServer.controller.checkController;


import com.example.p2p.resourceServer.config.TestSecurityConfig;
import com.example.p2p.resourceServer.controller.CheckController;
import com.example.p2p.resourceServer.controller.ExceptionController;
import com.example.p2p.resourceServer.dto.CheckDTO;
import com.example.p2p.resourceServer.service.CheckService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(CheckController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import({TestSecurityConfig.class})
public class CheckControllerCloseTest {
    @MockitoBean
    private CheckService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() throws Exception {
    }

    @Test
    void testCloseCheckSuccess() throws Exception{
        long id = 123;
        doNothing().when(service).closeCheck(id);

        mockMvc.perform(delete("/checks/{id}", id))
                .andExpect(status().isNoContent())
                .andExpect(content().json("{\"message\":\"Check close successfully\"}"));

        verify(service).closeCheck(id);
    }

    @Test
    void testCloseCheckBlankID() throws Exception{
        mockMvc.perform(delete("/checks/"))
                .andExpect(status().isNotFound());
    }


    @Test
    void testCloseCheckInvalidID() throws Exception{
        mockMvc.perform(delete("/checks/{id}","invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCloseCheckLongToLarge() throws Exception{
        mockMvc.perform(delete("/checks/12345678901234678901235679"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCloseCheckNegativeID() throws Exception{
        mockMvc.perform(delete("/checks/{id}","-14"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCloseCheckZeroID() throws Exception {
        mockMvc.perform(delete("/checks/0"))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testEmptyResultDataAccessException() throws Exception {
        long id = 1;

        doThrow(new EmptyResultDataAccessException("No row", 1))
                .when(service).closeCheck(id);

        mockMvc.perform(delete("/checks/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource not found"))
                .andExpect(jsonPath("$.details").exists());
    }

    @Test
    void testDataAccessException() throws Exception {
        long id = 1;

        doThrow(new DataAccessException("DB is down") {})
                .when(service).closeCheck(id);

        mockMvc.perform(delete("/checks/{id}", id))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Database error"))
                .andExpect(jsonPath("$.details").exists());
    }
}
