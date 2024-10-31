package org.unhcr.osmosys.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;

import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.Area;
import org.unhcr.osmosys.model.Audit;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class AuditDao extends GenericDaoJpa<Audit, Long> {
    public AuditDao() {
        super(Audit.class, Long.class);
    }

    public List<Audit> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM Audit o " +
                "WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, Audit.class);
        q.setParameter("state", state);
        return q.getResultList();
    }



}
