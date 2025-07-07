package com.example.p2p.resourceServer.exeption.transferException;

public class NoSuchTransferException extends TransferValidException {
    public NoSuchTransferException() {
        super("No such transfer");
    }
}
