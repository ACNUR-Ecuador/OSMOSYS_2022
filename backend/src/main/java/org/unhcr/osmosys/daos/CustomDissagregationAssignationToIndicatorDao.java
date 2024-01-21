package org.unhcr.osmosys.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.CustomDissagregationAssignationToIndicator;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class CustomDissagregationAssignationToIndicatorDao extends GenericDaoJpa<CustomDissagregationAssignationToIndicator, Long> {
    public CustomDissagregationAssignationToIndicatorDao() {
        super(CustomDissagregationAssignationToIndicator.class, Long.class);
    }

    @SuppressWarnings("unchecked")
    public List<CustomDissagregationAssignationToIndicator> getActiveCustomDissagregationAssignationsByIndicatorExecutionId(Long ieId, Long periodId) {

        String jpql = "SELECT DISTINCT o " +
                " FROM IndicatorExecution ie " +
                " inner join  ie.indicator i " +
                " inner join  i.customDissagregationAssignationToIndicators o " +
                " WHERE ie.id = :ieId " +
                " and o.state =:state " +
                " and o.period.id =:periodId "
                ;
        Query q = getEntityManager().createQuery(jpql, CustomDissagregationAssignationToIndicator.class);
        q.setParameter("state", State.ACTIVO);
        q.setParameter("ieId", ieId);
        q.setParameter("periodId", periodId);
        return q.getResultList();
    }
}
