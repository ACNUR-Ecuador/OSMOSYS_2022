package org.unhcr.osmosys.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.DissagregationAssignationToIndicator;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class DissagregationAssignationToIndicatorDao extends GenericDaoJpa<DissagregationAssignationToIndicator, Long> {
    public DissagregationAssignationToIndicatorDao() {
        super(DissagregationAssignationToIndicator.class, Long.class);
    }

    /******************************************/


    @SuppressWarnings("unchecked")
    public List<DissagregationAssignationToIndicator> getActiveDissagregationAssignationsByIndicatorExecutionId(Long ieId, Long periodId) {

        String jpql = "SELECT DISTINCT o " +
                " FROM IndicatorExecution ie " +
                " inner join ie.indicator i " +
                " inner join i.dissagregationsAssignationToIndicator o" +
                " WHERE ie.id = :ieId " +
                " and o.period.id=:periodId" +
                " and o.state =:state ";

        Query q = getEntityManager().createQuery(jpql, DissagregationAssignationToIndicator.class);
        q.setParameter("state", State.ACTIVO);
        q.setParameter("ieId", ieId);
        q.setParameter("periodId", periodId);
        return q.getResultList();
    }

}
