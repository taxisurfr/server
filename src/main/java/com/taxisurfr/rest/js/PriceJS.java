package com.taxisurfr.rest.js;

import com.taxisurfr.domain.Location;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PriceJS {
    public boolean newPrice;
    public Location startroute;
    public Location endroute;
    public Long cents;
    public Long contractorId;
    public Long newcontractorId;
    public Long id;
}


