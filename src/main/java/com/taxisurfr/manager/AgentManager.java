package com.taxisurfr.manager;

import com.taxisurfr.domain.Agent;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import java.util.List;
import java.util.logging.Logger;


@Stateless
public class AgentManager extends AbstractDao<Agent> {
    @Inject
    Logger logger;

    public AgentManager() {
        super(Agent.class);
        ;

    }

    public com.taxisurfr.domain.Agent getAgent(com.taxisurfr.domain.Contractor contractor) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    private Agent createAgent(String email, boolean admin) {


        throw new UnsupportedOperationException("not yet implemented");

    }

    public Agent getAgent(String email) {
        Agent agent = (Agent) getEntityManager().createNamedQuery("Agent.getByEmail")
                .setParameter("email", email)
                .getSingleResult();
        return agent;
    }

    public List<Agent> getAgents() {
        Query query = getEntityManager().createQuery("SELECT e FROM Agent e");
        return (List<Agent>) query.getResultList();
    }

}
