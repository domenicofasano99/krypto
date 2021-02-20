package com.bok.krypto.exception;

public class WalletNotFoundException extends RuntimeException {
    public WalletNotFoundException(String message) {
        super(message);
    }
}
