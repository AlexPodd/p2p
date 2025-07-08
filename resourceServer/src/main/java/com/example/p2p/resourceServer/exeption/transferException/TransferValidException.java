package com.example.p2p.resourceServer.exeption.transferException;

import org.springframework.http.HttpStatus;

public class TransferValidException extends RuntimeException {
    protected HttpStatus status = HttpStatus.BAD_REQUEST;

    public TransferValidException(String message) {
        super(message);
    }

    public HttpStatus getStatus() {
        return status;
    }
}
