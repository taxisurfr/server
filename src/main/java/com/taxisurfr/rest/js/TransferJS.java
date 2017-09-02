package com.taxisurfr.rest.js;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TransferJS {
    public int cents;
    public Long agentId=100L;
    public String description;
}


