package com.example.p2p.resourceServer.util.transferValidatorChain;

import com.example.p2p.resourceServer.dto.TransferDTO;
import com.example.p2p.resourceServer.exeption.transferException.InvalidCurrencyTypeToTransferException;
import com.example.p2p.resourceServer.repository.CheckRepository;

public class CheckCurrencyTypeValid implements TransferValidator{
    @Override
    public void validate(TransferDTO transfer, CheckRepository repository) throws Exception {
       if(!repository.getTypeByID(transfer.getIdFrom()).equals(repository.getTypeByID(transfer.getIdTo()))){

           throw new InvalidCurrencyTypeToTransferException();
       }
    }
}
