package com.taxisurfr.rest.js;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TransferJS {
    public int cents;
    public Long contractorId;
    public String description;
}


