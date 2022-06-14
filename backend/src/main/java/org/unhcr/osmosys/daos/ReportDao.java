package org.unhcr.osmosys.daos;

import org.unhcr.osmosys.model.reportDTOs.IndicatorExecutionDetailedDTO;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class ReportDao {
    @PersistenceContext(unitName = "main-persistence-unit")
    protected EntityManager entityManager;

    // osmosys_ie_detailed.sql
    private static final String detailedIndicatorExecutions =
            "SELECT * " +
                    "FROM " +
                    "osmosys.ie_detailed ";
    private static final String detailedIndicatorExecutionsWithOffices =
            "SELECT * " +
                    "FROM " +
                    "osmosys.ie_detailed_with_offices_ids ie ";


    private static final String detailedIndicatorExecutionsOrder = " ORDER BY 1,2,3,4,5,6,7,8,9,15,16,17,18,19,20,23,24,25 ";


    public List<IndicatorExecutionDetailedDTO> getAllIndicatorExecutionDetailed(Long periodId) {

        String sql = ReportDao.detailedIndicatorExecutions
                + " WHERE period_id= :periodId and value>0 ";
        sql += ReportDao.detailedIndicatorExecutionsOrder;
        Query q = this.entityManager.createNativeQuery(sql, "IndicatorExecutionDetailedMapping");
        q.setParameter("periodId", periodId);
        return q.getResultList();
    }

    public List<IndicatorExecutionDetailedDTO> getAllPerformanceIndicatorsIndicatorExecutionDetailed(Long periodId) {
        String sql = ReportDao.detailedIndicatorExecutions
                + " WHERE performance_indicator_id is not null "
                + " AND period_id= :periodId and value>0";
        sql += ReportDao.detailedIndicatorExecutionsOrder;
        Query q = this.entityManager.createNativeQuery(sql, "IndicatorExecutionDetailedMapping");
        q.setParameter("periodId", periodId);
        return q.getResultList();
    }

    public List<IndicatorExecutionDetailedDTO> getPartnersIndicatorsExecutionsDetailedByPeriodId(Long periodId) {

        String sql = ReportDao.detailedIndicatorExecutions
                + " WHERE project_id is not null "
                + " AND period_id= :periodId and value>0";
        sql += ReportDao.detailedIndicatorExecutionsOrder;
        Query q = this.entityManager.createNativeQuery(sql, "IndicatorExecutionDetailedMapping");
        q.setParameter("periodId", periodId);
        return q.getResultList();
    }

    public List<IndicatorExecutionDetailedDTO> getPartnersGeneralIndicatorsDetailedByPeriodId(Long periodId) {

        String sql = ReportDao.detailedIndicatorExecutions
                + " WHERE project_id is not null "
                + " AND performance_indicator_id is null "
                + " AND period_id= :periodId " +
                " and value>0 ";
        sql += ReportDao.detailedIndicatorExecutionsOrder;
        Query q = this.entityManager.createNativeQuery(sql, "IndicatorExecutionDetailedMapping");
        q.setParameter("periodId", periodId);
        return q.getResultList();
    }

    public List<IndicatorExecutionDetailedDTO> getPartnersPerformanceIndicatorsDetailedByPeriodId(Long periodId) {

        String sql = ReportDao.detailedIndicatorExecutions
                + " WHERE project_id is not null "
                + " AND performance_indicator_id is not null "
                + " AND period_id= :periodId" +
                " and value>0 ";
        sql += ReportDao.detailedIndicatorExecutionsOrder;
        Query q = this.entityManager.createNativeQuery(sql, "IndicatorExecutionDetailedMapping");
        q.setParameter("periodId", periodId);
        return q.getResultList();
    }

    public List<IndicatorExecutionDetailedDTO> getDirectImplementationPerformanceIndicatorsDetailedByPeriodId(Long periodId) {
        String sql = ReportDao.detailedIndicatorExecutions
                + " WHERE project_id is null "
                + " AND performance_indicator_id is not null "
                + " AND period_id= :periodId " +
                " and value>0";
        sql += ReportDao.detailedIndicatorExecutionsOrder;
        Query q = this.entityManager.createNativeQuery(sql, "IndicatorExecutionDetailedMapping");
        q.setParameter("periodId", periodId);
        return q.getResultList();
    }

    public List<IndicatorExecutionDetailedDTO> getPartnerDetailedByProjectId(Long projectId) {
        String sql = ReportDao.detailedIndicatorExecutions
                + " WHERE project_id= :projectId ";
        sql += ReportDao.detailedIndicatorExecutionsOrder;
        Query q = this.entityManager.createNativeQuery(sql, "IndicatorExecutionDetailedMapping");
        q.setParameter("projectId", projectId);
        return q.getResultList();
    }

    public List<IndicatorExecutionDetailedDTO> getAllIndicatorExecutionDetailedByPeriodIdAndOfficeId(Long periodId, Long officeId) {
        String sql = ReportDao.detailedIndicatorExecutionsWithOffices
                + " WHERE performance_indicator_id is not null "
                + " AND ie.period_id= :periodId and ie.value>0 and (ie.office_id=:officeId or ie.office_parent_id=:officeId )";
        sql += ReportDao.detailedIndicatorExecutionsOrder;
        Query q = this.entityManager.createNativeQuery(sql, "IndicatorExecutionDetailedWithOfficesDTO");
        q.setParameter("periodId", periodId);
        q.setParameter("officeId", officeId);
        return q.getResultList();
    }
}
