package org.unhcr.osmosys.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.Pillar;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class PillarDao extends GenericDaoJpa<Pillar, Long> {
    public PillarDao() {
        super(Pillar.class, Long.class);
    }

    public List<Pillar> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM Pillar o " +
                "WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, State.class);
        q.setParameter("state", state);
        return q.getResultList();
    }


}
