package com.taxisurfr.domain;

import java.io.Serializable;

public enum Currency implements Serializable
{
    EUR("EUR €"),
    USD("$US"),
    AUD("$AU"),
    GBP("GBP £"),
    LKR("LKR ₨");
    public String symbol;
    public Double exchangeRate;

    Currency(String sym)
    {
        symbol = sym;
    }
}
