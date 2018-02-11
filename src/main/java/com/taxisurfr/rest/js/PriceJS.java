package com.taxisurfr.rest.js;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PriceJS {
    public boolean newPrice;
    public String startroute;
    public String endroute;
    public Long cents;
    public Long contractorId;
    public Long routeId;
}


