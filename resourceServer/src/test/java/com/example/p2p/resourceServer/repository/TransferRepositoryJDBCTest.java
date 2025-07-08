package com.example.p2p.resourceServer.repository;

import com.example.p2p.resourceServer.exeption.NoSuchCheckException;
import com.example.p2p.resourceServer.exeption.transferException.NoSuchTransferException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@ActiveProfiles("resourceTest")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransferRepositoryJDBCTest {

    @Autowired
    private TransferRepositoryJDBC repository;

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
    void transferDeleteNoTransfer(){
        assertThrows(NoSuchTransferException.class, () -> {
            repository.deleteTransfer(-1);
        });
    }

    @Test
    void transferGetNoTransfer(){
        assertThrows(NoSuchTransferException.class, () -> {
            repository.getTransferFromId(-1);
        });
    }



}
