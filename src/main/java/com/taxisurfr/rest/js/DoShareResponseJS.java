package com.taxisurfr.rest.js;

import com.taxisurfr.domain.Booking;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DoShareResponseJS {
    public Booking booking;
    public String stripeKey;
}
