package com.taxisurfr.manager;

import com.taxisurfr.domain.Contractor;
import com.taxisurfr.domain.Location;
import com.taxisurfr.domain.Price;
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
    LocationManager locationManager;

    @Inject
    RouteManager routeManager;

    @Inject
    Mailer mailer;

    public List<Price> getPrices(String email, boolean admin) {
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
                return o1.getStartroute().getName().compareTo(o2.getStartroute().getName());
            }
        };
        Collections.sort(pricesList, routeComparator);
        return pricesList;
    }

    public void updateOrCreatePrice(Price price) {

        boolean update = price.getId() != null;
        if (update) {
            Price priceToUpdate = find(price.getId());
            mergePrice(priceToUpdate, price);
        } else {
            Price newPrice = new Price();
            newPrice.setContractor(price.getContractor());
            newPrice.setStartroute(price.getStartroute());
            newPrice.setEndroute(price.getEndroute());
            newPrice.setCents(price.getCents());
            getEntityManager().persist(newPrice);
        }
    }

    private void mergePrice(Price priceToMerge, Price price) {
        priceToMerge.setStartroute(price.getStartroute());
        priceToMerge.setEndroute(price.getStartroute());
        priceToMerge.setCents(price.getCents());
        priceToMerge.setContractor(price.getContractor());

        getEntityManager().persist(priceToMerge);
    }

    public Price getByLocationAndContractor(Location start, Location end, Contractor contractor) {
        return (Price) getEntityManager().createNamedQuery("Price.getByLocationAndContractor")
                .setParameter("start", start)
                .setParameter("end", end)
                .setParameter("contractor", contractor)
                .getSingleResult();
    }

    public List<Price> getPrices(Location start, Location end) {
        if (start == null || end == null) return new ArrayList<>();
        List<Price> priceList = findPrices(start, end);
        if (priceList.size() == 0) {
            priceList = findPrices(end, start);
            if (priceList.size() == 0) {
                createPrice(start, end, 9999L);
            } else {
                for (Price price : priceList) {
                    createPrice(start, end, price.getCents());
                }
            }
            priceList = findPrices(start, end);
        }
        return priceList;
    }

    private void createPrice(Location startroute, Location endroute, Long cents) {
        Price price = new Price();
        price.setStartroute(startroute);
        price.setEndroute(endroute);
        Contractor contractor = contractorManager.getContractorById(1L);
        price.setContractor(contractor);
        price.setCents(cents);

        price.setLink(locationManager.getLink(startroute, endroute));
        getEntityManager().persist(price);
        mailer.sendPriceCreated(startroute.getName(), endroute.getName());
    }

    private List<Price> findPrices(Location start, Location end) {
        List<Price> priceList = getEntityManager().createNamedQuery("Price.getByLocation")
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();


        return priceList;
    }

    public Price getFromLink(String link) {
        if (link != null) {
            List<Price> routes = getEntityManager().createNamedQuery("Price.getByLink")
                    .setParameter("link", link)
                    .getResultList();
            if (routes.size() == 1) {
                return routes.get(0);
            }
        }
        return null;
    }

    public Price getPrice(String offer) {
        Long id = Long.parseLong(offer);
        Price price = getEntityManager().find(Price.class, id);
        return price;
    }
}
