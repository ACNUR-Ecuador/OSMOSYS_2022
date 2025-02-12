package org.unhcr.osmosys.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import org.unhcr.osmosys.model.ResultManagerIndicator;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class ResultManagerIndicatorDao extends GenericDaoJpa<ResultManagerIndicator, Long> {
    public ResultManagerIndicatorDao() {
        super(ResultManagerIndicator.class, Long.class);
    }

    public ResultManagerIndicator getResultManagerIndicatorByIdParameters(Long indicatorId, int quarterYearOrder, Long populationTypeId, Long periodId) {

        String jpql = "SELECT DISTINCT o FROM ResultManagerIndicator o " +
                "WHERE o.indicator.id = :indicatorId "+
                "AND o.quarterYearOrder = :quarterYearOrder "+
                "AND o.populationType.id = :populationTypeId "+
                "AND o.period.id = :periodId ";
        Query q = getEntityManager().createQuery(jpql, ResultManagerIndicator.class);
        q.setParameter("indicatorId", indicatorId);
        q.setParameter("quarterYearOrder", quarterYearOrder);
        q.setParameter("populationTypeId", populationTypeId);
        q.setParameter("periodId", periodId);

        List<ResultManagerIndicator> result = q.getResultList();

        if (result.isEmpty()) {
            // Manejar el caso cuando no se encuentra ningún resultado
            return null;
        } else {
            return result.get(0);
        }
    }






}
