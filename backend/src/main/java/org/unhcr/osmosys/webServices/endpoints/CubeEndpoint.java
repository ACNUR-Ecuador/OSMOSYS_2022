package org.unhcr.osmosys.webServices.endpoints;

import com.sagatechs.generics.security.annotations.BasicSecured;
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

    @Path("/factTable/{year}")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<FactDTO> getFactTableByPeriodYear(@PathParam("year") Integer year) {
        return this.cubeService.getFactTableByPeriodYear(year);
    }

    @Path("/monthQuarterYearTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<MonthQuarterYearDTO> getMonthQuarterYearTable() {
        return this.cubeService.getMonthQuarterYearTable();
    }

    @Path("/dissagregationTypeTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<DissagregationTypeDTO> getDissagregationTypeTable() {
        return this.cubeService.getDissagregationTypeTable();
    }

    @Path("/diversityTypeTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<DiversityTypeDTO> getDiversityTypeTable() {
        return this.cubeService.getDiversityTypeTable();
    }

    @Path("/ageTypeTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<AgeTypeDTO> getAgeTypeTable() {
        return this.cubeService.getAgeTypeTable();
    }

    @Path("/agePrimaryEducationTypeTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<AgePrimaryEducationTypeDTO> getAgePrimaryEducationTypeTable() {
        return this.cubeService.getAgePrimaryEducationTypeTable();
    }

    @Path("/ageTertiaryEducationTypeTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<AgeTertiaryEducationTypeDTO> getAgeTertiaryEducationTypeTable() {
        return this.cubeService.getAgeTertiaryEducationTypeTable();
    }

    @Path("/genderTypeTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<GenderTypeDTO> getGenderTypeTable() {
        return this.cubeService.getGenderTypeTable();
    }

    @Path("/countryOfOriginTypeTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<CountryOfOriginTypeDTO> getCountryOfOriginTypeTable() {
        return this.cubeService.getCountryOfOriginTypeTable();
    }

    @Path("/populationTypeTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<PopulationTypeDTO> getPopulationTypeTable() {
        return this.cubeService.getPopulationTypeTable();
    }

    @Path("/cantonesProvinciasTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<CantonesProvinciasDTO> getCantonesProvinciasTable() {
        return this.cubeService.getCantonesProvinciasTable();
    }

    @Path("/cantonesProvinciasCentroidsTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<CantonesProvinciasCentroidsDTO> getCantonesProvinciasCentroidsTable() {
        return this.cubeService.getCantonesProvinciasCentroidsTable();
    }

    @Path("/indicatorTypeTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<IndicatorTypeDTO> getIndicatorTypeTable() {
        return this.cubeService.getIndicatorTypeTable();
    }

    @Path("/userTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserDTO> getUserTable() {
        return this.cubeService.getUserTable();
    }

    @Path("/periodTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<PeriodDTO> getPeriodTable() {
        return this.cubeService.getPeriodTable();
    }

    @Path("/projectTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProjectDTO> getProjectTable() {
        return this.cubeService.getProjectTable();
    }

    @Path("/organizationTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganizationDTO> getOrganizationTable() {
        return this.cubeService.getOrganizationTable();
    }

    @Path("/officeTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<OfficeDTO> getOfficeTable() {
        return this.cubeService.getOfficeTable();
    }

    @Path("/reportStateTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReportStateDTO> getReportStateTable() {
        return this.cubeService.getReportStateTable();
    }

    @Path("/statementTable")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<StatementDTO> getStatementTable() {
        return this.cubeService.getStatementTable();
    }

    @Path("/monthSourceTable/{year}")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<MonthSourceDTO> getMonthSouceTable(@PathParam("year") Integer year) {
        return this.cubeService.getMonthSouceTable(year);
    }

    @Path("/monthCualitativeDataTable/{year}")
    @GET
    @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<MonthCualitativeDataDTO> getMonthCualitativeDataTable(@PathParam("year") Integer year) {
        return this.cubeService.getMonthCualitativeDataTable(year);
    }

    @Path("/indicatorsTable")
    @GET
    // @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<IndicatorDTO> getIndicatorsTable() {
        return this.cubeService.getIndicatorsTable();
    }

    @Path("/indicatorExecutionsDissagregationSimpleTable/{year}")
    @GET
    // @BasicSecured
    @Produces(MediaType.APPLICATION_JSON)
    public List<IndicatorExecutionDissagregationSimpleDTO> getIndicatorExecutionsDissagregationSimpleTable(@PathParam("year") Integer year) {
        return this.cubeService.getIndicatorExecutionsDissagregationSimpleTable(year);
    }

}
