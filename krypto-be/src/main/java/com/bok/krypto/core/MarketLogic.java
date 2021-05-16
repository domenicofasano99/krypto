package com.bok.krypto.core;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MarketLogic {

    /**
     * value intended in US dollars
     *
     * @param actualPrice of the krypto currency
     * @param amount      of the krypto currency
     * @return the value in US dollars
     */
    public BigDecimal value(BigDecimal actualPrice, BigDecimal amount) {
        return actualPrice.multiply(amount);
    }


    /**
     * value intended in US dollars
     *
     * @param purchaseValue of the krypto currency
     * @param actualValue   of the krypto currency
     * @return the value in US dollars of the profit (may be positive or negative to indicate losses)
     */
    public BigDecimal profit(BigDecimal purchaseValue, BigDecimal actualValue) {
        return actualValue.subtract(purchaseValue);
    }

    public BigDecimal transactionFee(BigDecimal amount) {
        return percentage(amount, Constants.TRANSACTION_FEE_PERCENT);
    }

    public BigDecimal transferFee(BigDecimal amount) {
        return percentage(amount, Constants.TRANSFER_FEE_PERCENT);
    }

    /**
     * @param base amount
     * @param pct  percentage
     * @return the percentage amount
     */
    public BigDecimal percentage(BigDecimal base, BigDecimal pct) {
        return base.multiply(pct).scaleByPowerOfTen(-2);
    }


}
