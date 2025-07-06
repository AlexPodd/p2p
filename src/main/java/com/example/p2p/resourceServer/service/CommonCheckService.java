package com.example.p2p.resourceServer.service;

import com.example.p2p.authServer.repository.UserRowMapper;
import com.example.p2p.resourceServer.dto.CheckDTO;
import com.example.p2p.resourceServer.exeption.NoAccessToCheckException;
import com.example.p2p.resourceServer.model.Check;
import com.example.p2p.resourceServer.model.currency.Currency;
import com.example.p2p.resourceServer.repository.CheckRepository;
import com.example.p2p.resourceServer.util.CurrencyUtil;
import com.example.p2p.resourceServer.util.UserContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class CommonCheckService implements CheckService
{
    private final CheckRepository repository;
    private final CurrencyUtil currencyUtil;
    private final UserContext context;
    public CommonCheckService(CheckRepository repository, CurrencyUtil currencyUtil, UserContext context){
        this.repository = repository;
        this.currencyUtil = currencyUtil;
        this.context = context;
    }

    @Override
    public void createCheck(CheckDTO check) throws Exception{
        BigInteger amount_integer, amount_fraction;
        Currency currency = currencyUtil.typeToCurrency(check.getCurrencyType());
        String[] balance = check.getBalance().split("\\.");
        amount_integer = new BigInteger(balance[0]);

        String frac = balance[1].length() >= currency.getPrecision()? balance[1].substring(0, currency.getPrecision()): balance[1];
        amount_fraction = new BigInteger(frac);

        currency.setAmount_integer(amount_integer);
        currency.setAmount_fraction(amount_fraction);
        long id = new SecureRandom().nextLong() & Long.MAX_VALUE;
        repository.saveCheck(id , context.getUserName() , currency);
    }

    @Override
    public void closeCheck(long id) throws Exception {
        isUsersCheck(id);
        repository.deleteCheckByID(id);
    }

    @Override
    public String getBalance(long id) throws Exception {
        isUsersCheck(id);
        Check check = (Check) repository.getCheckByID(id, new Check());
        return check.getCurrency().getAmount_integer().toString()+"."+check.getCurrency().getAmount_fraction().toString();
    }

    @Override
    public void isUsersCheck(long checkID) {
        if(!repository.getNameByID(checkID).equals(context.getUserName())){
            throw new NoAccessToCheckException();
        }
    }

    @Override
    public ArrayList<Long> getAllCheck() {
        return repository.getAllCheck(context.getUserName());
    }

}
