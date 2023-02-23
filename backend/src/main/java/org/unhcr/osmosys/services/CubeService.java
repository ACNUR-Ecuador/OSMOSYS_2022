package org.unhcr.osmosys.services;

import org.apache.commons.collections4.CollectionUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.CubeDao;
import org.unhcr.osmosys.model.cubeDTOs.*;
import org.unhcr.osmosys.model.enums.DissagregationType;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Stateless
public class CubeService {
    private static final Logger LOGGER = Logger.getLogger(CubeService.class);
    @Inject
    CubeDao cubeDao;

    @Inject
    CubeServiceAsync cubeServiceAsync;

    public List<FactDTO> getFactTableByPeriodYear(Integer periodYear) {
        long lStartTime = System.nanoTime();
        LOGGER.info("start getFactTableByPeriodYear dao: ");
        List<FactDTO> r = this.cubeDao.getFactTableByPeriodYear(periodYear);
        LOGGER.info("end getFactTableByPeriodYear dao: ");
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds getFactTableByPeriodYear dao: " + (lEndTime - lStartTime) / 1000000000);
        return r;
    }

    public List<FactDTO> getFactTablePaginatedByPeriodYear(Integer periodYear, int pageSize) {
        List<FactDTO> r = new ArrayList<>();
        LOGGER.info("start getFactTableByPeriodYear count: ");
        long countResults = this.cubeDao.getFactTableCount(periodYear);
        int lastPageNumber = (int) (Math.ceil(countResults / pageSize));
        LOGGER.info("start getFactTableByPeriodYear count: " + countResults);
        LOGGER.info("start getFactTableByPeriodYear pages: " + lastPageNumber);
        long lStartTime = System.nanoTime();
        LOGGER.info("start getFactTableByPeriodYear dao: ");
        for (int i = 0; i <= lastPageNumber; i++) {

            LOGGER.info("pagen: " + i);
            List<FactDTO> rp = this.cubeDao.getFactTableByPeriodYearPaginated(periodYear, pageSize, i);
            r.addAll(rp);
        }

        LOGGER.info("end getFactTableByPeriodYear dao: ");
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds getFactTableByPeriodYear dao: " + (lEndTime - lStartTime) / 1000000000);
        LOGGER.info("total result: " + r.size());
        return r;
    }

    public List<FactDTO> getFactTablePageByPeriodYear(Integer periodYear, int pageSize, int page) {

        LOGGER.info("page: " + page + " pageSize: " + pageSize);
        long lStartTime = System.nanoTime();
        List<FactDTO> rp = this.cubeDao.getFactTableByPeriodYearPaginated(periodYear, pageSize, page);

        long lEndTime = System.nanoTime();
        LOGGER.info("end getFactTablePAgeByPeriodYear dao: ");
        LOGGER.info("Elapsed time in seconds getFactTablePageByPeriodYear dao: " + (lEndTime - lStartTime) / 1000000000);
        LOGGER.info("total result: " + rp.size());
        return rp;
    }

    public Long getFactTableCountByPeriodYear(Integer periodYear) {

        LOGGER.info("count fact year: " + periodYear);
        long lStartTime = System.nanoTime();
        Long count = this.cubeDao.getFactTableCount(periodYear);

        long lEndTime = System.nanoTime();
        LOGGER.info("end getFactTablePAgeByPeriodYear dao: ");
        LOGGER.info("Elapsed time in seconds getFactTablePageByPeriodYear dao: " + (lEndTime - lStartTime) / 1000000000);
        LOGGER.info("total result: " + count);
        return count;
    }


