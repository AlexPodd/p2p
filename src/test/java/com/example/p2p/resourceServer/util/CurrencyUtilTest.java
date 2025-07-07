package com.example.p2p.resourceServer.util;


import com.example.p2p.resourceServer.exeption.InvalidCheckTypeException;
import com.example.p2p.resourceServer.exeption.NoSuchCheckException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class CurrencyUtilTest {

    @Mock
    private CurrencyProperties currencyProperties;

    @InjectMocks
    private CurrencyUtil util;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInvalidTypeCurrency() {
        when(currencyProperties.getCurrency("invalid")).thenReturn(null);
        assertThrows(InvalidCheckTypeException.class, () -> {
            util.typeToCurrency("invalid");
        });
    }
}