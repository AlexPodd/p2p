package com.example.p2p.resourceServer.exeption;

import org.springframework.http.HttpStatus;

public class NoAccessToCheckException extends CheckException {
    public NoAccessToCheckException() {
        super("Нет доступа к счету!", HttpStatus.FORBIDDEN);
    }
}
