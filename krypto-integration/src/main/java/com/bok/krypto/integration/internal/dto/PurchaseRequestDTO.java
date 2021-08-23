package com.bok.krypto.integration.internal.dto;

import java.math.BigDecimal;

public class PurchaseRequestDTO {
    public String symbol;
    public BigDecimal amount;
    public String currencyCode;
    public String cardToken;
}
