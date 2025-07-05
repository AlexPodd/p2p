package com.example.p2p.resourceServer.controller;


import com.example.p2p.resourceServer.exeption.CheckException;
import com.example.p2p.resourceServer.exeption.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(CheckException.class)
    public ResponseEntity<Response> handleException(CheckException ex) {
        return new ResponseEntity<>(new Response(ex.getMessage()), ex.getStatus());
    }
}
