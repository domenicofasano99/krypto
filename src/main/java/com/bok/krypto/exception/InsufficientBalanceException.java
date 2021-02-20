package com.bok.krypto.exception;

public class InsufficientBalanceException extends RuntimeException{
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
