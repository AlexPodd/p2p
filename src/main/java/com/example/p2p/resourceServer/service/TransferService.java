package com.example.p2p.resourceServer.service;

import com.example.p2p.resourceServer.dto.TransferDTO;
import jakarta.validation.ValidationException;

public interface TransferService {
    void transfer(long transferID) throws Exception;
    void cancelTransfer(long transferID);
    long initiate(TransferDTO transferDTO) throws Exception;
}
