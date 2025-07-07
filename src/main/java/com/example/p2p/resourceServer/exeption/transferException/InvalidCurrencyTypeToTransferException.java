package com.example.p2p.resourceServer.exeption.transferException;

public class InvalidCurrencyTypeToTransferException extends TransferValidException {
    public InvalidCurrencyTypeToTransferException() {
        super("currency cannot be converted");
    }
}
