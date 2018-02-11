package com.taxisurfr.manager;

import com.taxisurfr.domain.Contractor;
import com.taxisurfr.domain.Price;
import com.taxisurfr.domain.Route;
import com.taxisurfr.util.Mailer;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class PricesManager extends AbstractDao<Price> {
    @Inject
    Logger logger;

    public PricesManager() {
        super(Price.class);
    }

    @Inject
    ContractorManager contractorManager;

    @Inject
    RouteManager routeManager;

    @Inject
    Mailer mailer;

    public  List<Price> getPrices(String email, boolean admin) {
        List<Price> pricesList = new ArrayList<>();
        if (admin) {
            pricesList = getEntityManager().createNamedQuery("Price.getAll")
                    .getResultList();

        } else {
            Contractor contractor = contractorManager.getByEmail(email);
            pricesList = getEntityManager().createNamedQuery("Price.getByContractor")
                    .setParameter("contractor", contractor)
                    .getResultList();
        }

        Comparator<Price> routeComparator = new Comparator<Price>() {

            @Override
            public int compare(Price o1, Price o2) {
                return o1.getRoute().getStartroute().compareTo(o2.getRoute().getStartroute());
            }
        };
        Collections.sort(pricesList, routeComparator);
        return pricesList;
    }

    public void updateOrCreatePrice(Price price) {

        boolean update = price.getId()!=null;
        if (update) {
            Price priceToUpdate = find(price.getId());
            mergePrice(priceToUpdate,price);
        } else {
            Price newPrice = new Price();
            newPrice.setContractor(price.getContractor());
            newPrice.setRoute(price.getRoute());
            newPrice.setCents(price.getCents());
            getEntityManager().persist(newPrice);
        }
    }

    private void mergePrice(Price priceToMerge, Price price){
        priceToMerge.getRoute().setStartroute(price.getRoute().getStartroute());
        priceToMerge.getRoute().setEndroute(price.getRoute().getStartroute());
        priceToMerge.setCents(price.getCents());
        priceToMerge.setContractor(price.getContractor());

        getEntityManager().persist(priceToMerge);
    }

    public Price getById(Route route, Contractor contractor) {
        logger.info("----------route "+ route.getId()+ " contractor "+contractor.getId());
        return (Price) getEntityManager().createNamedQuery("Price.getByContractorAndRoute")
                .setParameter("route", route)
                .setParameter("contractor", contractor)
                .getSingleResult();
    }

    public List<Price> getPrices(Route route) {
        if (route == null) return new ArrayList<>();
        List<Price> priceList = findPrices(route);
        if (priceList.size()==0){
            Route returnRoute = routeManager.findRoute(route.getEndroute(),route.getStartroute());
            if (returnRoute!=null) {
                priceList = findPrices(returnRoute);
                if (priceList.size() == 0) {
                    createPrice(route, 9999L);
                } else {
                    for (Price price : priceList) {
                        createPrice(route, price.getCents());
                    }
                }
            }else{
                createPrice(route, 9999L);
            }
            priceList = findPrices(route);
        }


        return priceList;
    }

    private void createPrice(Route route, Long cents) {
        Price price = new Price();
        price.setRoute(route);
        Contractor contractor = contractorManager.getContractorById(0L);
        price.setContractor(contractor);
        price.setCents(cents);
        getEntityManager().persist(price);
        mailer.sendPriceCreated(route.getStartroute(),route.getEndroute());
    }

    private List<Price>findPrices(Route route){
        List<Price> priceList = getEntityManager().createNamedQuery("Price.getByRoute")
                .setParameter("route", route)
                .getResultList();

        return priceList;
    }
}
