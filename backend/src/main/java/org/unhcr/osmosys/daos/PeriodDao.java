package org.unhcr.osmosys.daos;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.Pillar;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class PeriodDao extends GenericDaoJpa<Period, Long> {
    public PeriodDao() {
        super(Period.class, Long.class);
    }

    public List<Period> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM Period o " +
                "WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, Period.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public Period getByYear(Integer year) throws GeneralAppException {

        String jpql = "SELECT DISTINCT o FROM Period o " +
                " WHERE o.year = :year";
        Query q = getEntityManager().createQuery(jpql, Period.class);
        q.setParameter("year", year);
        try {
            return (Period) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontró más de un item con el año " + year, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
