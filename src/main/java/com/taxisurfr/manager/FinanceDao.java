package com.taxisurfr.manager;

import com.taxisurfr.domain.Agent;
import com.taxisurfr.domain.Contractor;
import com.taxisurfr.domain.Finance;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class FinanceDao extends AbstractDao<Finance> {
    @Inject
    Logger logger;

    public FinanceDao() {
        super(Finance.class);;

    }


}
