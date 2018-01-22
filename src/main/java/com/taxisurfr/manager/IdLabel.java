package com.taxisurfr.manager;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class IdLabel implements java.io.Serializable {
    public Long id;
    public String label;
}

