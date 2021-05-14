package com.bok.krypto.integration.internal.dto;

import java.math.BigDecimal;

public class BankAccountBalance {
    public Long accountId;
    public BigDecimal balance;

    public BankAccountBalance(Long accountId, BigDecimal balance) {
        this.accountId = accountId;
        this.balance = balance;
    }
}
