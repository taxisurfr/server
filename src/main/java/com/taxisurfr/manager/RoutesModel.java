package com.taxisurfr.manager;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class RoutesModel {

    public boolean admin;
    public List routesList;
    public List locations;
    public boolean created;
}
