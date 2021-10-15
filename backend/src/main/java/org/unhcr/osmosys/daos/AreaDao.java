package org.unhcr.osmosys.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.Area;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class AreaDao extends GenericDaoJpa<Area, Long> {
    public AreaDao() {
        super(Area.class, Long.class);
    }

    public List<Area> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM Area o " +
                "WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, State.class);
        q.setParameter("state", state);
        return q.getResultList();
    }


}
