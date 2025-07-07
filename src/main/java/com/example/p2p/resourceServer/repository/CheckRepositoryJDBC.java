package com.example.p2p.resourceServer.repository;

import com.example.p2p.authServer.repository.UserRowMapper;
import com.example.p2p.resourceServer.exeption.NoSuchCheckException;

import com.example.p2p.resourceServer.model.Check;
import com.example.p2p.resourceServer.model.CheckAbstract;
import com.example.p2p.resourceServer.model.currency.Currency;
import com.example.p2p.resourceServer.util.CurrencyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
        simpleJdbcInsert.execute(parameters);
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
        return jdbcTemplate.queryForObject(query, new CheckRowMapper<>(check.getClass(),currencyUtil), id);
    }

    @Override
    public String getNameByID(long id) {
            String query = "SELECT user_id FROM checks WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(query, String.class, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchCheckException();
        }
    }

    @Override
    public ArrayList<Long> getAllCheck(String username) {
        String query = "SELECT id FROM checks WHERE user_id = ?";
        return new ArrayList<>(jdbcTemplate.query(
                query,
                (rs, rowNum) -> rs.getLong("id"),
                username
        ));
    }

    @Override
    public void isExist(long id) {
        String sql = "SELECT COUNT(*) FROM checks WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        if (count == null || count == 0) {
            throw new NoSuchCheckException();
        }
    }

    @Override
    public void updateCheck(CheckAbstract check) {
        String sql = "UPDATE checks SET amount_integer = ?, amount_fraction = ? WHERE id = ?";
        int updated = jdbcTemplate.update(
                sql,
                check.getCurrency().getAmount_integer(),
                check.getCurrency().getAmount_fraction(),
                check.getId()
        );

        if (updated == 0) {
            throw new NoSuchCheckException();
        }
    }

    @Override
    public String getTypeByID(long id) {
        String query = "SELECT currency_code FROM checks WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(query, String.class, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchCheckException();
        }
    }


}
