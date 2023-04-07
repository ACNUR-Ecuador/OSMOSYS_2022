package org.unhcr.osmosys.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.IndicatorValueCustomDissagregation;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class IndicatorValueCustomDissagregationDao extends GenericDaoJpa<IndicatorValueCustomDissagregation, Long> {
    public IndicatorValueCustomDissagregationDao() {
        super(IndicatorValueCustomDissagregation.class, Long.class);
    }

    public List<IndicatorValueCustomDissagregation> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM IndicatorValueCustomDissagregation o " +
                "WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, IndicatorValueCustomDissagregation.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public List<IndicatorValueCustomDissagregation> getIndicatorValueCustomDissagregationsByMonthId(Long monthId, State state) {

        String jpql = "SELECT DISTINCT o FROM IndicatorValueCustomDissagregation o" +
                " inner join fetch o.month m " +
                " inner join fetch o.customDissagregationOption cdo " +
                " inner join fetch cdo.customDissagregation cd "+
                " inner join fetch cd.customDissagregationOptions cdo2 "+
                " WHERE m.id  = :monthId " +
                " and o.state =:state " +
                " and cdo.state =:state " +
                " and cdo2.state =:state "
                ;
        Query q = getEntityManager().createQuery(jpql, IndicatorValueCustomDissagregation.class);
        q.setParameter("monthId",monthId);
        q.setParameter("state",state);
        return q.getResultList();
    }


}
