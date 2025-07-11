package com.example.p2p.resourceServer.service;

import com.example.p2p.resourceServer.dto.CheckDTO;
import com.example.p2p.resourceServer.exeption.InvalidCheckTypeException;
import com.example.p2p.resourceServer.exeption.NoAccessToCheckException;
import com.example.p2p.resourceServer.exeption.NoSuchCheckException;
import com.example.p2p.resourceServer.model.Check;
import com.example.p2p.resourceServer.model.currency.Currency;
import com.example.p2p.resourceServer.repository.CheckRepository;
import com.example.p2p.resourceServer.util.CurrencyUtil;
import com.example.p2p.resourceServer.util.UserContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)

public class CommonCheckServiceTest {
    @Mock
    private CheckRepository repository;

    @InjectMocks
    private CommonCheckService service;

    @Mock
    private CurrencyUtil currencyUtil;

    @Mock
    private UserContext userContext;

    @Test
    void testCreateCheckSuccess() throws Exception {
        CheckDTO dto = new CheckDTO("USD", "123.456");

        Currency mockCurrency = new Currency("USD", 2);
        when(currencyUtil.typeToCurrency("USD")).thenReturn(mockCurrency);

        when(userContext.getUserName()).thenReturn("testUser");
        doNothing().when(repository).saveCheck(anyLong(), eq("testUser"), any());

        service.createCheck(dto);

        ArgumentCaptor<Currency> currencyCaptor = ArgumentCaptor.forClass(Currency.class);
        verify(repository).saveCheck(anyLong(), eq("testUser"), currencyCaptor.capture());

        Currency captured = currencyCaptor.getValue();
        assertEquals(new BigInteger("123"), captured.getAmount_integer());
        assertEquals(new BigInteger("45"), captured.getAmount_fraction());
    }

    @Test
    void testCreateCheckSuccessNoTrim() throws Exception {
        // Arrange
        CheckDTO dto = new CheckDTO("BTC", "123.121235");

        Currency mockCurrency = new Currency("BTC", 100);
        when(currencyUtil.typeToCurrency("BTC")).thenReturn(mockCurrency);

        when(userContext.getUserName()).thenReturn("testUser");
        doNothing().when(repository).saveCheck(anyLong(), eq("testUser"), any());

        service.createCheck(dto);

        ArgumentCaptor<Currency> currencyCaptor = ArgumentCaptor.forClass(Currency.class);
        verify(repository).saveCheck(anyLong(), eq("testUser"), currencyCaptor.capture());

        Currency captured = currencyCaptor.getValue();
        assertEquals(new BigInteger("123"), captured.getAmount_integer());
        assertEquals(new BigInteger("121235"), captured.getAmount_fraction());
    }

    @Test
    void testCreateCheckInvalidType() throws Exception {
        CheckDTO dto = new CheckDTO("US", "123.456");
        when(currencyUtil.typeToCurrency("US")).thenThrow(InvalidCheckTypeException.class);
        assertThrows(InvalidCheckTypeException.class, () -> {
            service.createCheck(dto);
        });
    }

    @Test
    void testCloseCheckSuccess() throws Exception {
        long checkId = 1;

        when(userContext.getUserName()).thenReturn("testUser");
        when(repository.getNameByID(checkId)).thenReturn("testUser");
        doNothing().when(repository).deleteCheckByID(checkId);

        service.closeCheck(checkId);

        verify(repository).getNameByID(checkId);
        verify(repository).deleteCheckByID(checkId);
    }

    @Test
    void testCloseCheckInvalidUser() throws Exception {
        long checkId = 1;

        when(userContext.getUserName()).thenReturn("testUser");
        when(repository.getNameByID(checkId)).thenReturn("testUser1");

        assertThrows(NoAccessToCheckException.class, () -> {
            service.closeCheck(checkId);
        });
    }

    @Test
    void testCloseCheckInvalidCheck() throws Exception {
        long checkId = 1;

        when(userContext.getUserName()).thenReturn("testUser");
        when(repository.getNameByID(checkId)).thenReturn("testUser");

        doThrow(new NoSuchCheckException()).when(repository).deleteCheckByID(checkId);

            assertThrows(NoSuchCheckException.class, () -> {
                service.closeCheck(checkId);
            });
    }


    @Test
    void testGetAllCheckSuccess() throws Exception {
        ArrayList<Long> checkId = new ArrayList<>(List.of(10L,12L,15L));
        when(repository.getAllCheck(any())).thenReturn(checkId);

        ArrayList<Long> serviceAnswer = service.getAllCheck();
        assertEquals(checkId, serviceAnswer);
    }

    @Test
    void testGetAllCheckSuccessEmpty() throws Exception {
        ArrayList<Long> checkId = new ArrayList<>();
        when(repository.getAllCheck(any())).thenReturn(checkId);

        ArrayList<Long> serviceAnswer = service.getAllCheck();

        assertEquals(checkId, serviceAnswer);
    }


    @Test
    void testBalanceCheckSuccess() throws Exception {
        Long id = 10L;
        Check mockCheck = new Check();
        Currency mockCurrency = new Currency("USD", 2);
        mockCurrency.setAmount_integer(new BigInteger("100"));
        mockCurrency.setAmount_fraction(new BigInteger("10"));

        when(userContext.getUserName()).thenReturn("testUser");
        when(repository.getNameByID(id)).thenReturn("testUser");

        mockCheck.setCurrency(mockCurrency);

        when(repository.getCheckByID(eq(id), any(Check.class))).thenReturn(mockCheck);

        String balance = service.getBalance(id);
        assertEquals("100.10", balance);
    }


    @Test
    void testBalanceCheckWrongUser() throws Exception {
        Long id = 10L;
        when(userContext.getUserName()).thenReturn("testUser");
        when(repository.getNameByID(id)).thenReturn("testUser1");
        assertThrows(NoAccessToCheckException.class, () -> {
            service.getBalance(id);
        });
    }


    @Test
    void testBalanceNoSuchCheck() throws Exception {
        Long id = 10L;
        when(userContext.getUserName()).thenReturn("testUser");
        when(repository.getNameByID(id)).thenReturn("testUser");

        doThrow(new NoSuchCheckException()).when(repository).getCheckByID(eq(id), any());

        assertThrows(NoSuchCheckException.class, () -> {
            service.getBalance(id);
        });
    }
}
