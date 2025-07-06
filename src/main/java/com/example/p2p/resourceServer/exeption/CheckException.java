package com.example.p2p.resourceServer.exeption;

import org.springframework.http.HttpStatus;


public class CheckException extends RuntimeException{
    private final HttpStatus status;

    public CheckException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}

