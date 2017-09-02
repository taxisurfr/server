package com.taxisurfr.rest.js;

import com.taxisurfr.domain.Route;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by nbb on 05.04.2016.
 */
@XmlRootElement
public class NewSessionResultJS {
    public String stripePublishable;
    public Route route;
    public String hostname;
}


