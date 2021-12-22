package org.unhcr.osmosys.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.IndicatorExecution;
import org.unhcr.osmosys.model.enums.IndicatorType;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class IndicatorExecutionDao extends GenericDaoJpa<IndicatorExecution, Long> {
    public IndicatorExecutionDao() {
        super(IndicatorExecution.class, Long.class);
    }

    public List<IndicatorExecution> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM IndicatorExecution o " +
                "WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("state", state);
        return q.getResultList();
    }


    public List<IndicatorExecution> getGeneralIndicatorExecutionsByProjectId(Long projectId) {
        IndicatorType indicatorType = IndicatorType.GENERAL;
        String jpql = "SELECT DISTINCT o FROM IndicatorExecution o " +
                " left join fetch o.quarters " +
                " left join fetch o.period p " +
                " left join fetch p.generalIndicator " +
                " WHERE o.project.id = :projectId" +
                " and o.indicatorType =: generalType ";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("projectId",projectId);
        q.setParameter("generalType",indicatorType);
        return q.getResultList();
    }

    public List<IndicatorExecution> getPerformanceIndicatorExecutionsByProjectId(Long projectId) {
        IndicatorType indicatorType = IndicatorType.GENERAL;
        String jpql = "SELECT DISTINCT o FROM IndicatorExecution o " +
                " left join fetch o.quarters " +
                " left join fetch o.period p " +
                " left join fetch o.indicator " +
                " WHERE o.project.id = :projectId"+
                " and o.indicatorType <>: generalType ";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("projectId",projectId);
        q.setParameter("generalType",indicatorType);
        return q.getResultList();
    }

    public IndicatorExecution getByIdWithValues(Long id) {
        String jpql = "SELECT DISTINCT o FROM IndicatorExecution o " +
                " left join fetch o.indicator ind " +
                " left join fetch o.quarters q " +
                " left join fetch q.months m " +
                " left join fetch  m.indicatorValues " +
                " left join fetch  m.indicatorValuesIndicatorValueCustomDissagregations " +
                " WHERE o.id = :id";
        Query q = getEntityManager().createQuery(jpql, IndicatorExecution.class);
        q.setParameter("id",id);
        return (IndicatorExecution) q.getSingleResult();
    }
}
