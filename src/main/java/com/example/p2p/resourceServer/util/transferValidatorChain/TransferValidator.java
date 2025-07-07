package com.example.p2p.resourceServer.util.transferValidatorChain;

import com.example.p2p.resourceServer.dto.TransferDTO;
import com.example.p2p.resourceServer.model.Check;
import com.example.p2p.resourceServer.repository.CheckRepository;
import jakarta.validation.ValidationException;

public interface TransferValidator {
    void validate(TransferDTO transfer, CheckRepository repository) throws Exception;
}
