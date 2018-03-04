package com.taxisurfr.manager;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class PricesModel {

    public List prices;
    public boolean admin;
    public List locations;
    public List contractors;
}
