package com.example.p2p.resourceServer.util.transferValidatorChain;

import com.example.p2p.resourceServer.dto.TransferDTO;
import com.example.p2p.resourceServer.exeption.transferException.NoEnoughCurrency;
import com.example.p2p.resourceServer.model.Check;
import com.example.p2p.resourceServer.repository.CheckRepository;
import jakarta.validation.ValidationException;

import java.math.BigInteger;

public class EnoughValueValid implements TransferValidator{

    @Override
    public void validate(TransferDTO transfer, CheckRepository repository) throws Exception {
        Check check = new Check();
        check = (Check) repository.getCheckByID(transfer.getIdFrom(), check);

        String[] sum = transfer.getSum().split("\\.");
        BigInteger integer =  new BigInteger(sum[0]);
        BigInteger fracture = new BigInteger(sum[1]);

        if(check.getCurrency().getAmount_integer().compareTo(integer) < 0){
            throw new NoEnoughCurrency();
        }
        else if(check.getCurrency().getAmount_integer().compareTo(integer) == 0){
            if(check.getCurrency().getAmount_fraction().compareTo(fracture) < 0){
                throw new NoEnoughCurrency();
            }
        }
    }
}
