package com.example.p2p.resourceServer.service;


import com.example.p2p.resourceServer.dto.TransferDTO;
import com.example.p2p.resourceServer.exeption.InvalidCheckTypeException;
import com.example.p2p.resourceServer.exeption.NoAccessToCheckException;
import com.example.p2p.resourceServer.exeption.NoSuchCheckException;
import com.example.p2p.resourceServer.exeption.transferException.ExpireTransactionException;
import com.example.p2p.resourceServer.exeption.transferException.InvalidCurrencyTypeToTransferException;
import com.example.p2p.resourceServer.exeption.transferException.NoEnoughCurrency;
import com.example.p2p.resourceServer.exeption.transferException.NoSuchTransferException;
import com.example.p2p.resourceServer.model.Transfer;
import com.example.p2p.resourceServer.repository.CheckRepository;
import com.example.p2p.resourceServer.repository.TransferRepository;
import com.example.p2p.resourceServer.util.CurrencyUtil;
import com.example.p2p.resourceServer.util.UserContext;
import com.example.p2p.resourceServer.util.transferValidatorChain.CheckAccessValid;
import com.example.p2p.resourceServer.util.transferValidatorChain.CheckIsExistValid;
import com.example.p2p.resourceServer.util.transferValidatorChain.EnoughValueValid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("resourceTest")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommonTransferServiceIntegrationTest {

    @Autowired
    private CommonTransferService service;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private CheckRepository checkRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM transfers");
        jdbcTemplate.execute("DELETE FROM checks");
        jdbcTemplate.execute("INSERT INTO checks (id, user_id, currency_code, amount_integer, amount_fraction) VALUES (1, 'testUser', 'USD', 100, 20)");
        jdbcTemplate.execute("INSERT INTO checks (id, user_id, currency_code, amount_integer, amount_fraction) VALUES (2, 'testUser1', 'USD', 100, 20)");
        jdbcTemplate.execute("INSERT INTO checks (id, user_id, currency_code, amount_integer, amount_fraction) VALUES (3, 'testUser1', 'BTC', 100, 20)");

        jdbcTemplate.execute("INSERT INTO transfers (id, currency_code,id_from, id_to, amount_integer, amount_fraction) " +
                "VALUES (1000, 'USD' , 1, 2, 0, 50)");

        jdbcTemplate.execute("INSERT INTO transfers (id, currency_code, id_from, id_to, amount_integer, amount_fraction, created_at) " +
                "VALUES (1001, 'USD', 1, 2, 0, 50, NOW() - INTERVAL 15 MINUTE)");
    }

    @Test
    @WithMockUser(username = "testUser")
    void testInitiateSuccess() throws Exception {
        TransferDTO dto = new TransferDTO(1L, 2L, "0.50");

        long transferId = service.initiate(dto);

        assertTrue(transferId > 0);
        assertNotNull(transferRepository.getTransfer(transferId, new Transfer()));
    }

    @Test
    @WithMockUser(username = "testUser")
    void testInitiateSuccessFrac() throws Exception {
        TransferDTO dto = new TransferDTO(1L, 2L, "100.19");

        long transferId = service.initiate(dto);

        assertTrue(transferId > 0);
        assertNotNull(transferRepository.getTransfer(transferId, new Transfer()));
    }


    @Test
    @WithMockUser(username = "testUser")
    void testInitiateNoCheck() throws Exception {
        TransferDTO dto = new TransferDTO(1L, 4, "0.50");
        assertThrows(NoSuchCheckException.class, () -> {
            service.initiate(dto);
        });
    }


    @Test
    @WithMockUser(username = "testUser")
    void testInitiateNoCheckFrom() throws Exception {
        TransferDTO dto = new TransferDTO(4L, 1, "0.50");
        assertThrows(NoSuchCheckException.class, () -> {
            service.initiate(dto);
        });
    }



    @Test
    @WithMockUser(username = "testUser")
    void testInitiateNoAccess() throws Exception {
        TransferDTO dto = new TransferDTO(2, 1, "0.50");
        assertThrows(NoAccessToCheckException.class, () -> {
            service.initiate(dto);
        });
    }



    @Test
    @WithMockUser(username = "testUser")
    void testInitiateNoEnoughCurrency() throws Exception {
        TransferDTO dto = new TransferDTO(1, 2, "10000000.50");
        assertThrows(NoEnoughCurrency.class, () -> {
            service.initiate(dto);
        });
    }

    @Test
    @WithMockUser(username = "testUser")
    void testInitiateNoEnoughCurrencyFrac() throws Exception {
        TransferDTO dto = new TransferDTO(1, 2, "100.50");
        assertThrows(NoEnoughCurrency.class, () -> {
            service.initiate(dto);
        });
    }


    @Test
    @WithMockUser(username = "testUser")
    void testInitiateNoCurrencyConvert() throws Exception {
        TransferDTO dto = new TransferDTO(1, 3, "0.50");
        assertThrows(InvalidCurrencyTypeToTransferException.class, () -> {
            service.initiate(dto);
        });
    }

    @Test
    @WithMockUser(username = "testUser")
    void testCancelTransferSuccess() throws Exception {
        TransferDTO dto = new TransferDTO(1L, 2L, "0.50");
        long transferId = service.initiate(dto);

        assertNotNull(transferRepository.getTransfer(transferId, new Transfer()));
         service.cancelTransfer(transferId);

        assertThrows(Exception.class, () -> {
            transferRepository.getTransfer(transferId, new Transfer());
        });
    }


    @Test
    @WithMockUser(username = "testUser1")
    void testCancelTransferNoAccess() throws Exception {
        assertThrows(NoAccessToCheckException.class, () -> {
            service.cancelTransfer(1000);
        });
    }

    @Test
    @WithMockUser(username = "testUser")
    void testDoTransferSuccess() throws Exception {
        assertDoesNotThrow(() -> service.transfer(1000));
    }

    @Test
    @WithMockUser(username = "testUser")
    void testDoTransferExpired() throws Exception {
        assertThrows(ExpireTransactionException.class, () -> {
            service.transfer(1001);
        });
    }


    @Test
    @WithMockUser(username = "testUser1")
    void testDoTransferNoAccess() throws Exception {
        assertThrows(NoAccessToCheckException.class, () -> {
            service.transfer(1000);
        });
    }


    @Test
    @WithMockUser(username = "testUser")
    void fullTransferLifecycleTest() throws Exception {
        TransferDTO dto = new TransferDTO(1L, 2L, "10.25");

        long transferId = service.initiate(dto);
        assertTrue(transferId > 0);

        Transfer createdTransfer = (Transfer) transferRepository.getTransfer(transferId, new Transfer());
        assertNotNull(createdTransfer);
        assertEquals("USD", createdTransfer.getCurrency().getCode());


        service.transfer(transferId);

        var checkFrom = checkRepository.getCheckByID(1L, new com.example.p2p.resourceServer.model.Check());
        var checkTo = checkRepository.getCheckByID(2L, new com.example.p2p.resourceServer.model.Check());


        BigInteger integerFrom = new BigInteger("89");
        BigInteger fractureFrom = new BigInteger("95");

        BigInteger integerTo = new BigInteger("110");
        BigInteger fractureTo = new BigInteger("45");

        assertEquals(integerFrom, checkFrom.getCurrency().getAmount_integer());
        assertEquals(fractureFrom, checkFrom.getCurrency().getAmount_fraction());

        assertEquals(integerTo, checkTo.getCurrency().getAmount_integer());
        assertEquals(fractureTo, checkTo.getCurrency().getAmount_fraction());

        assertThrows(Exception.class, () -> {
            transferRepository.getTransfer(transferId, new Transfer());
        });
    }
}
