package com.example.p2p.resourceServer.repository;

import com.example.p2p.resourceServer.model.CheckAbstract;
import com.example.p2p.resourceServer.model.currency.Currency;
import com.example.p2p.resourceServer.util.CurrencyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckRowMapper<T extends CheckAbstract> implements RowMapper<T> {

    private final Class<T> clazz;


    private final CurrencyUtil currencyUtil;


    public CheckRowMapper(Class<T> clazz, CurrencyUtil currencyUtil) {
        this.clazz = clazz;
        this.currencyUtil = currencyUtil;
    }

    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            T check = clazz.getDeclaredConstructor().newInstance();
            check.setId(rs.getLong("id"));
            check.setUserID(rs.getString("user_id"));
            Currency currency = currencyUtil.typeToCurrency(rs.getString("currency_code"));
            currency.setAmount_fraction(new BigInteger(rs.getString("amount_fraction")));
            currency.setAmount_integer(new BigInteger(rs.getString("amount_integer")));
            check.setCurrency(currency);
            return check;
        } catch (Exception e) {
            throw new SQLException("Failed to map row to " + clazz.getSimpleName(), e);
        }
    }
}
