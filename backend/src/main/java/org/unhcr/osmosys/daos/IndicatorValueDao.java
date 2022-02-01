package org.unhcr.osmosys.daos;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.IndicatorValue;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class IndicatorValueDao extends GenericDaoJpa<IndicatorValue, Long> {
    public IndicatorValueDao() {
        super(IndicatorValue.class, Long.class);
    }

    public List<IndicatorValue> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM IndicatorValue o " +
                "WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, IndicatorValue.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public List<IndicatorValue> getIndicatorValuesByMonthIdAndState(Long monthId, State state) {
// todo mejorar esta consulta
        String jpql = "SELECT DISTINCT o FROM IndicatorValue o " +
                "left join fetch o.month m " +
                "left join fetch m.sources " +
                "left join fetch m.indicatorValuesIndicatorValueCustomDissagregations " +
                "WHERE m.id  = :monthId " +
                " and o.state=:state";
        Query q = getEntityManager().createQuery(jpql, IndicatorValue.class);
        q.setParameter("monthId", monthId);
        q.setParameter("state", state);
        return q.getResultList();
    }
}
