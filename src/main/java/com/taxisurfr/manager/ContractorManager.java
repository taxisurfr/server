package com.taxisurfr.manager;

import com.taxisurfr.domain.Agent;
import com.taxisurfr.domain.Contractor;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class ContractorManager extends AbstractDao<Contractor> {
    @Inject
    Logger logger;

    public ContractorManager() {
        super(Contractor.class);
        ;

    }

    public List<Contractor> getContractorList(boolean admin) {

        return getEntityManager().createNamedQuery("Contractor.getAll")
                .getResultList();
    }

    public List<IdLabel> getContractorIdList(boolean admin) {

        List<IdLabel>listContractorIdList = new ArrayList<>();
        for (Contractor contractor: getContractorList(admin)){
            IdLabel idLabel = new IdLabel();
                idLabel.id = contractor.getId();
                idLabel.label = contractor.getName();
            listContractorIdList.add(idLabel);
            }
        return listContractorIdList;
    }

    public void updateOrCreateContractor(Contractor contractor) {

        boolean update = contractor.getId()!=null;
        if (update) {
            List<Contractor> contractorList = getEntityManager().createNamedQuery("Contractor.getById")
                    .setParameter("id", contractor.getId())
                    .getResultList();

            Contractor contractorToUpdate = contractorList.get(0);

            contractorToUpdate.setName(contractor.getName());
            contractorToUpdate.setEmail(contractor.getEmail());
            contractorToUpdate.setAddress1(contractor.getAddress1());
            contractorToUpdate.setAddress2(contractor.getAddress2());
            contractorToUpdate.setAddress3(contractor.getAddress3());
            contractorToUpdate.setAddress4(contractor.getAddress4());

            getEntityManager().persist(contractorToUpdate);
        } else {
            Agent agent = new Agent();
            agent.setEmail(contractor.getEmail());
            getEntityManager().persist(agent);
            contractor.setAgentId(agent.getId());
            getEntityManager().persist(contractor);
        }
    }

    public Contractor getByEmail(String email) {
        List<Contractor> contractorList = getEntityManager().createNamedQuery("Contractor.getByEmail")
                .setParameter("email", email)
                .getResultList();
        return contractorList.get(0);
    }

    public Contractor getContractorById(Long contractorId) {
        return find(contractorId);
    }
}
