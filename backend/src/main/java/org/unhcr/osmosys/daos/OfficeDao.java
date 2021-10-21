package org.unhcr.osmosys.daos;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.Office;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.List;

@Stateless
public class OfficeDao extends GenericDaoJpa<Office, Long> {
    public OfficeDao() {
        super(Office.class, Long.class);
    }


    public List<Office> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM Office o " +
                "WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, Office.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public Office getByAcronym(String acronym) throws GeneralAppException {

        String jpql = "SELECT DISTINCT o FROM Office o " +
                "WHERE lower(o.acronym) = lower(:acronym)";
        Query q = getEntityManager().createQuery(jpql, Office.class);
        q.setParameter("acronym", acronym);
        try {
            return (Office) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontró más de un item con el código " + acronym, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Office getByDescription(String description) throws GeneralAppException {

        String jpql = "SELECT DISTINCT o FROM Office o " +
                "WHERE lower(o.description) = lower(:description)";
        Query q = getEntityManager().createQuery(jpql, Office.class);
        q.setParameter("description", description);
        try {
            return (Office) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontró más de un item con la misma descripción " + description, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public List<Office> getByNoParent() {
        String jpql = "SELECT DISTINCT o FROM Office o " +
                "WHERE o.parentOffice is null ";
        Query q = getEntityManager().createQuery(jpql, Office.class);

        return q.getResultList();
    }
}
