package com.taxisurfr.manager;

import com.taxisurfr.domain.Currency;
import com.taxisurfr.domain.ExchangeRate;
import com.taxisurfr.domain.Profile;
import com.taxisurfr.domain.SessionStat;
import com.taxisurfr.rest.js.NewSessionJS;
import com.taxisurfr.util.Mailer;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Stateless
public class StatManager extends AbstractDao<SessionStat> {
    private static final Logger logger = Logger.getLogger(StatManager.class.getName());
    public static String newline = System.getProperty("line.separator");

    private static final Map<String,Currency> currencyMap = new HashMap<>();
    static {
        currencyMap.put("Australia",Currency.AUD);
        currencyMap.put("United States",Currency.USD);
        currencyMap.put("Sri Lanka",Currency.LKR);
        currencyMap.put("New Zealand",Currency.NZD);
        currencyMap.put("Japan",Currency.JPY);
        currencyMap.put("United Kingdom",Currency.GBP);
        currencyMap.put("Russia",Currency.RUB);

    }

    @Inject
    private Mailer mailer;

    @Inject
    ExchangeRateManager exchangeRateManager;

    @Inject Ip2NationManager ip2NationManager;
    @Inject
    Ip2NationCountriesManager ip2NationManagerCountries;

    public StatManager() {
        super(SessionStat.class);
    }

    public SessionStat addSession(NewSessionJS newSessionJS) {
        SessionStat sessionStat = new SessionStat();
        sessionStat.setSrc(extractSrc(newSessionJS.url));
        sessionStat.setRouteKey(newSessionJS.link);
        sessionStat.setCountry(newSessionJS.country);
        persist(sessionStat);
        return sessionStat;
    }

    public SessionStat addSession() {
        SessionStat sessionStat = new SessionStat();
/*
        sessionStat.setTime(LocalDateTime.now());
        persist(sessionStat);
*/
        return sessionStat;
    }

    private String extractSrc(String url) {
        String src = url != null ?
            url.contains("?src=") ? url.substring(url.indexOf("?src=") + 5) : null :
            null;
        return src;
    }

    public void sendDaily(Profile profile) {

        List<SessionStat> list = findAll();
        String report = "sessions:" + list.size();
        for (SessionStat stat : list) {
            report +=
                "<br> country=" + stat.getCountry() + "\t\troute=" +stat.getRoute()+ "\t\tsrc=" +stat.getSrc();

        }

        mailer.sendDaily(report,profile);
        deleteAll();
    }

    public Currency newSession(String userAgent, Long ipaddress, String src, String route) {
        String country = "XXX";
        SessionStat sessionStat = new SessionStat();
        sessionStat.setSrc(src);
        sessionStat.setRoute(route);
        sessionStat.setUserAgent(userAgent);
        List inet = getEntityManager().createNamedQuery("Ip2nation.get")
                .setParameter("INET_ATON", ipaddress)
                .getResultList();

        if (inet.size()!=0) {
            country = (String) inet.get(0);
        }else{
            if (ip2NationManager.findAll().size() == 0){
                ip2NationManager.setup();
                ip2NationManagerCountries.setup();
                country = "first";
            }
        }
        sessionStat.setCountry(country);
        persist(sessionStat);

        Currency currency = currencyMap.get(country);
        if (currency == null){
            currency = Currency.EUR;
        }
        setExchangeRate(currency);
        return currency;

    }
    private void setExchangeRate(Currency currency){
        ExchangeRate exchangeRateCurrency = exchangeRateManager.getByCurrency(currency);
        ExchangeRate exchangeRateLKR = exchangeRateManager.getByCurrency(Currency.LKR);
        Double euroRate = currency.equals(Currency.EUR) ? 1 : exchangeRateCurrency.getValue();
        Double lkrRate = euroRate / exchangeRateLKR.getValue();
        currency.exchangeRate = lkrRate;
    }
}
