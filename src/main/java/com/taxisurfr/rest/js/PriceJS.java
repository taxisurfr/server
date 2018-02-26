package com.taxisurfr.rest.js;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PriceJS {
    public boolean newPrice;
    public Long start;
    public Long end;
    public Long cents;
    public Long contractorId;
    public Long id;
}


