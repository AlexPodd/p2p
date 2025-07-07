package com.example.p2p.resourceServer.repository;


import com.example.p2p.resourceServer.exeption.InvalidCheckTypeException;
import com.example.p2p.resourceServer.exeption.NoSuchCheckException;
import com.example.p2p.resourceServer.model.Check;
import com.example.p2p.resourceServer.model.currency.Currency;
import com.example.p2p.resourceServer.service.CommonCheckService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DataFormatException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("resourceTest")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CheckRepositoryJDBCTest {


    @Autowired
    private CheckRepositoryJDBC repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM checks");
        jdbcTemplate.execute("INSERT INTO checks (id, user_id, currency_code, amount_integer, amount_fraction) VALUES (1, 'testUser', 'USD', 100, 20)");
    }

    @Test
    void testGetNameByID() {
        String name = repository.getNameByID(1L);
        assertEquals("testUser", name);
    }

    @Test
    void testSaveCheckSuccess() throws Exception{
        jdbcTemplate.execute("DELETE FROM checks");

        Currency currency = new Currency("USD", 2);
        currency.setAmount_integer(new BigInteger("200"));
        currency.setAmount_fraction(new BigInteger("50"));



        repository.saveCheck(2L, "newUser", currency);

        Check savedCheck = (Check) repository.getCheckByID(2L, new Check());

        assertCheckEquals(
                savedCheck,
                2L,
                "newUser",
                "USD",
                new BigInteger("200"),
                new BigInteger("50")
        );
    }


    @Test
    void testSaveCheckError() throws Exception{
        Currency currency = new Currency("USD", 2);

        assertThrows(DataAccessException.class, () -> {
            repository.saveCheck(1L, "newUser", currency);
        });
    }

    @Test
    void testCloseCheckError() throws Exception{
        assertThrows(NoSuchCheckException.class, () -> {
            repository.deleteCheckByID(2L);
        });
    }

    @Test
    void testCloseCheckSuccess() throws Exception{
            repository.deleteCheckByID(1L);
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM checks WHERE id = ?",
                new Object[]{1L},
                Integer.class
        );
        assertEquals(0, count);
    }

    @Test
    void testGetCheckErrorNoSuchCheck() throws Exception{
        assertThrows(DataAccessException.class, () -> {
            repository.getCheckByID(2L, new Check());
        });
    }

    @Test
    void testGetCheckSuccess() throws Exception{
        long id = 1L;
        Check check;
       check = (Check) repository.getCheckByID(id, new Check());

        assertCheckEquals(
                check,
                1L,
                "testUser",
                "USD",
                new BigInteger("100"),
                new BigInteger("20")
        );
    }


    @Test
    void testGetNameByIDSuccess() throws Exception{
        long id = 1;
        String name;
        name = repository.getNameByID(id);
        assertEquals(name, "testUser");
    }

    @Test
    void testGetNameByIDNoSuchCheck() throws Exception{
        assertThrows(NoSuchCheckException.class, () -> {
            repository.getNameByID(2L);
        });
    }

    @Test
    void testGetAllCheckReturnsIds() throws Exception {
        jdbcTemplate.execute("DELETE FROM checks");

        jdbcTemplate.execute("INSERT INTO checks (id, user_id, currency_code, amount_integer, amount_fraction) VALUES (10, 'user1', 'USD', 100, 0)");
        jdbcTemplate.execute("INSERT INTO checks (id, user_id, currency_code, amount_integer, amount_fraction) VALUES (20, 'user1', 'USD', 200, 0)");
        jdbcTemplate.execute("INSERT INTO checks (id, user_id, currency_code, amount_integer, amount_fraction) VALUES (30, 'user1', 'USD', 300, 0)");

        ArrayList<Long> ids = repository.getAllCheck("user1");

        assertEquals(3, ids.size());
        assertTrue(ids.contains(10L));
        assertTrue(ids.contains(20L));
        assertTrue(ids.contains(30L));
    }

    @Test
    void testGetAllCheckEmptyResult() {
        jdbcTemplate.execute("DELETE FROM checks");
        ArrayList<Long> ids = repository.getAllCheck("nonexistent_user");
        assertNotNull(ids);
        assertTrue(ids.isEmpty());
    }

    @Test
    void testIsExistSuccess(){
        assertDoesNotThrow(() -> repository.isExist(1L));
    }

    @Test
    void testIsExistNoCheck(){
        assertThrows(NoSuchCheckException.class, () -> {
            repository.getNameByID(2L);
        });
    }

    @Test
    void testUpdateCheckSuccess() {
        Currency currency = new Currency("USD", 2);
        currency.setAmount_integer(new BigInteger("500"));
        currency.setAmount_fraction(new BigInteger("75"));

        Check check = new Check();
        check.setId(1L);
        check.setUserID("testUser");
        check.setCurrency(currency);

        repository.updateCheck(check);

        Check updatedCheck = (Check) repository.getCheckByID(1L, new Check());

        assertEquals(new BigInteger("500"), updatedCheck.getCurrency().getAmount_integer());
        assertEquals(new BigInteger("75"), updatedCheck.getCurrency().getAmount_fraction());
    }

    @Test
    void testUpdateCheckFails_NoSuchCheck() {
        Currency currency = new Currency("USD", 2);
        currency.setAmount_integer(new BigInteger("123"));
        currency.setAmount_fraction(new BigInteger("456"));

        Check check = new Check();
        check.setId(999L);
        check.setUserID("user");
        check.setCurrency(currency);

        assertThrows(NoSuchCheckException.class, () -> repository.updateCheck(check));
    }


    private void assertCheckEquals(Check actual, long expectedId, String expectedUserId, String expectedCurrencyCode, BigInteger expectedAmountInt, BigInteger expectedAmountFrac) {
        assertEquals(expectedId, actual.getId());
        assertEquals(expectedUserId, actual.getUserID());
        assertEquals(expectedCurrencyCode, actual.getCurrency().getCode());
        assertEquals(expectedAmountInt, actual.getCurrency().getAmount_integer());
        assertEquals(expectedAmountFrac, actual.getCurrency().getAmount_fraction());
    }
}
