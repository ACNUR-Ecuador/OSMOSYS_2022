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

    private static final String detailedIndicatorExecutions =
            "SELECT " +
                    "ie.id as ie_id, " +
                    "CASE  " +
                    "  WHEN ie.project_id is null THEN  'Implementación Directa' " +
                    "  WHEN ie.project_id is not null THEN  'Implementación Socios' " +
                    "END AS implementation_type, " +
                    "ar.code || ' - '||  ar.short_description as area, " +
                    "st.code || ' - '|| st.description as statement, " +
                    "stb.code || ' - '|| stb.description as statement_project, " +
                    "CASE " +
                    "  WHEN i.id is null THEN 'General' " +
                    "  WHEN i.id is not null THEN 'Producto'  " +
                    "END as indicator_type, " +
                    "CASE " +
                    "  WHEN i.id is null THEN gi.description " +
                    "  WHEN i.id is not null THEN i.code || ' - '|| i.description  " +
                    "END as indicator, " +
                    "i.category, " +
                    "i.frecuency, " +
                    "COALESCE (offf.acronym, org.acronym) implementers, " +
                    "ie.total_execution, " +
                    "ie.target, " +
                    "ie.execution_percentage, " +
                    "q.order_ quarter_order, " +
                    "q.quarter, " +
                    "q.total_execution quarter_execution, " +
                    "q.target quarter_target, " +
                    "q.execution_percentage quarter_percentage, " +
                    "mo.order_ month_order, " +
                    "mo.month, " +
                    "mo.total_execution month_execution, " +
                    "values_d.iv_id, " +
                    "values_d.ivc_id, " +
                    "values_d.dissagregation_type, " +
                    "values_d.dissagregation_level1, " +
                    "values_d.dissagregation_level2, " +
                    "values_d.value " +
                    "FROM " +
                    "osmosys.indicator_executions ie " +
                    "LEFT JOIN osmosys.indicators i on ie.performance_indicator_id=i.id " +
                    "LEFT JOIN osmosys.statements stb on ie.project_statement_id= stb.id " +
                    "LEFT JOIN osmosys.statements st on i.statement_id = st.id " +
                    "LEFT JOIN osmosys.areas ar on st.area_id=ar.id " +
                    "LEFT JOIN osmosys.offices offf on ie.reporting_office_id=offf.id " +
                    "LEFT JOIN osmosys.projects pr on ie.project_id=pr.id " +
                    "LEFT JOIN osmosys.organizations org on pr.organization_id=org.id " +
                    "LEFT JOIN osmosys.periods per on ie.period_id=per.id " +
                    "LEFT JOIN osmosys.general_indicators gi on per.id=gi.periodo_id " +
                    "LEFT JOIN osmosys.quarters q on ie.id=q.indicator_execution_id " +
                    "LEFT JOIN osmosys.months mo on q.id=mo.quarter_id " +
                    "INNER JOIN ( " +
                    "SELECT " +
                    "* " +
                    "FROM " +
                    "( " +
                    "(SELECT " +
                    "ie.id ie_id, " +
                    "mo.id month_id, " +
                    "null as iv_id, " +
                    "ivc.id as ivc_id, " +
                    "cd.description as dissagregation_type, " +
                    "cdo.name as dissagregation_level1, " +
                    "null as dissagregation_level2, " +
                    "ivc.value as value " +
                    "FROM " +
                    "osmosys.indicator_executions ie " +
                    "INNER JOIN osmosys.quarters q on ie.id=q.indicator_execution_id " +
                    "INNER JOIN osmosys.months mo on q.id=mo.quarter_id " +
                    "INNER JOIN osmosys.indicator_values_custom_dissagregation ivc on mo.id=ivc.month_id " +
                    "INNER JOIN osmosys.custom_dissagregation_options cdo on ivc.custom_dissagregation_option=cdo.id " +
                    "INNER JOIN osmosys.custom_dissagregations cd on cdo.custom_dissagregation_id=cd.id " +
                    "WHERE ie.state='ACTIVO'  " +
                    "AND q.state='ACTIVO'  " +
                    "AND mo.state='ACTIVO'  " +
                    "AND ivc.state='ACTIVO' ) " +
                    "UNION " +
                    "( " +
                    "SELECT " +
                    "ie.id ie_id, " +
                    "mo.id month_id, " +
                    "iv.id as iv_id, " +
                    "null as ivc_id, " +
                    "iv.dissagregation_type, " +
                    "COALESCE(can.code||'-'||can.description,COALESCE(iv.population_type,COALESCE(iv.diversity_type,COALESCE(iv.gender_type,COALESCE(iv.age_type,iv.country_of_origin))))) as dissagregation_level1, " +
                    "prov.code||'-'||prov.description as dissagregation_level2, " +
                    "iv.value as value " +
                    "FROM " +
                    "osmosys.indicator_executions ie " +
                    "INNER JOIN osmosys.quarters q on ie.id=q.indicator_execution_id " +
                    "INNER JOIN osmosys.months mo on q.id=mo.quarter_id " +
                    "INNER JOIN osmosys.indicator_values iv on mo.id=iv.month_id " +
                    "LEFT JOIN osmosys.cantones can on iv.canton_id=can.id " +
                    "LEFT JOIN osmosys.provincias prov on can.provincia_id=prov.id " +
                    "WHERE ie.state='ACTIVO'  " +
                    "AND q.state='ACTIVO'  " +
                    "AND mo.state='ACTIVO'  " +
                    "AND iv.state='ACTIVO' ) " +
                    ") as s " +
                    "ORDER BY 1,2,5,6,7 " +
                    ") as values_d on mo.id=values_d.month_id " +
                    "WHERE ie.state='ACTIVO' AND q.state='ACTIVO' AND mo.state='ACTIVO' ";

    private static final String detailedIndicatorExecutionsOrder = " ORDER BY ar.id,1,2,3,4,5,6,7,8,9,15,16,17,18,19,20,23,24,25 ";

    public List<IndicatorExecutionDetailedDTO> getAllIndicatorExecutionDetailed(Long periodId) {

        String sql = ReportDao.detailedIndicatorExecutions
                + " AND ie.period_id= :periodId ";
        sql += ReportDao.detailedIndicatorExecutionsOrder;
        Query q = this.entityManager.createNativeQuery(sql, "IndicatorExecutionDetailedMapping");
        q.setParameter("periodId", periodId);
        return q.getResultList();
    }

    public List<IndicatorExecutionDetailedDTO> getAllPerformanceIndicatorsIndicatorExecutionDetailed(Long periodId) {
        String sql = ReportDao.detailedIndicatorExecutions
                + " AND ie.performance_indicator_id is not null "
                + " AND ie.period_id= :periodId ";
        sql += ReportDao.detailedIndicatorExecutionsOrder;
        Query q = this.entityManager.createNativeQuery(sql, "IndicatorExecutionDetailedMapping");
        q.setParameter("periodId", periodId);
        return q.getResultList();
    }
    public List<IndicatorExecutionDetailedDTO> getPartnersIndicatorsExecutionsDetailedByPeriodId(Long periodId) {

        String sql = ReportDao.detailedIndicatorExecutions
                + " AND ie.project_id is not null "
                + " AND ie.period_id= :periodId ";
        sql += ReportDao.detailedIndicatorExecutionsOrder;
        Query q = this.entityManager.createNativeQuery(sql, "IndicatorExecutionDetailedMapping");
        q.setParameter("periodId", periodId);
        return q.getResultList();
    }
    public List<IndicatorExecutionDetailedDTO> getPartnersGeneralIndicatorsDetailedByPeriodId(Long periodId) {

        String sql = ReportDao.detailedIndicatorExecutions
                + " AND ie.project_id is not null "
                + " AND ie.performance_indicator_id is null "
                + " AND ie.period_id= :periodId ";
        sql += ReportDao.detailedIndicatorExecutionsOrder;
        Query q = this.entityManager.createNativeQuery(sql, "IndicatorExecutionDetailedMapping");
        q.setParameter("periodId", periodId);
        return q.getResultList();
    }
    public List<IndicatorExecutionDetailedDTO> getPartnersPerformanceIndicatorsDetailedByPeriodId(Long periodId) {

        String sql = ReportDao.detailedIndicatorExecutions
                + " AND ie.project_id is not null "
                + " AND ie.performance_indicator_id is not null "
                + " AND ie.period_id= :periodId ";
        sql += ReportDao.detailedIndicatorExecutionsOrder;
        Query q = this.entityManager.createNativeQuery(sql, "IndicatorExecutionDetailedMapping");
        q.setParameter("periodId", periodId);
        return q.getResultList();
    }
    public List<IndicatorExecutionDetailedDTO> getDirectImplementationPerformanceIndicatorsDetailedByPeriodId(Long periodId) {
        String sql = ReportDao.detailedIndicatorExecutions
                + " AND ie.project_id is null "
                + " AND ie.performance_indicator_id is not null "
                + " AND ie.period_id= :periodId ";
        sql += ReportDao.detailedIndicatorExecutionsOrder;
        Query q = this.entityManager.createNativeQuery(sql, "IndicatorExecutionDetailedMapping");
        q.setParameter("periodId", periodId);
        return q.getResultList();
    }
}
