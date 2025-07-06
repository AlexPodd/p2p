package com.example.p2p.resourceServer.controller;


import com.example.p2p.resourceServer.exeption.CheckException;
import com.example.p2p.resourceServer.exeption.Response;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(CheckException.class)
    public ResponseEntity<Response> handleException(CheckException ex) {
        return new ResponseEntity<>(new Response(ex.getMessage()), ex.getStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleValidationException(ConstraintViolationException ex) {
        return ResponseEntity
                .badRequest()
                .body(Map.of("error", "Validation failed", "details", ex.getMessage()));
    }

    @ExceptionHandler(org.springframework.dao.EmptyResultDataAccessException.class)
    public ResponseEntity<?> handleEmptyResult(EmptyResultDataAccessException ex) {
        return ResponseEntity
                .status(404)
                .body(Map.of("error", "Resource not found", "details", ex.getMessage()));
    }

    @ExceptionHandler(org.springframework.dao.DataAccessException.class)
    public ResponseEntity<?> handleDatabaseError(DataAccessException ex) {
        return ResponseEntity
                .internalServerError()
                .body(Map.of("error", "Database error", "details", ex.getMessage()));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNullPointer(NullPointerException ex) {
        return ResponseEntity
                .internalServerError()
                .body(Map.of("error", "Null pointer exception", "details", ex.getMessage()));
    }

}
