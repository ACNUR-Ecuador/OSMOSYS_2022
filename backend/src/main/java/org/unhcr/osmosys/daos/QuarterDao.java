package org.unhcr.osmosys.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.Quarter;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class QuarterDao extends GenericDaoJpa<Quarter, Long> {
    public QuarterDao() {
        super(Quarter.class, Long.class);
    }

    public List<Quarter> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM Quarter o " +
                "WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, Quarter.class);
        q.setParameter("state", state);
        return q.getResultList();
    }




}
