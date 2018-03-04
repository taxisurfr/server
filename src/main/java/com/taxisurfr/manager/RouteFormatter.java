package com.taxisurfr.manager;

import com.taxisurfr.domain.Price;

public class RouteFormatter {
    public static String asRoute(Price price){
        return price.getStartroute().getName()+" to "+price.getEndroute().getName();
    }
}
