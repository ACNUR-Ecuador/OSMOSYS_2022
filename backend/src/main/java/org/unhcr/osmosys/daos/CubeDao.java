package org.unhcr.osmosys.daos;

import org.jboss.logging.Logger;
import org.unhcr.osmosys.model.cubeDTOs.*;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class CubeDao {
    private static final Logger LOGGER = Logger.getLogger(CubeDao.class);
    @PersistenceContext(unitName = "main-persistence-unit")
    protected EntityManager entityManager;

    private static final String factTable = "SELECT " +
            "* " +
            "from  " +
            "cube.fact_table f";

    private static final String factTableCount = "SELECT " +
            " count(*) " +
            "from  " +
            "cube.fact_table f";

    private static final String tagsTable = "SELECT " +
            "* " +
            "from  " +
            "cube.tags t ";

    private static final String tagIndicatorValues = "SELECT " +
            "* " +
            "from  " +
            "cube.tag_indicator_values t ";

    private static final String projectManagersTable = "SELECT " +
            "* " +
            "from  " +
            "cube.project_managers p ";

    private static final String resultManagersTable = "SELECT " +
            "* " +
            "from  " +
            "cube.result_managers r ";

    private static final String tagIndicators = "SELECT " +
            "* " +
            "from  " +
            "cube.tag_indicators t ";

    private static final String monthQuarterYearTable = "SELECT " +
            "* " +
            "from  " +
            "cube.month_quarter_year t";
    private static final String DissagregationTypeTable = "SELECT " +
            "* " +
            "from  " +
            "cube.dissagregation_type t " ;

    private static final String DiversityTypeTable = "SELECT " +
            "* " +
            "from  " +
            "cube.diversity_type t";
    private static final String AgeTypeTable = "SELECT " +
            "* " +
            "from  " +
            "cube.age_type t";

    private static final String GenderTypeTable = "SELECT " +
            "* " +
            "from  " +
            "cube.gender_type t";
    private static final String CountryOfOriginTypeTable = "SELECT " +
            "* " +
            "from  " +
            "cube.country_of_origin t";
    private static final String PopulationTypeTable = "SELECT " +
            "* " +
            "from  " +
            "cube.population_type t";
    private static final String CantonesProvinciasTable = "SELECT " +
            "* " +
            "from  " +
            "cube.cantones_provincias t";
    private static final String IndicatorTypeTable = "SELECT " +
            "* " +
            "from  " +
            "cube.indicator_type t";
    private static final String UserTable = "SELECT " +
            "* " +
            "from  " +
            "cube.users t";
    private static final String periodTable = "SELECT " +
            "* " +
            "from  " +
            "cube.periods t";
    private static final String projectTable = "SELECT " +
            "* " +
            "from  " +
            "cube.projects t";
    private static final String organizationTable = "SELECT " +
            "* " +
            "from  " +
            "cube.organizations t";
    private static final String officeTable = "SELECT " +
            "* " +
            "from  " +
            "cube.offices t";

    private static final String reportStateTable = "SELECT " +
            "* " +
            "from  " +
            "cube.report_state t";
    private static final String statementTable = "SELECT " +
            "* " +
            "from  " +
            "cube.statements t";
    private static final String monthSourceTable = "SELECT " +
            "* " +
            "from  " +
            "cube.month_source t";
    private static final String monthCualitativeDataTable = "SELECT " +
            "* " +
            "from  " +
            "cube.month_cualitative_data t";
    private static final String indicatorsTable = "SELECT " +
            "* " +
            "from  " +
            "cube.indicators t";
    private static final String dissagregationSimpleTable = "SELECT " +
            "* " +
            "from  " +
            "cube.indicator_execution_dissagregation_simple t";
    private static final String indicatorMainDissagregationTable =
            "SELECT " +
                    "i.id as indicator_id, dai.period_id, dai.dissagregation_type " +
                    "FROM " +
                    "osmosys.indicators i " +
                    "INNER JOIN osmosys.dissagregation_assignation_indicator dai on i.id=dai.indicator_id " +
                    "WHERE dai.state='ACTIVO' " +
                    "ORDER BY dai.period_id, dai.indicator_id, dai.dissagregation_type";

    private static final String generalndicatorMainDissagregationTable =
           "SELECT " +
                   "i.id as indicator_id  , i.periodo_id period_id , dai.dissagregation_type " +
                   "FROM " +
                   "osmosys.general_indicators i " +
                   "INNER JOIN osmosys.dissagregation_assignation_general_indicator dai on i.id=dai.general_indicator_id " +
                   "WHERE dai.state='ACTIVO' " +
                   "ORDER BY  i.periodo_id,  dai.dissagregation_type";

    private static final String customMainDissagregationTable =
           "SELECT " +
                   "cdai.indicator_id as indicator_id, cdai.period_id , cd.name indicatorlabel, 'PERSONALIZADO' AS indicatorType " +
                   "FROM " +
                   "osmosys.custom_dissagregation_assignation_indicator cdai " +
                   "INNER JOIN osmosys.custom_dissagregations cd on cdai.custom_dissagregation_id=cd.id";

    private static final String implementersTable = "SELECT " +
            "* " +
            "from  " +
            "cube.implementers t";
    private static final String customDissagregationTable = "SELECT " +
            "* " +
            "from  " +
            "cube.custom_dissagregation_table t";


    public List<FactDTO> getFactTableByPeriodYear(Integer year) {

        String sql = CubeDao.factTable + " where f.period_year =:year" ;
        Query q = this.entityManager.createNativeQuery(sql, "FactDTOMapping");
        q.setParameter("year", year);
        return q.getResultList();
    }
    public Object getFactTableByPeriodYearText(Integer year) {

        String sql = "SELECT CAST(json_agg(t) as VARCHAR) jsono FROM (SELECT * from cube.fact_table t where t.period_year  =:year  ) t ";
        Query q = this.entityManager.createNativeQuery(sql);
        q.setParameter("year", year);
        return q.getSingleResult();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<FactDTO> getFactTableByPeriodYearPaginated(Integer year, int pageSize, int pageNumber) {
        //this.entityManager.getTransaction().begin();
        String sql = CubeDao.factTable + " where f.period_year =:year order by f.id" ;
        Query q = this.entityManager.createNativeQuery(sql, "FactDTOMapping");
        q.setFirstResult((pageNumber) * pageSize);
        q.setMaxResults(pageSize);
        q.setParameter("year", year);
        //this.entityManager.getTransaction().commit();
        return q.getResultList();
    }
    public long getFactTableCount(Integer year) {

        String sql = CubeDao.factTableCount + " where f.period_year =:year" ;
        Query q = this.entityManager.createNativeQuery(sql);
        q.setParameter("year", year);
        BigInteger r= (BigInteger) q.getSingleResult();
        return r.longValue();
    }

    public List<ProjectManagersDTO> getProjectManagers() {
        String sql = CubeDao.projectManagersTable ;
        Query q = this.entityManager.createNativeQuery(sql, "ProjectManagerDTOMapping");
        return q.getResultList();
    }

    public List<ResultManagersDTO> getResultManagers() {
        String sql = CubeDao.resultManagersTable ;
        Query q = this.entityManager.createNativeQuery(sql, "ResultManagerDTOMapping");
        return q.getResultList();
    }

    public List<TagIndicatorsDTO> getTagIndicatorsByPeriodYear(Integer year) {
        String sql = CubeDao.tagIndicators +  " where t.period_year =:year";
        Query q = this.entityManager.createNativeQuery(sql, "TagIndicatorsDTOMapping");
        q.setParameter("year", year);
        return q.getResultList();
    }

    public List<TagsDTO> getTagTableByPeriodYear(Integer year) {
        String sql = CubeDao.tagsTable +  " where t.period_year =:year";
        Query q = this.entityManager.createNativeQuery(sql, "TagsDTOMapping");
        q.setParameter("year", year);
        return q.getResultList();
    }

    public List<TagIndicatorsDTO> getTagIndicatorsByPeriodYear() {
        String sql = CubeDao.tagIndicators ;
        Query q = this.entityManager.createNativeQuery(sql, "TagIndicatorsDTOMapping");
        return q.getResultList();
    }

    public List<TagsDTO> getTagTableByPeriodYear() {
        String sql = CubeDao.tagsTable ;
        Query q = this.entityManager.createNativeQuery(sql, "TagsDTOMapping");
        return q.getResultList();
    }


    public List<TagIndicatorValuesDTO> getTagIndicatorValues() {
        String sql = CubeDao.tagIndicatorValues ;
        Query q = this.entityManager.createNativeQuery(sql, "TagIndicatorValuesDTOMapping");
        return q.getResultList();
    }

    public List<MonthQuarterYearDTO> getMonthQuarterYearTable() {
        Query q = this.entityManager.createNativeQuery(CubeDao.monthQuarterYearTable, "MonthQuarterYearDTOMapping");
        return q.getResultList();
    }

    public List<DissagregationTypeDTO> getDissagregationTypeTable() {
        Query q = this.entityManager.createNativeQuery(CubeDao.DissagregationTypeTable, "DissagregationTypeDTOMapping");
        return q.getResultList();
    }

    public List<StandardDissagregationOptionDTO> getDiversityTypeTable() {
        Query q = this.entityManager.createNativeQuery(CubeDao.DiversityTypeTable, "StandardDissagregationOptionDTOMapping");
        return q.getResultList();
    }

    public List<StandardDissagregationOptionDTO> getAgeTypeTable() {
        Query q = this.entityManager.createNativeQuery(CubeDao.AgeTypeTable, "StandardDissagregationOptionDTOMapping");
        return q.getResultList();
    }


    public List<StandardDissagregationOptionDTO> getGenderTypeTable() {
        Query q = this.entityManager.createNativeQuery(CubeDao.GenderTypeTable, "StandardDissagregationOptionDTOMapping");
        return q.getResultList();
    }

    public List<StandardDissagregationOptionDTO> getCountryOfOriginTypeTable() {
        Query q = this.entityManager.createNativeQuery(CubeDao.CountryOfOriginTypeTable, "StandardDissagregationOptionDTOMapping");
        return q.getResultList();
    }

    public List<StandardDissagregationOptionDTO> getPopulationTypeTable() {
        Query q = this.entityManager.createNativeQuery(CubeDao.PopulationTypeTable, "StandardDissagregationOptionDTOMapping");
        return q.getResultList();
    }

    public List<CantonesProvinciasDTO> getCantonesProvinciasTable() {
        Query q = this.entityManager.createNativeQuery(CubeDao.CantonesProvinciasTable, "CantonesProvinciasDTOMapping");
        return q.getResultList();
    }

    public List<CantonesProvinciasCentroidsDTO> getCantonesProvinciasCentroidsTable() {
        Query q = this.entityManager.createNativeQuery(CubeDao.CantonesProvinciasTable, "CantonesProvinciasCentroidsDTOMapping");
        return q.getResultList();
    }

    public List<IndicatorTypeDTO> getIndicatorTypeTable() {
        Query q = this.entityManager.createNativeQuery(CubeDao.IndicatorTypeTable, "IndicatorTypeDTOMapping");
        return q.getResultList();
    }

    public List<UserDTO> getUserTable() {
        Query q = this.entityManager.createNativeQuery(CubeDao.UserTable, "UserDTOMapping");
        return q.getResultList();
    }

    public List<PeriodDTO> getPeriodTable() {
        Query q = this.entityManager.createNativeQuery(CubeDao.periodTable, "PeriodDTOMapping");
        return q.getResultList();
    }

    public List<ProjectDTO> getProjectTable() {
        Query q = this.entityManager.createNativeQuery(CubeDao.projectTable, "ProjectDTOMapping");
        return q.getResultList();
    }

    public List<OrganizationDTO> getOrganizationTable() {
        Query q = this.entityManager.createNativeQuery(CubeDao.organizationTable, "OrganizationDTOMapping");
        return q.getResultList();
    }

    public List<OfficeDTO> getOfficeTable() {
        Query q = this.entityManager.createNativeQuery(CubeDao.officeTable, "OfficeDTOMapping");
        return q.getResultList();
    }

    public List<ReportStateDTO> getReportStateTable() {
        Query q = this.entityManager.createNativeQuery(CubeDao.reportStateTable, "ReportStateDTOMapping");
        return q.getResultList();
    }

    public List<StatementDTO> getStatementTable() {
        Query q = this.entityManager.createNativeQuery(CubeDao.statementTable, "StatementDTOMapping");
        return q.getResultList();
    }

    public List<MonthSourceDTO> getMonthSouceTable(Integer year) {
        Query q = this.entityManager.createNativeQuery(CubeDao.monthSourceTable + " where t.year =:year ", "MonthSourceDTOMapping");
        q.setParameter("year", year);
        return q.getResultList();
    }

    public List<MonthCualitativeDataDTO> getMonthCualitativeDataTable(Integer year) {
        Query q = this.entityManager.createNativeQuery(CubeDao.monthCualitativeDataTable + " where t.year =:year ", "MonthCualitativeDataDTOMapping");
        q.setParameter("year", year);
        return q.getResultList();
    }

    public List<IndicatorDTO> getIndicatorsTable() {
        Query q = this.entityManager.createNativeQuery(CubeDao.indicatorsTable, "IndicatorDTOMapping");
        return q.getResultList();
    }

    public List<IndicatorExecutionDissagregationSimpleDTO> getIndicatorExecutionsDissagregationSimpleTable(Integer year) {
        Query q = this.entityManager.createNativeQuery(CubeDao.dissagregationSimpleTable + " where t.year =:year ", "IndicatorExecutionsDissagregationSimpleDTOMapping");
        q.setParameter("year", year);
        return q.getResultList();
    }
    public List<IndicatorMainDissagregationDTO> getIndicatorMainDissagregationDTOTable() {
        Query q = this.entityManager.createNativeQuery(CubeDao.indicatorMainDissagregationTable , "IndicatorMainDissagregationDTOMapping");
        return q.getResultList();
    }
    public List<IndicatorMainDissagregationDTO> getGeneralIndicatorMainDissagregationDTOTable() {
        Query q = this.entityManager.createNativeQuery(CubeDao.generalndicatorMainDissagregationTable , "IndicatorMainDissagregationDTOMapping");
        return q.getResultList();
    }
    public List<IndicatorMainDissagregationDTO> getCustomMainDissagregationDTOTable() {
        Query q = this.entityManager.createNativeQuery(CubeDao.customMainDissagregationTable , "IndicatorMainDissagregationCustomDTOMapping");
        return q.getResultList();
    }
    public List<ImplementerDTO> getImplementersTable() {
        Query q = this.entityManager.createNativeQuery(CubeDao.implementersTable , "ImplementerDTOMapping");
        return q.getResultList();
    }

    public List<CustomDissagregationDTO> getCustomDissagregationsTable() {
        Query q = this.entityManager.createNativeQuery(CubeDao.customDissagregationTable , "CustomDissagregationDTOMapping");
        return q.getResultList();
    }
}
