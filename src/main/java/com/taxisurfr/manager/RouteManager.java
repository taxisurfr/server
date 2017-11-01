package com.taxisurfr.manager;

import com.taxisurfr.domain.Agent;
import com.taxisurfr.domain.Contractor;
import com.taxisurfr.domain.PickupType;
import com.taxisurfr.domain.Route;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;
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

    private static final String[] LOCATIONS = {"Colombo Airport", "Colombo Downtown", "Arugam Bay",
            "Dambulla", "Galle", "Haputale", "Hikkaduwa", "Kalpitiya", "Kandy", "Kitulgala", "Polunnaruwa", "Mirissa",
            "Mirissa - return",
            "Weligama", "Weligama - return","Yala Tissamaharama", "Polunaruwa", "Bandarawella", "Ella", "Tangalle", "Akkaraipattu", "Nuwara Eliya",
            "Midigama", "Kalpitiya", "Batikallo", "Passikuda","Sigiriya", "Trinco", "Udawalawa", "Unawatuna"};

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


        routesModel.locations = new ArrayList();
        for (String s: LOCATIONS){
            ValueLabel valueLabel = new ValueLabel();
            valueLabel.value = s;
            valueLabel.label = s;

            routesModel.locations.add(valueLabel);
        }
        return routesModel;
    }

    public void editRoute(Long id, int cents) {
        Route route = find(id);
        route.setCents(cents);
        persist(route);
    }

    public boolean saveRoute(String startroute, String endroute) {
        boolean created = false;
        Route route = getRoute(startroute,endroute);
        if (route == null) {
            List<Route>listRoutes = getEntityManager().createNamedQuery("Route.getByStart")
                    .setParameter("startroute", startroute)
                    .getResultList();

            Comparator<Route> routeComparator = new Comparator<Route>(){

                @Override
                public int compare(Route o1, Route o2) {
                    return o1.getId().compareTo(o2.getId());
                }
            };
            Collections.sort(listRoutes, routeComparator);

            route = new Route();
            route.setId(listRoutes.get(listRoutes.size()-1).getId()+1);

            route.setStartroute(startroute);
            route.setEndroute(endroute);
            route.setCents(99900);
            startroute = startroute.replace(' ','-').toLowerCase();
            endroute = endroute.replace(' ','-').toLowerCase();
            route.setDescription("/taxi-"+startroute+'-'+endroute);
            //fixme
            route.setContractorId(1L);
            persist(route);
            created = true;
        }else{
            logger.warning("did not create : already exists");
        }
        return created;
    }
}
