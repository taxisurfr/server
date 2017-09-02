package com.taxisurfr.rest.js;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SessionJS {
    public Long routeId;
    public String hostname;
    public boolean shareAnnouncement;
    public String country;
}
