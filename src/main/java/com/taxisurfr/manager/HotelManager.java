package com.taxisurfr.manager;

import com.taxisurfr.domain.Hotel;
import com.taxisurfr.domain.Location;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class HotelManager extends AbstractDao<Hotel> {
    @Inject
    Logger logger;

    public HotelManager() {
        super(Hotel.class);
    }

    public Hotel getByLink(String link) {
        List<Hotel> hotelList = getEntityManager().createNamedQuery("Hotel.getByLink")
                .setParameter("link", link)
                .getResultList();
        return hotelList !=null && hotelList.size() >0 ? hotelList.get(0) : null;
    }

    public List<Hotel> getHotelsAtLocation(Location location) {
        List<Hotel> hotelList = getEntityManager().createNamedQuery("Hotel.getHotelsAtLocation")
                .setParameter("location", location)
                .getResultList();
        return hotelList;
    }

}
