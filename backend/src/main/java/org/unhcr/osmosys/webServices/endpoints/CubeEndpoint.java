package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.security.annotations.BasicSecured;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.model.cubeDTOs.*;
import org.unhcr.osmosys.services.CubeService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/cube")
@RequestScoped
public class CubeEndpoint {

    @Inject
    CubeService cubeService;
    private static final Logger LOGGER = Logger.getLogger(CubeEndpoint.class);

    @Path("/factTable/{year}")
    @GET
    //@Compress
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<FactDTO> getFactTableByPeriodYear(@PathParam("year") Integer year) {
        long lStartTime = System.nanoTime();
        List<FactDTO> r = this.cubeService.getFactTablePaginatedByPeriodYear(year, 100000);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds factTable: " + (lEndTime - lStartTime) / 1000000000);
        System.gc();
        return r;
    }

    @Path("/monthQuarterYearTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<MonthQuarterYearDTO> getMonthQuarterYearTable() {
        long lStartTime = System.nanoTime();
        List<MonthQuarterYearDTO> r = this.cubeService.getMonthQuarterYearTable();
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds monthQuarterYearTable: " + (lEndTime - lStartTime) / 1000000000);
        return r;
    }

    @Path("/dissagregationTypeTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<DissagregationTypeDTO> getDissagregationTypeTable() {
        long lStartTime = System.nanoTime();
        List<DissagregationTypeDTO> r = this.cubeService.getDissagregationTypeTable();
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds dissagregationTypeTable: " + (lEndTime - lStartTime) / 1000000000);
        return r;
    }

    @Path("/diversityTypeTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<DiversityTypeDTO> getDiversityTypeTable() {
        long lStartTime = System.nanoTime();
        List<DiversityTypeDTO> r = this.cubeService.getDiversityTypeTable();
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds diversityTypeTable: " + (lEndTime - lStartTime) / 1000000000);
        return r;
    }

    @Path("/ageTypeTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<AgeTypeDTO> getAgeTypeTable() {
        long lStartTime = System.nanoTime();
        List<AgeTypeDTO> r = this.cubeService.getAgeTypeTable();
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds ageTypeTable: " + (lEndTime - lStartTime) / 1000000000);
        return r;
    }

    @Path("/agePrimaryEducationTypeTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<AgePrimaryEducationTypeDTO> getAgePrimaryEducationTypeTable() {
        long lStartTime = System.nanoTime();
        List<AgePrimaryEducationTypeDTO> r = this.cubeService.getAgePrimaryEducationTypeTable();
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds agePrimaryEducationTypeTable: " + (lEndTime - lStartTime) / 1000000000);
        return r;
    }

    @Path("/ageTertiaryEducationTypeTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<AgeTertiaryEducationTypeDTO> getAgeTertiaryEducationTypeTable() {
        long lStartTime = System.nanoTime();
        List<AgeTertiaryEducationTypeDTO> r = this.cubeService.getAgeTertiaryEducationTypeTable();
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds ageTertiaryEducationTypeTable: " + (lEndTime - lStartTime) / 1000000000);
        return r;
    }

    @Path("/genderTypeTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<GenderTypeDTO> getGenderTypeTable() {
        long lStartTime = System.nanoTime();
        List<GenderTypeDTO> r = this.cubeService.getGenderTypeTable();
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds genderTypeTable: " + (lEndTime - lStartTime) / 1000000000);
        return r;
    }

    @Path("/countryOfOriginTypeTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<CountryOfOriginTypeDTO> getCountryOfOriginTypeTable() {
        long lStartTime = System.nanoTime();
        List<CountryOfOriginTypeDTO> r = this.cubeService.getCountryOfOriginTypeTable();
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds countryOfOriginTypeTable: " + (lEndTime - lStartTime) / 1000000000);
        return r;
    }

    @Path("/populationTypeTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<PopulationTypeDTO> getPopulationTypeTable() {
        long lStartTime = System.nanoTime();
        List<PopulationTypeDTO> r = this.cubeService.getPopulationTypeTable();
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds populationTypeTable: " + (lEndTime - lStartTime) / 1000000000);
        return r;
    }

    @Path("/cantonesProvinciasTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<CantonesProvinciasDTO> getCantonesProvinciasTable() {
        long lStartTime = System.nanoTime();
        List<CantonesProvinciasDTO> r = this.cubeService.getCantonesProvinciasTable();
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds cantonesProvinciasTable: " + (lEndTime - lStartTime) / 1000000000);
        return r;
    }

    @Path("/cantonesProvinciasCentroidsTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<CantonesProvinciasCentroidsDTO> getCantonesProvinciasCentroidsTable() {
        long lStartTime = System.nanoTime();
        List<CantonesProvinciasCentroidsDTO> r = this.cubeService.getCantonesProvinciasCentroidsTable();
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds cantonesProvinciasCentroidsTable: " + (lEndTime - lStartTime) / 1000000000);
        return r;
    }

    @Path("/indicatorTypeTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<IndicatorTypeDTO> getIndicatorTypeTable() {
        long lStartTime = System.nanoTime();
        List<IndicatorTypeDTO> r = this.cubeService.getIndicatorTypeTable();
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds indicatorTypeTable: " + (lEndTime - lStartTime) / 1000000000);
        return r;
    }

    @Path("/userTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserDTO> getUserTable() {
        long lStartTime = System.nanoTime();
        List<UserDTO> r = this.cubeService.getUserTable();
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds userTable: " + (lEndTime - lStartTime) / 1000000000);
        return r;
    }

    @Path("/periodTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<PeriodDTO> getPeriodTable() {
        long lStartTime = System.nanoTime();
        List<PeriodDTO> r = this.cubeService.getPeriodTable();
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds periodTable: " + (lEndTime - lStartTime) / 1000000000);
        return r;
    }

    @Path("/projectTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProjectDTO> getProjectTable() {
        long lStartTime = System.nanoTime();
        List<ProjectDTO> r = this.cubeService.getProjectTable();
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds projectTable: " + (lEndTime - lStartTime) / 1000000000);
        return r;
    }

    @Path("/organizationTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganizationDTO> getOrganizationTable() {
        long lStartTime = System.nanoTime();
        List<OrganizationDTO> r = this.cubeService.getOrganizationTable();
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds organizationTable: " + (lEndTime - lStartTime) / 1000000000);
        return r;
    }

    @Path("/officeTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<OfficeDTO> getOfficeTable() {
        long lStartTime = System.nanoTime();
        List<OfficeDTO> r = this.cubeService.getOfficeTable();
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds officeTable: " + (lEndTime - lStartTime) / 1000000000);
        return r;
    }

    @Path("/reportStateTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReportStateDTO> getReportStateTable() {
        long lStartTime = System.nanoTime();
        List<ReportStateDTO> r = this.cubeService.getReportStateTable();

        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds reportStateTable: " + (lEndTime - lStartTime) / 1000000000);
        return r;
    }

    @Path("/statementTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<StatementDTO> getStatementTable() {
        long lStartTime = System.nanoTime();
        List<StatementDTO> r = this.cubeService.getStatementTable();
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds statementTable: " + (lEndTime - lStartTime) / 1000000000);
        return r;
    }

    @Path("/monthSourceTable/{year}")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<MonthSourceDTO> getMonthSouceTable(@PathParam("year") Integer year) {
        long lStartTime = System.nanoTime();
        List<MonthSourceDTO> r = this.cubeService.getMonthSouceTable(year);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds monthSourceTable: " + (lEndTime - lStartTime) / 1000000000);
        return r;
    }

    @Path("/monthCualitativeDataTable/{year}")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<MonthCualitativeDataDTO> getMonthCualitativeDataTable(@PathParam("year") Integer year) {
        long lStartTime = System.nanoTime();
        List<MonthCualitativeDataDTO> r = this.cubeService.getMonthCualitativeDataTable(year);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds monthCualitativeDataTable: " + (lEndTime - lStartTime) / 1000000000);
        return r;
    }

    @Path("/indicatorsTable")
    @GET
    // @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<IndicatorDTO> getIndicatorsTable() {
        long lStartTime = System.nanoTime();
        List<IndicatorDTO> r = this.cubeService.getIndicatorsTable();
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds indicatorsTable: " + (lEndTime - lStartTime) / 1000000000);
        return r;
    }

    @Path("/indicatorExecutionsDissagregationSimpleTable/{year}")
    @GET
    // @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<IndicatorExecutionDissagregationSimpleDTO> getIndicatorExecutionsDissagregationSimpleTable(@PathParam("year") Integer year) {
        long lStartTime = System.nanoTime();
        List<IndicatorExecutionDissagregationSimpleDTO> r = this.cubeService.getIndicatorExecutionsDissagregationSimpleTable(year);
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds indicatorExecutionsDissagregationSimpleTable: " + (lEndTime - lStartTime) / 1000000000);
        return r;
    }
    @Path("/implementersTable")
    @GET
    // @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<ImplementerDTO> getImplementersTable() {
        long lStartTime = System.nanoTime();
        List<ImplementerDTO> r = this.cubeService.getImplementersTable();
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds getImplementersTable: " + (lEndTime - lStartTime) / 1000000000);
        return r;
    }

}
