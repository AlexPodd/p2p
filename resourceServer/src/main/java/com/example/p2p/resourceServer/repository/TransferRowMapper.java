package com.example.p2p.resourceServer.repository;

import com.example.p2p.resourceServer.model.TransferAbstract;
import com.example.p2p.resourceServer.model.currency.Currency;
import com.example.p2p.resourceServer.util.CurrencyUtil;


import com.example.p2p.resourceServer.model.CheckAbstract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransferRowMapper<T extends TransferAbstract> implements RowMapper<T> {

    private final Class<T> clazz;

    private final CurrencyUtil currencyUtil;

    public TransferRowMapper(Class<T> clazz, CurrencyUtil currencyUtil) {
        this.clazz = clazz;
        this.currencyUtil = currencyUtil;
    }

    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            T transfer = clazz.getDeclaredConstructor().newInstance();

            transfer.setId(rs.getLong("id"));
            transfer.setCheckFromId(rs.getLong("id_from"));
            transfer.setCheckToId(rs.getLong("id_to"));
            transfer.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            Currency currency = currencyUtil.typeToCurrency(rs.getString("currency_code"));
            currency.setAmount_fraction(new BigInteger(rs.getString("amount_fraction")));
            currency.setAmount_integer(new BigInteger(rs.getString("amount_integer")));
            transfer.setCurrency(currency);

            return transfer;
        } catch (Exception e) {
            throw new SQLException("Failed to map row to " + clazz.getSimpleName(), e);
        }
    }
}
