package com.taxisurfr.domain;

import java.io.Serializable;

public enum Currency implements Serializable
{
    EUR("EUR €"),
    USD("$US"),
    AUD("$AU"),
    GBP("GBP £"),
    LKR("LKR ₨"),
    NZD("NZD $NZ"),
    RUB("RUB RUB"),
    JPY("JPY JPY");

    public String symbol;
    public Double exchangeRate;

    Currency(String sym)
    {
        symbol = sym;
    }
}
