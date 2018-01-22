package com.taxisurfr.rest.js;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
public class NewBookingJS {
    public Long routeId;
    public String name;
    public Date date;
    public String email;
    public String dateText;
    public String flightNo;
    public String landingTime;
    public int pax;
    public int surfboards;
    public String requirements;
    public Boolean shareWanted;
    public Boolean announceShare;
    public Long priceId;
}
