package com.example.p2p.resourceServer.exeption;

import org.springframework.http.HttpStatus;

public class InvalidCheckTypeException extends CheckException {
  public InvalidCheckTypeException() {
    super("Неверный тип чека!", HttpStatus.BAD_REQUEST);
  }
}
