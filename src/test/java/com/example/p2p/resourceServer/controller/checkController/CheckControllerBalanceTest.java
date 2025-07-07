package com.example.p2p.resourceServer.controller.checkController;


import com.example.p2p.resourceServer.config.TestSecurityConfig;
import com.example.p2p.resourceServer.controller.CheckController;
import com.example.p2p.resourceServer.controller.ExceptionController;
import com.example.p2p.resourceServer.exeption.NoSuchCheckException;
import com.example.p2p.resourceServer.service.CheckService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CheckController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import({TestSecurityConfig.class})
public class CheckControllerBalanceTest {
    @MockitoBean
    private CheckService service;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void contextLoads() throws Exception {
    }

    @Test
    void testBalanceCheckSuccess() throws Exception{
        long id = 123;
        String someValue = "123.23";
        when(service.getBalance(id)).thenReturn(someValue);

        mockMvc.perform(get("/checks/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"Balance retrieved successfully\"}"));
        verify(service).getBalance(id);
    }

    @Test
    void testBalanceCheckBlankID() throws Exception{
        mockMvc.perform(get("/checks/"))
                .andExpect(status().isNotFound());
    }


    @Test
    void testBalanceCheckInvalidID() throws Exception{
        mockMvc.perform(get("/checks/{id}","invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testBalanceCheckLongToLarge() throws Exception{
        mockMvc.perform(get("/checks/12345678901234678901235679"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testBalanceCheckNegativeID() throws Exception{
        mockMvc.perform(get("/checks/{id}","-14"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testBalanceCheckZeroID() throws Exception {
        mockMvc.perform(get("/checks/0"))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testBalanceCheckNoSuchCheck() throws Exception{
        long id = 10;

        doThrow(new NoSuchCheckException()).when(service).getBalance(id);


        mockMvc.perform(get("/checks/{id}",id))
         .andExpect(status().isNotFound())
                .andExpect(content().json("{\"message\":\"Счет с таким номером не обнаружен!\"}"));
    }


    @Test
    void testNullPointerException() throws Exception {
        long id = 1;

        doThrow(new NullPointerException("oops"))
                .when(service).getBalance(id);

        mockMvc.perform(get("/checks/{id}", id))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Null pointer exception"))
                .andExpect(jsonPath("$.details").exists());
    }
}
