package com.taxisurfr.manager;

import com.taxisurfr.domain.TaxisurfrUser;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Stateless
public class UserDAO extends AbstractDao<TaxisurfrUser> {
    @Inject
    Logger logger;

    public UserDAO() {
        super(TaxisurfrUser.class);;

    }

    public Optional<TaxisurfrUser> findByEmail(String email) {
        TaxisurfrUser user = null;
        List<TaxisurfrUser> routes = getEntityManager().createNamedQuery("User.findByEmail")
            .setParameter("email", email)
            .getResultList();
        if (routes.size() > 0){
            user = routes.get(0);
        }
        return Optional.ofNullable(user);
    }

    public Optional<TaxisurfrUser> findByProvider(TaxisurfrUser.Provider provider, String id) {
        return null;
    }

    public Optional<TaxisurfrUser> findById(long l) {
        return null;
    }

    public TaxisurfrUser save(TaxisurfrUser userToSave) {
        return null;
    }
}