    public List<FactDTO> getFactTablePaginatedByPeriodYearAsync(Integer periodYear, int pageSize) throws ExecutionException, InterruptedException {
        List<FactDTO> r = new ArrayList<>();

        int threads = 4;
        LOGGER.info("start getFactTableByPeriodYear count: ");
        long countResults = this.cubeDao.getFactTableCount(periodYear);
        int lastPageNumber = (int) (Math.ceil(countResults / pageSize));
        LOGGER.info("start getFactTableByPeriodYear count: " + countResults);
        LOGGER.info("start getFactTableByPeriodYear pages: " + lastPageNumber);
        long lStartTime = System.nanoTime();
        LOGGER.info("start getFactTableByPeriodYear dao: ");
        for (int i = 0; i <= lastPageNumber; ) {

            List<Future<List<FactDTO>>> thrds = new ArrayList<>();
            int th = 0;
            while (th < threads && i <= lastPageNumber) {
                Future<List<FactDTO>> thf = this.cubeServiceAsync.getFactTablePageByPeriodYearAsync(periodYear, pageSize, i);
                thrds.add(thf);
                th++;
                i++;
            }

            while (thrds.stream().filter(listFuture -> !listFuture.isDone()).count() == 0) {
                try {
                    Thread.sleep(100);
                    // LOGGER.info("waiting: "+i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            for (Future<List<FactDTO>> thrd : thrds) {
                r.addAll(thrd.get());
            }
            LOGGER.info("end getFactTableByPeriodYear dao: " + r.size());

        }

        LOGGER.info("end getFactTableByPeriodYear dao: ");
        long lEndTime = System.nanoTime();
        LOGGER.info("Elapsed time in seconds getFactTableByPeriodYear dao: " + (lEndTime - lStartTime) / 1000000000);
        LOGGER.info("total result: " + r.size());
        return r;
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

    public List<CantonesProvinciasCentroidsDTO> getCantonesProvinciasCentroidsTable() {
        return this.cubeDao.getCantonesProvinciasCentroidsTable();
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
        IndicatorDTO generalIndicator = new IndicatorDTO(0L, "000000", "# total de beneficiarios", null, "MENSUAL", null);
        indicators.add(generalIndicator);
        indicators.addAll(this.cubeDao.getIndicatorsTable());
        return indicators;
    }

    public List<IndicatorExecutionDissagregationSimpleDTO> getIndicatorExecutionsDissagregationSimpleTable(Integer year) {
        return this.cubeDao.getIndicatorExecutionsDissagregationSimpleTable(year);
    }


    public List<IndicatorMainDissagregationDTO> getIndicatorMainDissagregationDTOTable() {
        List<IndicatorMainDissagregationDTO> r = this.cubeDao.getIndicatorMainDissagregationDTOTable();

        Map<Long, Map<Long, List<IndicatorMainDissagregationDTO>>> grouped = r.stream()
                .collect(
                        Collectors.groupingBy(IndicatorMainDissagregationDTO::getPeriod_id,
                                Collectors.groupingBy(IndicatorMainDissagregationDTO::getIndicator_id))
                );

        LOGGER.debug(" by getPeriod_id");
        for (Map.Entry<Long, Map<Long, List<IndicatorMainDissagregationDTO>>> entry : grouped.entrySet()) {
            LOGGER.debug("getPeriod_id: " + entry.getKey());
            LOGGER.debug(" by indicator_id");

            for (Map.Entry<Long, List<IndicatorMainDissagregationDTO>> entry1 : entry.getValue().entrySet()) {
                LOGGER.debug(" by indicator_id" + entry1.getKey());
                if(entry1.getKey()==67){
                    LOGGER.debug("este");
                }
                this.setMainDissagregation(entry1.getValue());
            }
        }
        return r;

        /*Map<Long, List<IndicatorExecutionDissagregationSimpleDTO>> groupedByIeId =
                r.stream().collect(Collectors.groupingBy(IndicatorExecutionDissagregationSimpleDTO::getIe_id));

        for (Map.Entry<Long, List<IndicatorExecutionDissagregationSimpleDTO>> entry : groupedByIeId.entrySet()) {
            DissagregationType mainDissagregationType = this.setMainDissagregation(entry.getValue());
            LOGGER.debug(entry.getKey() + "-" + mainDissagregationType);
        }*/
    }

    private DissagregationType setMainDissagregation(List<IndicatorMainDissagregationDTO> ies) {
        DissagregationType mainDissagregation = null;
        Set<DissagregationType> dissagregations = ies.stream().map(IndicatorMainDissagregationDTO::getDissagregation_type).collect(Collectors.toSet());

        List<DissagregationType> notDiversityDissagregations = dissagregations.stream()
                .filter(dissagregationType -> !dissagregationType.getStringValue().contains("DIVERSIDAD"))
                .collect(Collectors.toList());

        if (notDiversityDissagregations.size() < 2) {
            Optional<DissagregationType> mainDissagregationOptional = notDiversityDissagregations.stream().findFirst();
            if(mainDissagregationOptional.isPresent()){
                mainDissagregation=mainDissagregationOptional.get();
            }else {
                mainDissagregation=dissagregations.stream().findFirst().get();
            }
        } else {
            List<DissagregationType> mainDissagregationLugarList = notDiversityDissagregations.stream()
                    .filter(dissagregationType -> dissagregationType.getStringValue()
                            .contains("LUGAR"))
                    .collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(mainDissagregationLugarList)){
               mainDissagregation= mainDissagregationLugarList.stream().findFirst().get();
            }else {
                mainDissagregation = notDiversityDissagregations.stream().findFirst().get();
            }
        }
        for (IndicatorMainDissagregationDTO dto : ies) {
            if (dto.getDissagregation_type().equals(mainDissagregation)) {
                dto.setMainDissagregation(Boolean.TRUE);
            } else {
                dto.setMainDissagregation(Boolean.FALSE);
            }
        }

        return mainDissagregation;
    }

    public List<ImplementerDTO> getImplementersTable() {
        return this.cubeDao.getImplementersTable();
    }

}
