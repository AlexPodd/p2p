package com.example.p2p.resourceServer.controller.p2pController;


import com.example.p2p.resourceServer.config.TestSecurityConfig;
import com.example.p2p.resourceServer.controller.CheckController;
import com.example.p2p.resourceServer.controller.P2PController;
import com.example.p2p.resourceServer.dto.CheckDTO;
import com.example.p2p.resourceServer.dto.TransferDTO;
import com.example.p2p.resourceServer.service.CheckService;
import com.example.p2p.resourceServer.service.TransferService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(P2PController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import({TestSecurityConfig.class})
public class P2pControllerTest {

    @MockitoBean
    private TransferService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Test
    void contextLoads() throws Exception {
    }

    @Test
    void testInitiateSuccess() throws Exception {
        TransferDTO dto = new TransferDTO(1L, 2L, "0.50");
        when(service.initiate(any(TransferDTO.class))).thenReturn(123L);


        mockMvc.perform(post("/bankTransfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Transfer is prepared. Please confirm."))
                .andExpect(jsonPath("$.transactionId").value(123L));
    }

    @Test
    void testInitiateZeroFrom() throws Exception{
        TransferDTO dto = new TransferDTO(0, 2L, "100.50");

        mockMvc.perform(post("/bankTransfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testInitiateZeroTo() throws Exception{
        TransferDTO dto = new TransferDTO(2, 0, "100.50");

        mockMvc.perform(post("/bankTransfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testInitiateNegativeFrom() throws Exception{
        TransferDTO dto = new TransferDTO(-2, 2L, "100.50");

        mockMvc.perform(post("/bankTransfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testInitiateNegativeTo() throws Exception{
        TransferDTO dto = new TransferDTO(2, -5, "100.50");

        mockMvc.perform(post("/bankTransfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testInitiateBlankSum() throws Exception{
        TransferDTO dto = new TransferDTO(5, 2L, "");

        mockMvc.perform(post("/bankTransfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testInitiateZeroSum() throws Exception{
        TransferDTO dto = new TransferDTO(5, 2L, "0.0");

        mockMvc.perform(post("/bankTransfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testInitiateNegativeSum() throws Exception{
        TransferDTO dto = new TransferDTO(5, 2L, "-2.9");

        mockMvc.perform(post("/bankTransfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }




    @Test
    void testCloseTransactionSuccess() throws Exception{
        long id = 123;
        doNothing().when(service).cancelTransfer(id);

        mockMvc.perform(delete("/bankTransfer/{id}", id))
                .andExpect(status().isNoContent())
                .andExpect(content().json("{\"message\":\"Transfer is cancel.\"}"));

        verify(service).cancelTransfer(id);
    }


    @Test
    void testDataAccessException() throws Exception {
        long id = 1;

        doThrow(new DataAccessException("DB is down") {})
                .when(service).cancelTransfer(id);

        mockMvc.perform(delete("/bankTransfer/{id}", id))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Database error"))
                .andExpect(jsonPath("$.details").exists());
    }


    @Test
    void testEmptyResultDataAccessException() throws Exception {
        long id = 1;

        doThrow(new EmptyResultDataAccessException("No row", 1))
                .when(service).cancelTransfer(id);

        mockMvc.perform(delete("/bankTransfer/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource not found"))
                .andExpect(jsonPath("$.details").exists());
    }



    @Test
    void testConfirmTransactionInvalidID() throws Exception{
        mockMvc.perform(patch("/bankTransfer/{id}","invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testConfirmTransactionLongToLarge() throws Exception{
        mockMvc.perform(patch("/bankTransfer/12345678901234678901235679"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testConfirmTransactionNegativeID() throws Exception{
        mockMvc.perform(patch("/bankTransfer/{id}","-14"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testConfirmTransactionZeroID() throws Exception {
        mockMvc.perform(patch("/bankTransfer/0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testConfirmTransactionBlankID() throws Exception{
        mockMvc.perform(patch("/bankTransfer/"))
                .andExpect(status().isNotFound());
    }



    @Test
    void testCloseTransactionInvalidID() throws Exception{
        mockMvc.perform(delete("/bankTransfer/{id}","invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCloseTransactionLongToLarge() throws Exception{
        mockMvc.perform(delete("/bankTransfer/12345678901234678901235679"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCloseTransactionNegativeID() throws Exception{
        mockMvc.perform(delete("/bankTransfer/{id}","-14"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCloseTransactionZeroID() throws Exception {
        mockMvc.perform(delete("/bankTransfer/0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCloseTransactionBlankID() throws Exception{
        mockMvc.perform(delete("/bankTransfer/"))
                .andExpect(status().isNotFound());
    }


    @Test
    void testConfirmTransactionSuccess() throws Exception{
        long id = 123;
        doNothing().when(service).transfer(id);

        mockMvc.perform(patch("/bankTransfer/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"Transfer is do.\"}"));

        verify(service).transfer(id);
    }
}
