package com.taxisurfr.manager;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxisurfr.domain.Currency;
import com.taxisurfr.domain.ExchangeRate;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class ExchangeRateManager extends AbstractDao<ExchangeRate>{
    @Inject
    Logger logger;

    public ExchangeRateManager() {
        super(ExchangeRate.class);
    }
    //private static String URL = "http://data.fixer.io/api/latest?access_key=cddb1101f41431100b2d9977b318588c&symbols=USD,LKR,AUD,GBP&format=1";
    private static String URL = "http://data.fixer.io/api/latest?access_key=cddb1101f41431100b2d9977b318588c&symbols=%s&format=1";

    public void dailyUpdate() {

        Client client = ClientBuilder.newClient();
        String currencyList = "";
        for (Currency currency : Currency.values()){
            currencyList += ","+currency.toString();
        }
        currencyList = currencyList.substring(1,currencyList.length());
        URL = String.format(URL,currencyList);
        WebTarget userTarget = client.target(URL);
        Response response = userTarget
                .request("application/json").get();
        String json = response.readEntity(String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        ExchangeRateData exchangeRateData = null;
        try {
            exchangeRateData = objectMapper.readValue(json.toLowerCase(), ExchangeRateData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Currency currency: Currency.values()) {
            save(currency, exchangeRateData.getRates());
        }
    }

    private void save(Currency currency, ExchangeRateData.Rates rates ){
      Double value = rates.get(currency);
        List<ExchangeRate> currency1ist = getEntityManager().createNamedQuery("ExchangeRate.getByCurrency").setParameter("currency", currency).getResultList();
        ExchangeRate exchangeRate = null;
        if (currency1ist.size() == 0){
            exchangeRate = new ExchangeRate();
        }else {
            exchangeRate = currency1ist.get(0);
        }
        exchangeRate.setCurrency(currency);
        exchangeRate.setValue(value);
        getEntityManager().persist(exchangeRate);
    }

    public ExchangeRate getByCurrency(Currency currency){
        if (currency == Currency.EUR) {
            ExchangeRate exchangeRate = new ExchangeRate();
            exchangeRate.setValue(1D);
            return exchangeRate;
        }
        return (ExchangeRate) getEntityManager().createNamedQuery("ExchangeRate.getByCurrency").setParameter("currency", currency).getSingleResult();
    }
}

