package com.example.p2p.resourceServer.exeption.transferException;

public class NoEnoughCurrency extends TransferValidException {
    public NoEnoughCurrency() {
        super("No enough currency");
    }
}
