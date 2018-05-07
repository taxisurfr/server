package com.taxisurfr.rest.js;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PriceJS {
    public boolean newPrice;
    public Long startrouteId;
    public Long endrouteId;
    public Long cents;
    public Long contractorId;
    public Long id;
}


