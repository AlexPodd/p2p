package com.example.p2p.resourceServer.repository;

import com.example.p2p.authServer.repository.UserRowMapper;
import com.example.p2p.resourceServer.exeption.NoSuchCheckException;

import com.example.p2p.resourceServer.model.Check;
import com.example.p2p.resourceServer.model.CheckAbstract;
import com.example.p2p.resourceServer.model.currency.Currency;
import com.example.p2p.resourceServer.util.CurrencyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@Repository
public class CheckRepositoryJDBC implements CheckRepository{

    private final JdbcTemplate jdbcTemplate;
    private final CurrencyUtil currencyUtil;

    @Autowired
    public CheckRepositoryJDBC(JdbcTemplate jdbcTemplate, CurrencyUtil currencyUtil) {
        this.jdbcTemplate = jdbcTemplate;
        this.currencyUtil = currencyUtil;
    }


    @Override
    public void saveCheck(long id, String username, Currency currency) throws Exception {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("checks").usingColumns("id", "user_id", "currency_code", "amount_integer", "amount_fraction");;
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("id", id);
        parameters.put("user_id", username);
        parameters.put("currency_code", currency.getCode());
        parameters.put("amount_integer", currency.getAmount_integer());
        parameters.put("amount_fraction", currency.getAmount_fraction());
        int rowsAffected = simpleJdbcInsert.execute(parameters);
        if(rowsAffected != 1){
            throw new Exception("123");
        }
    }

    @Override
    public void deleteCheckByID(long id) throws Exception {
        String sql = "DELETE FROM checks WHERE id = ?";
        int deleted = jdbcTemplate.update(sql, id);
        if(deleted == 0){
            throw new NoSuchCheckException();
        }
    }

    @Override
    public CheckAbstract getCheckByID(long id, CheckAbstract check) {
        String query = "SELECT * FROM checks WHERE id = ?";
        CheckAbstract checkAbstract = jdbcTemplate.queryForObject(query, new CheckRowMapper<>(check.getClass(),currencyUtil), id);
        if(checkAbstract == null){
            throw new NoSuchCheckException();
        }
        return checkAbstract;
    }

    @Override
    public String getNameByID(long id) {
            String query = "SELECT user_id FROM checks WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(query, String.class, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchCheckException();
        }}

    @Override
    public ArrayList<Long> getAllCheck(String username) {
        String query = "SELECT id FROM checks WHERE user_id = ?";
        return new ArrayList<>(jdbcTemplate.query(
                query,
                (rs, rowNum) -> rs.getLong("id"),
                username
        ));
    }


}
