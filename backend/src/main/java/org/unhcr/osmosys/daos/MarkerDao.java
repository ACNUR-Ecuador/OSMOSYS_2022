package org.unhcr.osmosys.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.Marker;
import org.unhcr.osmosys.model.enums.MarkerType;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class MarkerDao extends GenericDaoJpa<Marker, Long> {
    public MarkerDao() {
        super(Marker.class, Long.class);
    }

    public List<Marker> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM Marker o " +
                "WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, Marker.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public Marker getByTypeAndSubTypeAndShortDescription(MarkerType type, String subType, String shortDescription) {

        String jpql = "SELECT DISTINCT o FROM Marker o " +
                "WHERE o.type=:type and lower(o.subType)=lower(:subType) and lower(o.shortDescription)=lower(:shortDescription)";
        Query q = getEntityManager().createQuery(jpql, Marker.class);
        q.setParameter("type", type);
        q.setParameter("shortDescription", shortDescription);
        q.setParameter("subType", subType);
        try {
            return (Marker) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
