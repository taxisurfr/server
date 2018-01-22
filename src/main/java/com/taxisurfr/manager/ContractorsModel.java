package com.taxisurfr.manager;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class ContractorsModel {

    public boolean admin;
    public List contractorsList;
}
