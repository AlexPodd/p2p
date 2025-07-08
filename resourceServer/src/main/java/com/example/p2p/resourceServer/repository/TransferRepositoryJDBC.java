package com.example.p2p.resourceServer.repository;

import com.example.p2p.resourceServer.dto.TransferDTO;
import com.example.p2p.resourceServer.exeption.NoSuchCheckException;
import com.example.p2p.resourceServer.exeption.transferException.NoSuchTransferException;
import com.example.p2p.resourceServer.model.TransferAbstract;
import com.example.p2p.resourceServer.util.CurrencyUtil;
import jdk.jfr.Registered;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;


@Repository
public class TransferRepositoryJDBC implements TransferRepository{
    private final JdbcTemplate jdbcTemplate;
    private final CurrencyUtil currencyUtil;

    @Autowired
    public TransferRepositoryJDBC(JdbcTemplate jdbcTemplate, CurrencyUtil currencyUtil) {
        this.jdbcTemplate = jdbcTemplate;
        this.currencyUtil = currencyUtil;
    }

    @Override
    public void deleteTransfer(long transferID) {
        String sql = "DELETE FROM transfers WHERE id = ?";
        int deleted = jdbcTemplate.update(sql, transferID);
        if(deleted == 0){
            throw new NoSuchTransferException();
        }
    }


    @Override
    public void createTransfer(long id, TransferDTO transferDTO, BigInteger integer, BigInteger fracture, String currencyType) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("transfers").usingColumns("id", "id_from", "id_to", "amount_integer", "amount_fraction","currency_code");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("id", id);
        parameters.put("id_from", transferDTO.getIdFrom());
        parameters.put("id_to", transferDTO.getIdTo());
        parameters.put("amount_integer", integer);
        parameters.put("amount_fraction", fracture);
        parameters.put("currency_code", currencyType);
        simpleJdbcInsert.execute(parameters);

    }

    @Override
    public TransferAbstract getTransfer(long transferID, TransferAbstract transferAbstract) {
        String query = "SELECT * FROM transfers WHERE id = ?";
        return jdbcTemplate.queryForObject(query, new TransferRowMapper<>(transferAbstract.getClass(), currencyUtil), transferID);
    }



    @Override
    public long getTransferFromId(long transferID){
        String query = "SELECT id_from FROM transfers WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(query, Long.class, transferID);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchTransferException();
        }
    }
}
