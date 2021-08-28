package com.bok.krypto.util;

import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Currency;

public class Constants {
    public static final Currency STANDARD_CURRENCY = Currency.getInstance("USD");

    public static final MathContext mathContext = new MathContext(10, RoundingMode.HALF_UP);
}
