package org.unhcr.osmosys.daos;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.CustomDissagregation;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class CustomDissagregationDao extends GenericDaoJpa<CustomDissagregation, Long> {
    public CustomDissagregationDao() {
        super(CustomDissagregation.class, Long.class);
    }

    public List<CustomDissagregation> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM CustomDissagregation o " +
                "WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, CustomDissagregation.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public CustomDissagregation getByName(String name) {
        String jpql = "SELECT DISTINCT o FROM CustomDissagregation o " +
                "WHERE lower(o.name) = lower(:name)";
        Query q = getEntityManager().createQuery(jpql, CustomDissagregation.class);
        q.setParameter("name", name);
        try {
            return (CustomDissagregation) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
