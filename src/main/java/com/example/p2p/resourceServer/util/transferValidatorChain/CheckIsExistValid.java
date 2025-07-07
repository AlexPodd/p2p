package com.example.p2p.resourceServer.util.transferValidatorChain;

import com.example.p2p.resourceServer.dto.TransferDTO;
import com.example.p2p.resourceServer.model.Check;
import com.example.p2p.resourceServer.repository.CheckRepository;
import jakarta.validation.ValidationException;

public class CheckIsExistValid implements TransferValidator{
    //Сам по себе метод репозитория вернет ошибку если нет чека
    @Override
    public void validate(TransferDTO transfer, CheckRepository repository) throws Exception {
        repository.isExist(transfer.getIdTo());
        repository.isExist(transfer.getIdFrom());
    }
}
