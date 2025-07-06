package com.example.p2p.resourceServer.controller.checkController;


import com.example.p2p.resourceServer.config.TestSecurityConfig;
import com.example.p2p.resourceServer.controller.CheckController;
import com.example.p2p.resourceServer.controller.ExceptionController;
import com.example.p2p.resourceServer.dto.CheckDTO;
import com.example.p2p.resourceServer.service.CheckService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CheckController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import({TestSecurityConfig.class, ExceptionController.class})
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
}
