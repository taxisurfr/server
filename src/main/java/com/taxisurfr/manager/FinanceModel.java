package com.taxisurfr.manager;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class FinanceModel {

    public List paymentList;
    public List transferList;
    public List summaryList;
    public boolean admin;
}
