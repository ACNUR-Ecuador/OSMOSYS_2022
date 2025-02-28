package org.unhcr.osmosys.daos;

import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.IndicatorExecution;
import org.unhcr.osmosys.model.Office;
import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.Tags;
import org.unhcr.osmosys.model.cubeDTOs.OfficeDTO;
import org.unhcr.osmosys.model.enums.IndicatorType;
import org.unhcr.osmosys.model.reportDTOs.IndicatorExecutionDetailedDTO;
import org.unhcr.osmosys.model.reportDTOs.IndicatorExecutionTagDTO;
import org.unhcr.osmosys.model.reportDTOs.LaterReportDTO;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public List<IndicatorExecutionTagDTO> getTagReport(Tags tag, Period period) {

        String jpql = "SELECT i.performance_indicator_id, i.indicator, i.period_id , i.quarter, i.month, i.month_order,  i.month_execution as value  " +
                "FROM osmosys.ie_detailed i " +
                "WHERE i.project_id IS NOT NULL " +
                "AND i.performance_indicator_id IS NOT NULL " +
                "AND i.period_id = :periodId " +
                "AND i.value > 0 " +
                "AND i.performance_indicator_id IN :indicatorIds " +
                "GROUP BY i.performance_indicator_id, i.indicator, i.period_id, i.quarter, i.month, i.month_order,i.month_execution  " +
                "ORDER BY i.month_order";

        Query q = this.entityManager.createNativeQuery(jpql, "IndicatorExecutionTagDTOMapping");
        List<Long> streams = tag.getIndicatorTagAssignations().stream().filter(ie -> ie.getState().equals(State.ACTIVO)).map(ia -> ia.getIndicator().getId()).collect(Collectors.toList());
        q.setParameter("periodId", period.getId());
        q.setParameter("indicatorIds", streams);
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

    /****************************LATE REPORT *********************************/

    private static final String orderGroupLateReportPartners = "GROUP BY 1,2,3,4,5,7 " +
            "ORDER BY 1,2,3,4,5";
    private static final String orderGroupLateReportDirectImplementation = "GROUP BY 1,2,3,4,6,7 " +
            "ORDER BY 1,2,3,4";
    private static final String late_months_partners = "" +
            "SELECT " +
            "pr.name project, org.acronym implementer, COALESCE(i.code, '00000') indicator_code, COALESCE (i.description, '# total de beneficiarios') indicator, i.category indicator_category, string_agg(m.month, ', ' ORDER BY m.month_year_order) late_months,  fp.name focal_point, ps.name partner_supervisor " +
            "FROM " +
            "osmosys.indicator_executions ie  " +
            "LEFT JOIN osmosys.indicators i on ie.performance_indicator_id=i.id " +
            "INNER JOIN osmosys.projects pr on ie.project_id=pr.id " +
            "INNER JOIN osmosys.organizations org on pr.organization_id=org.id " +
            "INNER JOIN osmosys.quarters q on ie.id=q.indicator_execution_id and ie.state='ACTIVO'  and q.state='ACTIVO' " +
            "INNER JOIN osmosys.months m on q.id=m.quarter_id and m.state='ACTIVO'   " +
            "INNER JOIN security.user fp on fpa.focal_pointer_id=fp.id " +
            "INNER JOIN osmosys.user ps on pr.partner_manager=ps.id " +
            " " +
            "WHERE " +
            // "pr.organization_id=22 and " +
            " m.year=:year " +
            "and m.month_year_order <:month " +
            "and m.total_execution is null ";

    private static final String late_months_review_partners = "SELECT " +
            "pr.name project, org.acronym implementer, COALESCE(i.code, '00000') indicator_code, COALESCE (i.description, '# total de beneficiarios') indicator, i.category indicator_category, string_agg(m.month, ', ' ORDER BY m.month_year_order) late_months,  fp.name focal_point, ps.name partner_supervisor " +
            "FROM " +
            "osmosys.indicator_executions ie  " +
            "LEFT JOIN osmosys.indicators i on ie.performance_indicator_id=i.id " +
            "INNER JOIN osmosys.projects pr on ie.project_id=pr.id " +
            "INNER JOIN osmosys.organizations org on pr.organization_id=org.id " +
            "INNER JOIN osmosys.quarters q on ie.id=q.indicator_execution_id and ie.state='ACTIVO'  and q.state='ACTIVO' " +
            "INNER JOIN osmosys.months m on q.id=m.quarter_id and m.state='ACTIVO'   " +
            "INNER JOIN osmosys.focal_point_assignation fpa on pr.id=fpa.project_id " +
            "INNER JOIN security.user fp on fpa.focal_pointer_id=fp.id " +
            "INNER JOIN osmosys.user ps on pr.partner_manager=ps.id " +
            "WHERE " +
            // "pr.organization_id=16 and " +
            " m.year=:year " +
            "and m.month_year_order <:month " +
            "and  (m.checked is null or m.checked=FALSE) ";

    private static final String late_months_di =
            "SELECT " +
                    "o.acronym implementer, COALESCE(i.code, '00000') indicator_code, " +
                    " COALESCE (i.description, '# total de beneficiarios') indicator, i.category indicator_category, string_agg(m.month, ', ' ORDER BY m.month_year_order) late_months, " +
                    "  sup.name supervisor, res.name responsible, null as helper " +
                    "FROM " +
                    "osmosys.indicator_executions ie  " +
                    "LEFT JOIN osmosys.indicators i on ie.performance_indicator_id=i.id " +
                    "INNER JOIN osmosys.offices o on ie.reporting_office_id=o.id " +
                    "INNER JOIN osmosys.quarters q on ie.id=q.indicator_execution_id and ie.state='ACTIVO'  and q.state='ACTIVO' " +
                    "INNER JOIN osmosys.months m on q.id=m.quarter_id and m.state='ACTIVO'   " +
                    " INNER JOIN security.user sup on ie.supervisor_user_id=sup.id " +
                    " INNER JOIN security.user res on ie.assigned_user_id=res.id " +
                    " " +
                    "WHERE " +
                    // "(ie.assigned_user_id=22 or ie.assigned_user_backup_id=22) and " +
                    " m.year=:year " +
                    "and m.month_year_order <:month " +
                    "and m.total_execution is null ";
    private static final String late_months_review_di = "SELECT " +
            "o.acronym implementer, COALESCE(i.code, '00000') indicator_code, COALESCE (i.description, '# total de beneficiarios') indicator, i.category indicator_category, string_agg(m.month, ', ' ORDER BY m.month_year_order) late_months ," +
            "   sup.name supervisor, res.name responsible, null as helper " +
            " FROM " +
            "osmosys.indicator_executions ie  " +
            "LEFT JOIN osmosys.indicators i on ie.performance_indicator_id=i.id " +
            "INNER JOIN osmosys.offices o on ie.reporting_office_id=o.id " +
            "INNER JOIN osmosys.quarters q on ie.id=q.indicator_execution_id and ie.state='ACTIVO'  and q.state='ACTIVO' " +
            "INNER JOIN osmosys.months m on q.id=m.quarter_id and m.state='ACTIVO'   " +
            " INNER JOIN security.user sup on ie.supervisor_user_id=sup.id " +
            " INNER JOIN security.user res on ie.assigned_user_id=res.id " +
            "WHERE " +
            // "ie.supervisor_user_id=22  and " +
            " m.year=:year " +
            "and m.month_year_order <:month " +
            "and  (m.checked is null or m.checked=FALSE) ";


    public List<LaterReportDTO> getPartnerLateReviewByProjectId(Long projectId, Integer currentYear, Integer currentMonth) {
        String sql = ReportDao.late_months_review_partners
                + " and  pr.id= :projectId ";
        sql += ReportDao.orderGroupLateReportPartners;
        Query q = this.entityManager.createNativeQuery(sql, "LateReportMappingDTOMappingPartners");

        q.setParameter("projectId", projectId);
        q.setParameter("year", currentYear);
        q.setParameter("month", currentMonth);
        return q.getResultList();
    }

    public List<IndicatorExecution> getPartnerLateReviewReportByFocalPointId(Long focalPointId, Integer currentYear, Integer currentMonth) {

        String jpql = IndicatorExecutionDao.jpqlProjectIndicators +
                " WHERE o.project is not null " +
                " and o.state =:state " +
                " and (q.state is null or q.state =:state )" +
                " and (m.state is null or m.state =:state )" +
                " and fpu.id =:focalPointId " +
                " and fpa.state =:state " +
                " and ( " +
                " (m.year <= :currentYear )" +
                " or (m.year = :currentYear and m.monthYearOrder <= :currentMonth )" +
                ") " +
                " order by  " +
                " pr.code, pr.name, o.indicatorType, o.projectStatement.code , i.code  ";
        Query q = this.entityManager.createQuery(jpql, IndicatorExecution.class);
        q.setParameter("state", State.ACTIVO);
        q.setParameter("focalPointId", focalPointId);
        q.setParameter("currentMonth", currentMonth);
        q.setParameter("currentYear", currentYear);
        return q.getResultList();
    }

/*    public List<LaterReportDTO> getPartnerLateReviewReportByProjectId(Long projectId, Integer currentYear, Integer currentMonth) {
        String sql = ReportDao.late_months_review_partners
                + " and  pr.id= :projectId ";
        sql += ReportDao.orderGroupLateReportPartners;
        Query q = this.entityManager.createNativeQuery(sql, "LateReportMappingDTOMappingPartners");

        q.setParameter("projectId", projectId);
        q.setParameter("year", currentYear);
        q.setParameter("month", currentMonth);
        return q.getResultList();
    }*/

    public List<LaterReportDTO> getPartnerLateReportByPartnerId(Long partnerId, Integer currentYear, Integer currentMonthYearOrder) {
        String sql = ReportDao.late_months_partners
                + " and  pr.organization_id= :partnerId ";
        sql += ReportDao.orderGroupLateReportPartners;
        Query q = this.entityManager.createNativeQuery(sql, "LateReportMappingDTOMappingPartners");

        q.setParameter("projectId", partnerId);
        q.setParameter("year", currentYear);
        q.setParameter("month", currentMonthYearOrder);
        return q.getResultList();
    }

/*    public List<LaterReportDTO> getPartnerLateReportByFocalPointId(Long focalPointId, Integer currentYear, Integer currentMonthYearOrder) {
        String sql = ReportDao.late_months_partners
                + " and  pr.focal_point_id= :focalPointId ";
        sql += ReportDao.orderGroupLateReportPartners;
        Query q = this.entityManager.createNativeQuery(sql, "LateReportMappingDTOMappingPartners");

        q.setParameter("focalPointId", focalPointId);
        q.setParameter("year", currentYear);
        q.setParameter("month", currentMonthYearOrder);
        return q.getResultList();
    }*/

    public List<IndicatorExecution> getDirectImplementationLateReportByResponsableId(Long periodId, Long responsableId, Integer currentYear, Integer currentMonth) {

        String jpql = IndicatorExecutionDao.jpqlDirectImplementationIndicators +
                " WHERE p.id = :periodId " +
                " and o.indicatorType <> :generalType " +
                " and o.project.id is null" +
                " and o.state =:state " +
                " and (q.state is null or q.state =:state )" +
                " and (m.state is null or m.state =:state )" +
                " and p.id =:periodId " +
                " and ( au.id =:responsableId  or aub.id =:responsableId ) " +
                " and ( " +
                " (m.year <= :currentYear )" +
                " or (m.year = :currentYear and m.monthYearOrder <= :currentMonth )" +
                ") "
                // " order by o.reportingOffice.acronym,  " +
                //  " o.projectStatement.code , o.indicator.code  "
                ;
        Query q = this.entityManager.createQuery(jpql, IndicatorExecution.class);
        q.setParameter("generalType", IndicatorType.GENERAL);
        q.setParameter("state", State.ACTIVO);
        q.setParameter("periodId", periodId);
        q.setParameter("currentMonth", currentMonth);
        q.setParameter("currentYear", currentYear);
        q.setParameter("responsableId", responsableId);
        return q.getResultList();
    }

    public List<IndicatorExecution> getPartnerLateReportByProjectId(Long projectId, Integer currentYear, Integer currentMonth) {
        String jpql = IndicatorExecutionDao.jpqlProjectIndicators +
                " WHERE o.project is not null " +
                " and o.state =:state " +
                " and (q.state is null or q.state =:state )" +
                " and (m.state is null or m.state =:state )" +
                " and o.project.id =:projectId " +
                " and ( " +
                " (m.year <= :currentYear )" +
                " or (m.year = :currentYear and m.monthYearOrder <= :currentMonth )" +
                ") " +
                " order by  " +
                " pr.code, pr.name, o.indicatorType, o.projectStatement.code , i.code  ";
        Query q = this.entityManager.createQuery(jpql, IndicatorExecution.class);
        q.setParameter("state", State.ACTIVO);
        q.setParameter("projectId", projectId);
        q.setParameter("currentMonth", currentMonth);
        q.setParameter("currentYear", currentYear);
        return q.getResultList();
    }

    public List<LaterReportDTO> getDirectImplementationLateReviewReportBySupervisorId(Long supervisorId, Integer currentYear, Integer currentMonthYearOrder) {
        String sql = ReportDao.late_months_review_di
                + " and  ie.supervisor_user_id= :responsableId ";
        sql += ReportDao.orderGroupLateReportDirectImplementation;
        Query q = this.entityManager.createNativeQuery(sql, "LateReportMappingDTOMappingDI");

        q.setParameter("responsableId", supervisorId);
        q.setParameter("year", currentYear);
        q.setParameter("month", currentMonthYearOrder);
        return (List<LaterReportDTO>) q.getResultList();
    }

    public List<LaterReportDTO> getAllLateReviewReportDirectImplementation(Integer currentYear, Integer currentMonthYearOrder) {
        String sql = ReportDao.late_months_review_di;
        sql += ReportDao.orderGroupLateReportDirectImplementation;
        Query q = this.entityManager.createNativeQuery(sql, "LateReportMappingDTOMappingDI");

        q.setParameter("year", currentYear);
        q.setParameter("month", currentMonthYearOrder);
        return (List<LaterReportDTO>) q.getResultList();
    }

    public List<IndicatorExecution> getAllLateReportDirectImplementation(Long periodId, Integer currentYear, Integer currentMonth) {
        String jpql = IndicatorExecutionDao.jpqlDirectImplementationIndicators +
                " WHERE p.id = :periodId " +
                " and o.indicatorType <> :generalType " +
                " and o.project.id is null" +
                " and o.state =:state " +
                " and (q.state is null or q.state =:state )" +
                " and (m.state is null or m.state =:state )" +
                " and p.id =:periodId " +
                " and ( " +
                " (m.year <= :currentYear )" +
                " or (m.year = :currentYear and m.monthYearOrder <= :currentMonth )" +
                ") " /*+
                " order by o.reportingOffice.acronym,  " +
                " o.projectStatement.code , o.indicator.code  "*/;
        Query q = this.entityManager.createQuery(jpql, IndicatorExecution.class);
        q.setParameter("generalType", IndicatorType.GENERAL);
        q.setParameter("state", State.ACTIVO);
        q.setParameter("periodId", periodId);
        q.setParameter("currentMonth", currentMonth);
        q.setParameter("currentYear", currentYear);
        return q.getResultList();
    }

    public List<IndicatorExecution> getAllLateReportPartners(Long periodId, int currentMonth, int currentYear) {

        String jpql = IndicatorExecutionDao.jpqlProjectIndicators +
                " WHERE o.project is not null " +
                " and o.project.state =:state " +
                " and o.state =:state " +
                " and (q.state is null or q.state =:state )" +
                " and (m.state is null or m.state =:state )" +
                " and p.id =:periodId " +
                " and ( " +
                " (m.year <= :currentYear )" +
                " or (m.year = :currentYear and m.monthYearOrder <= :currentMonth )" +
                ") " +
                " order by org.acronym, org.description, " +
                " pr.code, pr.name, o.indicatorType, o.projectStatement.code , i.code  ";
        Query q = this.entityManager.createQuery(jpql, IndicatorExecution.class);
        q.setParameter("state", State.ACTIVO);
        q.setParameter("periodId", periodId);
        q.setParameter("currentMonth", currentMonth);
        q.setParameter("currentYear", currentYear);
        return q.getResultList();

    }

    public List<LaterReportDTO> getAllLateReviewPartners(Integer currentYear, Integer currentMonthYearOrder) {
        String sql = ReportDao.late_months_review_partners;
        sql += ReportDao.orderGroupLateReportPartners;
        Query q = this.entityManager.createNativeQuery(sql, "LateReportMappingDTOMappingPartners");

        q.setParameter("year", currentYear);
        q.setParameter("month", currentMonthYearOrder);
        return (List<LaterReportDTO>) q.getResultList();
    }

    public List<IndicatorExecution> getDirectImplementationLateReportBySupervisorId(Long periodId, Long supervisorId, Integer currentYear, Integer currentMonth) {
        String jpql = IndicatorExecutionDao.jpqlDirectImplementationIndicators +
                " WHERE p.id = :periodId " +
                " and o.indicatorType <> :generalType " +
                " and o.project.id is null" +
                " and o.state =:state " +
                " and (q.state is null or q.state =:state )" +
                " and (m.state is null or m.state =:state )" +
                " and p.id =:periodId " +
                " and su.id =:supervisorId " +
                " and ( " +
                " (m.year <= :currentYear )" +
                " or (m.year = :currentYear and m.monthYearOrder <= :currentMonth )" +
                ") "
                // " order by o.reportingOffice.acronym,  " +
                //  " o.projectStatement.code , o.indicator.code  "
                ;
        Query q = this.entityManager.createQuery(jpql, IndicatorExecution.class);
        q.setParameter("generalType", IndicatorType.GENERAL);
        q.setParameter("state", State.ACTIVO);
        q.setParameter("periodId", periodId);
        q.setParameter("currentMonth", currentMonth);
        q.setParameter("currentYear", currentYear);
        q.setParameter("supervisorId", supervisorId);
        return q.getResultList();
    }

    public List<IndicatorExecution> getOfficeLateDirectImplementationReport(Long periodId, Long officeId, Integer currentYear, Integer currentMonth) {
        String jpql = IndicatorExecutionDao.jpqlDirectImplementationIndicators +
                " WHERE p.id = :periodId " +
                " and o.indicatorType <> :generalType " +
                " and o.project.id is null" +
                " and o.state =:state " +
                " and (q.state is null or q.state =:state )" +
                " and (m.state is null or m.state =:state )" +
                " and p.id =:periodId " +
                " and o.reportingOffice.id =:officeId " +
                " and ( " +
                " (m.year <= :currentYear )" +
                " or (m.year = :currentYear and m.monthYearOrder <= :currentMonth )" +
                ") "
                // " order by o.reportingOffice.acronym,  " +
                //  " o.projectStatement.code , o.indicator.code  "
                ;
        Query q = this.entityManager.createQuery(jpql, IndicatorExecution.class);
        q.setParameter("generalType", IndicatorType.GENERAL);
        q.setParameter("state", State.ACTIVO);
        q.setParameter("periodId", periodId);
        q.setParameter("currentMonth", currentMonth);
        q.setParameter("currentYear", currentYear);
        q.setParameter("officeId", officeId);
        return q.getResultList();
    }
}
