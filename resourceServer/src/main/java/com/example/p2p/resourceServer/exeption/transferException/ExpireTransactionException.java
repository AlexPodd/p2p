package com.example.p2p.resourceServer.exeption.transferException;

import org.springframework.http.HttpStatus;

public class ExpireTransactionException extends TransferValidException {

    public ExpireTransactionException() {
        super("Transaction expired");
         status = HttpStatus.GONE;
    }
}
