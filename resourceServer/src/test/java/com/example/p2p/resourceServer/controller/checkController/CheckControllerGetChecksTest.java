package com.example.p2p.resourceServer.controller.checkController;

import com.example.p2p.resourceServer.config.TestSecurityConfig;
import com.example.p2p.resourceServer.controller.CheckController;
import com.example.p2p.resourceServer.controller.ExceptionController;
import com.example.p2p.resourceServer.service.CheckService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CheckController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import({TestSecurityConfig.class})
public class CheckControllerGetChecksTest {
    @MockitoBean
    private CheckService service;

    @Autowired
    private MockMvc mockMvc;



    @Test
    void testGetChecksSuccess() throws Exception {
        ArrayList<Long> checkIds = new ArrayList<>(List.of(1L, 2L, 3L));
        when(service.getAllCheck()).thenReturn(checkIds);

        mockMvc.perform(get("/checks"))
                .andExpect(status().isOk())
                .andExpect(content().json("[1,2,3]"));

        verify(service).getAllCheck();
    }

    @Test
    void testGetChecksEmptyList() throws Exception {
        when(service.getAllCheck()).thenReturn( new ArrayList<>())  ;

        mockMvc.perform(get("/checks"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(service).getAllCheck();
    }


}
