package com.taxisurfr.manager;

import com.taxisurfr.domain.Location;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class LocationManager extends AbstractDao<Location> {
    @Inject
    Logger logger;

    private static final String TAXI = "/taxi-";

    public LocationManager() {
        super(Location.class);
    }


    public Location getStartFromLink(String link) {
        return getFromLink(link,0);
    }
    public Location getEndFromLink(String link) {
        return getFromLink(link,1);
    }

    public Location getFromLink(String link,int index) {
        Location location = null;
        try {
            link = link.split(TAXI)[1];
            link = link.split("--")[index];
            if (link.contains("-")){
                String[] txt =link.split("-");
                link = txt[0]+" "+txt[1];
            }

            link = link.toUpperCase();
            location = (Location) getEntityManager().createNamedQuery("Location.get")
                    .setParameter("name", link)
                    .getResultList().get(0);
        } catch (Exception ex){

        }

        return location;
    }

    public String getLink(Location start, Location end) {
        String startroute = start.getName();
        String endroute = end.getName();
        startroute = startroute.replace(' ', '-').toLowerCase();
        endroute = endroute.replace(' ', '-').toLowerCase();
        return TAXI + startroute + "--" + endroute;
    }

    public List<Location> getLocationLike(String link) {
        return getEntityManager().createNamedQuery("Location.getLike")
                .setParameter("name", link+"%")
                .getResultList();
    }

    public Location getLocation(String name) {
        logger.info("getLocation:"+name);
        name = name != null ? name.trim().toUpperCase().replace("-"," ") : "_null_";
        List locations = getEntityManager().createNamedQuery("Location.get")
                .setParameter("name", name)
                .getResultList();
        if (locations.size()==0){
            logger.severe("no locations found for search string:"+name);
            return null;
        }else{
            return (Location) locations.get(0);
        }
    }
}
