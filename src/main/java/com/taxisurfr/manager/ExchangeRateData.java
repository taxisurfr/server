package com.taxisurfr.manager;

import com.taxisurfr.domain.Currency;

import javax.ws.rs.core.Response;

public class ExchangeRateData {
    public static class Rates {
        Double usd;
        Double lkr;
        Double aud;
        Double gbp;

        public Double getGbp() {
            return gbp;
        }

        public void setGbp(Double gbp) {
            this.gbp = gbp;
        }

        public Double getUsd() {
            return usd;
        }

        public void setUsd(Double usd) {
            this.usd = usd;
        }

        public Double getLkr() {
            return lkr;
        }

        public void setLkr(Double lkr) {
            this.lkr = lkr;
        }

        public Double getAud() {
            return aud;
        }

        public void setAud(Double aud) {
            this.aud = aud;
        }

        @Override
        public String toString() {
            return "Rates{" +
                    "usd=" + usd +
                    ", lkr=" + lkr +
                    ", aud=" + aud +
                    '}';
        }

        public Double get(Currency currency) {
            switch (currency){
                case EUR:
                    return Double.valueOf(1);
                case USD:
                    return getUsd();
                case AUD:
                    return getAud();
                case GBP:
                    return getGbp();
                case LKR:
                    return getLkr();
            }
            return null;
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
