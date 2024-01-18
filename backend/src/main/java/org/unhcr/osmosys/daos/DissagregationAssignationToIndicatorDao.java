package org.unhcr.osmosys.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import org.unhcr.osmosys.model.DissagregationAssignationToIndicator;
import org.unhcr.osmosys.model.Indicator;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class DissagregationAssignationToIndicatorDao extends GenericDaoJpa<DissagregationAssignationToIndicator, Long> {
    public DissagregationAssignationToIndicatorDao() {
        super(DissagregationAssignationToIndicator.class, Long.class);
    }

    public List<DissagregationAssignationToIndicator> getByIndicatorExecutionId(Long indicatorExecutionId, Long periodId) {

        String jpql = " SELECT DISTINCT o" +
                " FROM IndicatorExecution ie " +
                " left outer join fetch ie.indicator i " +
                " left outer join fetch i.dissagregationsAssignationToIndicator o " +
                " left outer join fetch o.period p " +
                " left outer join fetch o.dissagregationAssignationToIndicatorPeriodCustomizations daicust " +
                " WHERE " +
                " ie.id=:indicatorExecutionId " +
                " and p.id =:periodId ";
        Query q = getEntityManager().createQuery(jpql, DissagregationAssignationToIndicator.class);
        return q.getResultList();

    }
}
