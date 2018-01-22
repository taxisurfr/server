package com.taxisurfr.rest.js;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PriceJS {
    public Long id;
    public String startroute;
    public String endroute;
    public Long cents;
    public Long contractorId;
    public Long routeId;
}


