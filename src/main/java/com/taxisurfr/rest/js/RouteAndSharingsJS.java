package com.taxisurfr.rest.js;

import com.taxisurfr.domain.Currency;
import com.taxisurfr.domain.Hotel;
import com.taxisurfr.domain.Price;

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

    public Hotel hotel;
    public List<Price> prices;
    public List<Share> sharingList;
    public List<Hotel> hotels;
    public String stripeKey;
    public boolean showNoRouteMessage;
    public Currency currency;
    public Double exchangeRate;
}


