package com.taxisurfr.manager;

import com.taxisurfr.domain.Currency;

import java.util.HashMap;
import java.util.Map;

public class ExchangeRateData {
    public static class Rates {
        Map<Currency,Double> values = new HashMap<>();

        public Double getGbp() {
            return values.get(Currency.GBP);
        }

        public void setGbp(Double gbp) {
            values.put(Currency.GBP,gbp);
        }

        public Double getUsd() {
            return values.get(Currency.USD);
        }

        public void setUsd(Double usd) {
            values.put(Currency.USD,usd);
        }

        public Double getLkr() {
            return values.get(Currency.LKR);
        }

        public void setLkr(Double value) {
            values.put(Currency.LKR,value);
        }

        public Double getAud() {
            return values.get(Currency.AUD);
        }

        public void setAud(Double value) {
            values.put(Currency.AUD,value);
        }

        //NZD
        public void setNzd(Double value) { values.put(Currency.NZD,value); }
        public Double getNzd() { return values.get(Currency.NZD); }

        //RUB
        public void setRub(Double value) { values.put(Currency.RUB,value); }
        public Double getRub() { return values.get(Currency.RUB); }

        //JPY
        public void setJpy(Double value) { values.put(Currency.JPY,value); }
        public Double getJpy() { return values.get(Currency.JPY); }

        public Double get(Currency currency){
            return values.get(currency);
        }

        public void setValues(Map<String, Double> values) {
            Map<String, Double> xxx = values;
            System.out.println(xxx);
        }
    }

    private String date;
    private Rates rates;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Rates getRates() {
        return rates;
    }

    public void setRates(Rates rates) {
        this.rates = rates;
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "date='" + date + '\'' +
                ", rates=" + rates +
                '}';
    }
}
