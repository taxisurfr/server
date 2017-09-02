package com.taxisurfr.manager;

import com.taxisurfr.domain.Profile;
import com.taxisurfr.domain.SessionStat;
import com.taxisurfr.rest.js.NewSessionJS;
import com.taxisurfr.util.Mailer;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class StatManager extends AbstractDao<SessionStat> {
    private static final Logger logger = Logger.getLogger(StatManager.class.getName());
    public static String newline = System.getProperty("line.separator");

    @Inject
    private Mailer mailer;

    @Inject Ip2NationManager ip2NationManager;
    @Inject
    Ip2NationCountriesManager ip2NationManagerCountries;

    public StatManager() {
        super(SessionStat.class);
    }

    public SessionStat addSession(NewSessionJS newSessionJS) {
        SessionStat sessionStat = new SessionStat();
        sessionStat.setTime(LocalDateTime.now());
        sessionStat.setSrc(extractSrc(newSessionJS.url));
        sessionStat.setRouteKey(newSessionJS.link);
        sessionStat.setCountry(newSessionJS.country);
        persist(sessionStat);
        return sessionStat;
    }

    public SessionStat addSession() {
        SessionStat sessionStat = new SessionStat();
        sessionStat.setTime(LocalDateTime.now());
        persist(sessionStat);
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
                "<br> country=" + stat.getCountry() + "  type=" +stat.getRouteKey();

        }

        mailer.sendDaily(report,profile);
        deleteAll();
    }

    public String newSession(String userAgent, Long ipaddress, String route, String src) {
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
        sessionStat.setTime(LocalDateTime.now());
        persist(sessionStat);

        return country;

    }
    //    public void updateSessionStat(StatInfo statInfo)
    //    {
    //        SessionStat sessionStat = ofy().load().type(SessionStat.class).filter("ip", statInfo.getIp()).first().now();
    //        if (sessionStat != null)
    //        {
    //            switch (statInfo.getUpdate())
    //            {
    //                case TYPE:
    //                    sessionStat.setType(statInfo.getDetail());
    //                    break;
    //                case ROUTE:
    //                    sessionStat.setRoute(statInfo.getDetail());
    //                    break;
    //            }
    //            ofy().save().entity(sessionStat).now();
    //        }
    //        else
    //        {
    //            logger.log(Level.SEVERE, "not session found for ip " + statInfo.getIp());
    //        }
    //     }
    //

}
