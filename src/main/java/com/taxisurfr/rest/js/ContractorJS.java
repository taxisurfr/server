package com.taxisurfr.rest.js;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ContractorJS {
    public Long id;
    public String address1;
    public String address2;
    public String address3;
    public String address4;
    public String name;
    public String email;
    public boolean update;
}
