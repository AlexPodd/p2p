package com.example.p2p.resourceServer.repository;

import com.example.p2p.resourceServer.dto.TransferDTO;
import com.example.p2p.resourceServer.model.TransferAbstract;

import java.math.BigInteger;

public interface TransferRepository {
    void deleteTransfer(long transferID);
    void createTransfer(long id, TransferDTO transferDTO, BigInteger integer, BigInteger fracture, String currencyType);
    TransferAbstract getTransfer(long transferID, TransferAbstract transferAbstract);
    long getTransferFromId(long transferID);

}
