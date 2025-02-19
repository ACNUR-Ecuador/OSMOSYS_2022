package org.unhcr.osmosys.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import org.unhcr.osmosys.model.ResultManagerIndicator;
import org.unhcr.osmosys.model.ResultManagerIndicatorQuarterReport;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;
@Stateless
public class ResultManagerIndicatorQuarterReportDao  extends GenericDaoJpa<ResultManagerIndicatorQuarterReport,Long> {
    public ResultManagerIndicatorQuarterReportDao() {super(ResultManagerIndicatorQuarterReport.class, Long.class);}

    public ResultManagerIndicatorQuarterReport getResultManIndQuarterReportByIdParameters(Long indicatorId, int quarterYearOrder, Long periodId) {

        String jpql = "SELECT DISTINCT o FROM ResultManagerIndicatorQuarterReport o " +
                "WHERE o.indicator.id = :indicatorId "+
                "AND o.quarterYearOrder = :quarterYearOrder "+
                "AND o.period.id = :periodId ";
        Query q = getEntityManager().createQuery(jpql, ResultManagerIndicatorQuarterReport.class);
        q.setParameter("indicatorId", indicatorId);
        q.setParameter("quarterYearOrder", quarterYearOrder);
        q.setParameter("periodId", periodId);

        List<ResultManagerIndicatorQuarterReport> result = q.getResultList();

        if (result.isEmpty()) {
            // Manejar el caso cuando no se encuentra ningún resultado
            return null;
        } else {
            return result.get(0);
        }
    }
}
