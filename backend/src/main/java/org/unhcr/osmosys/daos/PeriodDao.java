package org.unhcr.osmosys.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.Period;

import javax.ejb.Stateless;
import javax.persistence.Query;
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
        Query q = getEntityManager().createQuery(jpql, State.class);
        q.setParameter("state", state);
        return q.getResultList();
    }


}
