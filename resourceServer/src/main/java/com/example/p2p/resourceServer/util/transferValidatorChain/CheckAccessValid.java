package com.example.p2p.resourceServer.util.transferValidatorChain;

import com.example.p2p.resourceServer.dto.TransferDTO;
import com.example.p2p.resourceServer.exeption.NoAccessToCheckException;
import com.example.p2p.resourceServer.repository.CheckRepository;
import com.example.p2p.resourceServer.util.UserContext;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;

public class CheckAccessValid implements TransferValidator{

    @Autowired
    public UserContext context;
    @Override
    public void validate(TransferDTO transfer, CheckRepository repository) throws ValidationException {
       if(!repository.getNameByID(transfer.getIdFrom()).equals(context.getUserName())){
           throw new NoAccessToCheckException();
       }
    }
}
