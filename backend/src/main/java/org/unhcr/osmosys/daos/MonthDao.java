package org.unhcr.osmosys.daos;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.Month;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class MonthDao extends GenericDaoJpa<Month, Long> {
    public MonthDao() {
        super(Month.class, Long.class);
    }

    public List<Month> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM Month o " +
                "WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, Month.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public List<Month> getMonthsIndicatorExecutionId(Long indicatorExecutionId, State state) {
        String jpql = "SELECT DISTINCT o FROM Month o " +
                "WHERE o.state = :state" +
                " and o.quarter.indicatorExecution.id =: indicatorExecutionId";
        Query q = getEntityManager().createQuery(jpql, Month.class);
        q.setParameter("state", state);
        q.setParameter("indicatorExecutionId", indicatorExecutionId);
        return q.getResultList();
    }
}
