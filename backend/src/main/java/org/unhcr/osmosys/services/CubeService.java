package org.unhcr.osmosys.services;

import org.unhcr.osmosys.daos.CubeDao;
import org.unhcr.osmosys.model.cubeDTOs.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class CubeService {

    @Inject
    CubeDao cubeDao;

    public List<FactDTO> getFactTableByPeriodYear(Integer periodYear) {
        return this.cubeDao.getFactTableByPeriodYear(periodYear);
    }

    public List<MonthQuarterYearDTO> getMonthQuarterYearTable() {
        return this.cubeDao.getMonthQuarterYearTable();
    }

    public List<DissagregationTypeDTO> getDissagregationTypeTable() {
        return this.cubeDao.getDissagregationTypeTable();
    }

    public List<DiversityTypeDTO> getDiversityTypeTable() {
        return this.cubeDao.getDiversityTypeTable();
    }

    public List<AgeTypeDTO> getAgeTypeTable() {
        return this.cubeDao.getAgeTypeTable();
    }

    public List<AgePrimaryEducationTypeDTO> getAgePrimaryEducationTypeTable() {
        return this.cubeDao.getAgePrimaryEducationTypeTable();
    }
    public List<AgeTertiaryEducationTypeDTO> getAgeTertiaryEducationTypeTable() {
        return this.cubeDao.getAgeTertiaryEducationTypeTable();
    }

    public List<GenderTypeDTO> getGenderTypeTable() {
        return this.cubeDao.getGenderTypeTable();
    }

    public List<CountryOfOriginTypeDTO> getCountryOfOriginTypeTable() {
        return this.cubeDao.getCountryOfOriginTypeTable();
    }

    public List<PopulationTypeDTO> getPopulationTypeTable() {
        return this.cubeDao.getPopulationTypeTable();
    }

    public List<CantonesProvinciasDTO> getCantonesProvinciasTable() {
        return this.cubeDao.getCantonesProvinciasTable();
    }

    public List<IndicatorTypeDTO> getIndicatorTypeTable() {
        return this.cubeDao.getIndicatorTypeTable();
    }

    public List<UserDTO> getUserTable() {
        return this.cubeDao.getUserTable();
    }
    public List<PeriodDTO> getPeriodTable() {
        return this.cubeDao.getPeriodTable();
    }
    public List<ProjectDTO> getProjectTable() {
        return this.cubeDao.getProjectTable();
    }
    public List<OrganizationDTO> getOrganizationTable() {
        return this.cubeDao.getOrganizationTable();
    }
    public List<OfficeDTO> getOfficeTable() {
        return this.cubeDao.getOfficeTable();
    }
    public List<ReportStateDTO> getReportStateTable() {
        return this.cubeDao.getReportStateTable();
    }
    public List<StatementDTO> getStatementTable() {
        return this.cubeDao.getStatementTable();
    }
    public List<MonthSourceDTO> getMonthSouceTable(Integer year) {
        return this.cubeDao.getMonthSouceTable(year);
    }
    public List<MonthCualitativeDataDTO> getMonthCualitativeDataTable(Integer year) {
        return this.cubeDao.getMonthCualitativeDataTable(year);
    }
    public List<IndicatorDTO> getIndicatorsTable() {
        List<IndicatorDTO> indicators = new ArrayList<>();
        IndicatorDTO generalIndicator = new IndicatorDTO(0L,"000000","# total de beneficiarios",null,"MENSUAL",null);
        indicators.add(generalIndicator);
        indicators.addAll(this.cubeDao.getIndicatorsTable());
        return indicators;
    }
}
