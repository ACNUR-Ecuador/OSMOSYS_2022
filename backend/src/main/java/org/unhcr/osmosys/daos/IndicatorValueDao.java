package org.unhcr.osmosys.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.IndicatorValue;
import org.unhcr.osmosys.model.enums.DissagregationType;

import javax.ejb.Stateless;
import javax.persistence.Query;
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

    public void updateStateByPeriodIdIndicatorIdAndDissagregationType(List<Long> indicatorExecutionIds, DissagregationType dissagregationType, State state) {
        /*String jpql = "UPDATE IndicatorValue iv " +
                " SET iv.state=:state " +
                " where iv.id in ( " +
                " select o.id from IndicatorValue o where " +
                "  o.month.quarter.indicatorExecution.id in (:indicatorExecutionIds) " +
                " and iv.dissagregationType=:dissagregationType " +
                " ) ";
        Query q = getEntityManager().createQuery(jpql);*/
        String sql=" UPDATE " +
                " osmosys.indicator_values " +
                " SET state=:state " +
                " FROM " +
                " ( " +
                " SELECT " +
                " iv.id as iv_id " +
                " FROM " +
                " osmosys.indicator_values iv " +
                " INNER JOIN osmosys.months mo on iv.month_id=mo.id " +
                " INNER JOIN osmosys.quarters q on mo.quarter_id=q.id " +
                " INNER JOIN osmosys.indicator_executions ie on q.indicator_execution_id=ie.id " +
                " WHERE ie.id in( :indicatorExecutionIds ) AND iv.dissagregation_type=:dissagregationType " +
                " ) AS s " +
                " WHERE osmosys.indicator_values.id=s.iv_id ";
        Query q = getEntityManager().createNativeQuery(sql);
        q.setParameter("dissagregationType", dissagregationType.getStringValue());
        q.setParameter("indicatorExecutionIds", indicatorExecutionIds);
        q.setParameter("state", state.getStringValue());
        q.executeUpdate();
    }


}
