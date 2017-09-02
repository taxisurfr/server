package com.taxisurfr.rest.js;

import com.taxisurfr.domain.TaxisurfrUser;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LoginJS {
    public TaxisurfrUser taxisurfrUser;
    public String token;
}
