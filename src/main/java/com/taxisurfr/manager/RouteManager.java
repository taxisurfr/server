package com.taxisurfr.manager;

import com.taxisurfr.domain.PickupType;
import com.taxisurfr.domain.Route;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class RouteManager extends AbstractDao<Route>{

    @Inject
    Logger logger;

    private static List<Route> routeInfoCache;

    public RouteManager() {
        super(Route.class);;

    }



    public void createLinkInDescription(){
        List<Route>routes = getEntityManager().createNamedQuery("Route.getAll").getResultList();
        for (Route route : routes){
            if (route.getPickupType().equals(PickupType.AIRPORT) || route.getPickupType().equals(PickupType.HOTEL)) {
                String start = route.getStartroute().toLowerCase().trim().replace(' ', '-');
                String end = route.getEndroute().toLowerCase().trim().replace(' ', '-');
                String desc = "/taxi-" + start + "-" + end;
                route.setDescription(desc);
                persist(route);
            }
        }
    }
    public Route getRouteFromLink(String link) {
        if (link != null) {
            List<Route> routes = getEntityManager().createNamedQuery("Route.getByDescLink")
                    .setParameter("link", link)
                    .getResultList();
            if (routes.size() ==1){
                return routes.get(0);
            }
            routes = getEntityManager().createNamedQuery("Route.getByLink")
                .setParameter("link", link)
                .getResultList();
            return routes.size() > 0 ? routes.get(0) : null;
        }
        return null;
    }

    public List<Route> getRoutesStart(String query) {
        query += '%';
        logger.info("query:"+query);
        List<Route> routes = getEntityManager().createNamedQuery("Route.getByQuery")
                .setParameter("query", query)
                .getResultList();
        logger.info("count:"+routes);
        return routes;

    }


    public Route getRoute(String pickup, String dropoff) {
        List<Route> routes = getEntityManager().createNamedQuery("Route.getByStartEnd")
                .setParameter("startroute", pickup)
                .setParameter("endroute", dropoff)
                .getResultList();
        return routes.size() > 0 ? routes.get(0) : null;
    }


    public List<Route> getRoutes() {
        return findAll();
    }
}
