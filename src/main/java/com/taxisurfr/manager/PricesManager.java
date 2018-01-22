package com.taxisurfr.manager;

import com.taxisurfr.domain.Contractor;
import com.taxisurfr.domain.Price;
import com.taxisurfr.domain.Route;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
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

    public Price getById(Long id) {
        return getEntityManager().find(Price.class,id);
    }

    public List<Price> getPrices(Route route) {
        List<Price> priceList = getEntityManager().createNamedQuery("Price.getByRoute")
                .setParameter("route", route)
                .getResultList();

        return priceList;
    }
}
