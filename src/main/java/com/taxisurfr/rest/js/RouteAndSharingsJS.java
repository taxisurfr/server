package com.taxisurfr.rest.js;

import com.taxisurfr.domain.Route;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;
import java.util.List;

@XmlRootElement
public class RouteAndSharingsJS {
    @XmlRootElement
    public static class Share {
        public int amountToShare;
        public LocalDate date;
        public String time;
        public long bookingId;
        public String dateText;
        public int orderType;
    }

    public Route route;
    public List<Share> sharingList;
    public String stripeKey;
    public boolean showNoRouteMessage;
}


