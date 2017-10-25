package com.taxisurfr.manager;

import com.taxisurfr.domain.Agent;
import com.taxisurfr.domain.Contractor;
import com.taxisurfr.domain.PickupType;
import com.taxisurfr.domain.Route;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class RouteManager extends AbstractDao<Route> {

    @Inject
    Logger logger;

    private static List<Route> routeInfoCache;

    public RouteManager() {
        super(Route.class);
        ;

    }


    public void createLinkInDescription() {
        List<Route> routes = getEntityManager().createNamedQuery("Route.getAll").getResultList();
        for (Route route : routes) {
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
            if (routes.size() == 1) {
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
        logger.info("query:" + query);
        List<Route> routes = getEntityManager().createNamedQuery("Route.getByQuery")
                .setParameter("query", query)
                .getResultList();
        logger.info("count:" + routes);
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

    public RoutesModel getRoutes(Agent agent, boolean admin) {
        RoutesModel routesModel = new RoutesModel();
        routesModel.admin = admin;
        try {

            if (admin) {
                routesModel.routesList = getEntityManager().createNamedQuery("Route.getAll")
                        .getResultList();
            } else {
                routesModel.routesList = new ArrayList<>();
                if (agent != null) {
                    List<Contractor> listContractor = getEntityManager().createNamedQuery("Contractor.getByAgent")
                            .setParameter("agentId", agent.getId())
                            .getResultList();
                    for (Contractor contractor : listContractor) {
                        routesModel.routesList.addAll(
                                getEntityManager().createNamedQuery("Route.getByContractor")
                                        .setParameter("contractorId", contractor.getId())
                                        .getResultList()
                        );
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Comparator<Route> comparator = new Comparator<Route>(){

            @Override
            public int compare(Route o1, Route o2) {
                return o1.getStartroute().compareTo(o2.getStartroute());
            }
        };
        Collections.sort(routesModel.routesList, comparator);
        return routesModel;
    }

    public void editRoute(Long id, int cents) {
        Route route = find(id);
        route.setCents(cents);
        persist(route);
    }
}
