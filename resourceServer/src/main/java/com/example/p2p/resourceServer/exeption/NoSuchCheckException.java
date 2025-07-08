package com.example.p2p.resourceServer.exeption;

import org.springframework.http.HttpStatus;

public class NoSuchCheckException extends CheckException {
    public NoSuchCheckException() {
        super("Счет с таким номером не обнаружен!", HttpStatus.NOT_FOUND);
    }
}
