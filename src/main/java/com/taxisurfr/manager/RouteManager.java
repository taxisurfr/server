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
    @Inject
    PricesManager pricesManager;

    private static List<Route> routeInfoCache;

    public RouteManager() {
        super(Route.class);
        ;

    }

/*
    private static final String[] LOCATIONS = {"Colombo Airport", "Colombo Downtown", "Arugam Bay",
            "Dambulla", "Galle", "Haputale", "Hikkaduwa", "Kalpitiya", "Kandy", "Kitulgala", "Polunnaruwa", "Mirissa",
            "Mirissa - return",
            "Weligama", "Weligama - return","Yala Tissamaharama", "Polunaruwa", "Bandarawella", "Ella", "Tangalle", "Akkaraipattu", "Nuwara Eliya",
            "Midigama", "Kalpitiya", "Batikallo", "Passikuda","Sigiriya", "Trinco", "Udawalawa", "Unawatuna"};
*/

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
        Route route = findRoute(pickup,dropoff);

        if (route == null){
            PickupType pickupType = "Colombo Airport".equals(pickup) ? PickupType.AIRPORT : PickupType.HOTEL;
            route = createRoute(pickup,dropoff,pickupType);
        }
        return route;
    }

    private Route createRoute(String pickup, String dropoff,PickupType pickupType){
        Route route = new Route();

        route.setStartroute(pickup);
        route.setEndroute(dropoff);
        setDescription(route);
        route.setPickupType(pickupType);
        persist(route);
        return route;
    }
    public Route findRoute(String pickup, String dropoff) {
        List<Route> routes =  getEntityManager().createNamedQuery("Route.getByStartEnd")
                .setParameter("startroute", pickup)
                .setParameter("endroute", dropoff)
                .getResultList();
        return routes.size() != 0 ? routes.get(0) : null;
    }

    public List<Route> getRoutes() {
        return findAll();
    }

    public List<Route> getRoutes(Agent agent, boolean admin) {
        List<Route> routesList = new ArrayList<>();
        try {
            if (admin) {
                routesList = getEntityManager().createNamedQuery("Route.getAll")
                        .getResultList();
            } else {
                if (agent != null) {
                    List<Contractor> listContractor = getEntityManager().createNamedQuery("Contractor.getByAgent")
                            .setParameter("agentId", agent.getId())
                            .getResultList();
                    for (Contractor contractor : listContractor) {
                        routesList.addAll(
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
        Comparator<Route> comparator = new Comparator<Route>() {

            @Override
            public int compare(Route o1, Route o2) {
                if (o1.getStartroute() == null) {
                    System.out.println();
                }
                return o1.getStartroute().compareTo(o2.getStartroute());
            }
        };
        Collections.sort(routesList, comparator);


        return routesList;
    }

    public List<ValueLabel> getLocations(List<Route> routesList) {
        List<ValueLabel> locations = new ArrayList();
        Map<String, ValueLabel> labels = new HashMap<>();
        for (Object r : routesList) {
            Route route = (Route) r;
            ValueLabel valueLabel = new ValueLabel();
            valueLabel.value = route.startroute;
            valueLabel.label = route.startroute;
            labels.put(route.startroute, valueLabel);
        }
        locations.addAll(labels.values());
        return locations;
    }

    public boolean saveRoute(String startroute, String endroute) {
        boolean created = false;
        Route route = getRoute(startroute, endroute);
        if (route == null) {
            List<Route> listRoutes = getEntityManager().createNamedQuery("Route.getByStart")
                    .setParameter("startroute", startroute)
                    .getResultList();

            Comparator<Route> routeComparator = new Comparator<Route>() {

                @Override
                public int compare(Route o1, Route o2) {
                    return o1.getId().compareTo(o2.getId());
                }
            };
            Collections.sort(listRoutes, routeComparator);

            route = new Route();
            route.setId(listRoutes.get(listRoutes.size() - 1).getId() + 1);

            route.setStartroute(startroute);
            route.setEndroute(endroute);
            setDescription(route);
            //fixme
            route.setContractorId(1L);
            persist(route);
            created = true;
        } else {
            logger.warning("did not create : already exists");
        }
        return created;
    }

    public void saveStartRoute(Long id, String startroute) {

        Route route = new Route();
        route.setId(id);
        route.setStartroute(startroute);
        route.setEndroute("Arugam Bay");
        route.setContractorId(1L);
        route.setPickupType(PickupType.HOTEL);
        setDescription(route);

        persist(route);
    }

    private void setDescription(Route route) {
        String startroute = route.getStartroute();
        String endroute = route.getEndroute();
        startroute = startroute.replace(' ', '-').toLowerCase();
        endroute = endroute.replace(' ', '-').toLowerCase();
        route.setDescription("/taxi-" + startroute + '-' + endroute);
    }

    public Route getRouteById(Long routeId) {
        return  find(routeId);
    }
}
