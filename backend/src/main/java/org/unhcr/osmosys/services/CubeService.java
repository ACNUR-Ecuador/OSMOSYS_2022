package org.unhcr.osmosys.services;

import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.CubeDao;
import org.unhcr.osmosys.model.cubeDTOs.*;
import org.unhcr.osmosys.model.enums.DissagregationType;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public Object getFactTableByPeriodYearText(Integer periodYear) {
        long lStartTime = System.nanoTime();
        LOGGER.info("start getFactTableByPeriodYear dao: ");
        Object r = this.cubeDao.getFactTableByPeriodYearText(periodYear);
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

            while (thrds.stream().noneMatch(listFuture -> !listFuture.isDone())) {
                try {
                    Thread.sleep(100);
                    // LOGGER.info("waiting: "+i);
                } catch (InterruptedException e) {
                    LOGGER.error("Error en getFactTablePaginatedByPeriodYearAsync");
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

    public List<ProjectManagersDTO> getProjectManagers() {
        List<ProjectManagersDTO> projectManagersDTOS = this.cubeDao.getProjectManagers();
        return projectManagersDTOS;
    }

    public List<ResultManagersDTO> getResultManagers() {
        List<ResultManagersDTO>  resultManagersDTOS = this.cubeDao.getResultManagers();
        return resultManagersDTOS;
    }


    public List<TagIndicatorsDTO> getTagIndicatorsByPeriodYear(Integer periodYear) {
        List<TagIndicatorsDTO> tagIndicatorsDTOS = this.cubeDao.getTagIndicatorsByPeriodYear(periodYear);
        return tagIndicatorsDTOS;
    }

    public List<TagsDTO> getTagTableByPeriodYear(Integer periodYear) {
        List<TagsDTO> tagsDTOS = this.cubeDao.getTagTableByPeriodYear(periodYear);
        return tagsDTOS;
    }

    public List<TagIndicatorsDTO> getTagIndicatorsByPeriodYear() {
        List<TagIndicatorsDTO> tagIndicatorsDTOS = this.cubeDao.getTagIndicatorsByPeriodYear();
        return tagIndicatorsDTOS;
    }

    public List<TagsDTO> getTagTableByPeriodYear() {
        List<TagsDTO> tagsDTOS = this.cubeDao.getTagTableByPeriodYear();
        return tagsDTOS;
    }


  public List<TagIndicatorValuesDTO> getTagIndicatorValues() {
        List<TagIndicatorValuesDTO> tagIndicatorValuesDTOS = this.cubeDao.getTagIndicatorValues();
        return tagIndicatorValuesDTOS;
    }



    public List<MonthQuarterYearDTO> getMonthQuarterYearTable() {
        return this.cubeDao.getMonthQuarterYearTable();
    }

    public List<DissagregationTypeDTO> getDissagregationTypeTable() {
        return this.cubeDao.getDissagregationTypeTable();
    }

    public List<StandardDissagregationOptionDTO> getDiversityTypeTable() {
        return this.cubeDao.getDiversityTypeTable();
    }

    public List<StandardDissagregationOptionDTO> getAgeTypeTable() {
        return this.cubeDao.getAgeTypeTable();
    }


    public List<StandardDissagregationOptionDTO> getGenderTypeTable() {
        return this.cubeDao.getGenderTypeTable();
    }

    public List<StandardDissagregationOptionDTO> getCountryOfOriginTypeTable() {
        return this.cubeDao.getCountryOfOriginTypeTable();
    }

    public List<StandardDissagregationOptionDTO> getPopulationTypeTable() {
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
        List<IndicatorMainDissagregationDTO> performanceIndicators = this.cubeDao.getIndicatorMainDissagregationDTOTable();

        Map<Long, Map<Long, List<IndicatorMainDissagregationDTO>>> grouped = performanceIndicators.stream()
                .collect(
                        Collectors.groupingBy(IndicatorMainDissagregationDTO::getPeriod_id,
                                Collectors.groupingBy(IndicatorMainDissagregationDTO::getIndicator_id))
                );


        for (Map.Entry<Long, Map<Long, List<IndicatorMainDissagregationDTO>>> entry : grouped.entrySet()) {
            LOGGER.debug("getPeriod_id: " + entry.getKey());
            LOGGER.debug(" by indicator_id");

            for (Map.Entry<Long, List<IndicatorMainDissagregationDTO>> entry1 : entry.getValue().entrySet()) {
                LOGGER.debug(" by indicator_id" + entry1.getKey());
                entry1.getValue().forEach(indicatorMainDissagregationDTO -> indicatorMainDissagregationDTO.setIndicatorType("PRODUCTO"));
                entry1.getValue().forEach(indicatorMainDissagregationDTO -> indicatorMainDissagregationDTO.setIndicatorlabel(indicatorMainDissagregationDTO.getDissagregation_type().getLabel()));
                this.setMainDissagregation(entry1.getValue());
            }
        }

        List<IndicatorMainDissagregationDTO> generalIndicators = this.cubeDao.getGeneralIndicatorMainDissagregationDTOTable();
        Map<Long, Map<Long, List<IndicatorMainDissagregationDTO>>> groupedGeneral = generalIndicators.stream()
                .collect(
                        Collectors.groupingBy(IndicatorMainDissagregationDTO::getPeriod_id,
                                Collectors.groupingBy(IndicatorMainDissagregationDTO::getIndicator_id))
                );


        for (Map.Entry<Long, Map<Long, List<IndicatorMainDissagregationDTO>>> entry : groupedGeneral.entrySet()) {
            LOGGER.debug("getPeriod_id: " + entry.getKey());
            LOGGER.debug(" by indicator_id");

            for (Map.Entry<Long, List<IndicatorMainDissagregationDTO>> entry1 : entry.getValue().entrySet()) {
                LOGGER.debug(" by indicator_id" + entry1.getKey());
                entry1.getValue().forEach(indicatorMainDissagregationDTO -> indicatorMainDissagregationDTO.setIndicator_id(0L));
                entry1.getValue().forEach(indicatorMainDissagregationDTO -> indicatorMainDissagregationDTO.setIndicatorType("GENERAL"));
                entry1.getValue().forEach(indicatorMainDissagregationDTO -> indicatorMainDissagregationDTO.setIndicatorlabel(indicatorMainDissagregationDTO.getDissagregation_type().getLabel()));
                this.setMainDissagregation(entry1.getValue());
            }
        }

        List<IndicatorMainDissagregationDTO> customIndicators = this.cubeDao.getCustomMainDissagregationDTOTable();
        Map<Long, Map<Long, List<IndicatorMainDissagregationDTO>>> groupedCustom = customIndicators.stream()
                .collect(
                        Collectors.groupingBy(IndicatorMainDissagregationDTO::getPeriod_id,
                                Collectors.groupingBy(IndicatorMainDissagregationDTO::getIndicator_id))
                );


        for (Map.Entry<Long, Map<Long, List<IndicatorMainDissagregationDTO>>> entry : groupedCustom.entrySet()) {
            LOGGER.debug("getPeriod_id: " + entry.getKey());
            LOGGER.debug(" by indicator_id");

            for (Map.Entry<Long, List<IndicatorMainDissagregationDTO>> entry1 : entry.getValue().entrySet()) {
                LOGGER.debug(" by indicator_id" + entry1.getKey());
                entry1.getValue().forEach(indicatorMainDissagregationDTO -> indicatorMainDissagregationDTO.setOrder(0));
                entry1.getValue().forEach(indicatorMainDissagregationDTO -> indicatorMainDissagregationDTO.setMainDissagregation(Boolean.FALSE));
            }
        }


        List<IndicatorMainDissagregationDTO> total = Stream.concat(generalIndicators.stream(), performanceIndicators.stream())
                .collect(Collectors.toList());

      total = Stream.concat(total.stream(), customIndicators.stream())
                .collect(Collectors.toList());

        return  total;
    }

    private DissagregationType setMainDissagregation(List<IndicatorMainDissagregationDTO> ies) {
        DissagregationType mainDissagregation = null;
        Set<DissagregationType> dissagregations = ies.stream().map(IndicatorMainDissagregationDTO::getDissagregation_type).collect(Collectors.toSet());

        // retiro las de diversidad
        List<DissagregationType> notDiversityDissagregations = dissagregations.stream()
                .filter(dissagregationType ->
                        !dissagregationType.getSimpleDissagregations().contains(DissagregationType.DIVERSIDAD)
                )
                .collect(Collectors.toList());
        // busco la de mayor detalle
        Optional<DissagregationType> mainDissagregationOp = notDiversityDissagregations.stream().sorted((o1, o2) -> o2.getNumberOfDissagregationTypes() - o1.getNumberOfDissagregationTypes()).findFirst();
        if(mainDissagregationOp.isPresent()){
            mainDissagregation=mainDissagregationOp.get();
        }


        for (IndicatorMainDissagregationDTO dto : ies) {
            if (dto.getDissagregation_type().equals(mainDissagregation)) {
                dto.setMainDissagregation(Boolean.TRUE);
                dto.setOrder(dto.getDissagregation_type().getNumberOfDissagregationTypes());
            } else {
                dto.setMainDissagregation(Boolean.FALSE);
                dto.setOrder(dto.getDissagregation_type().getNumberOfDissagregationTypes());
            }
        }

        return mainDissagregation;
    }

    public List<ImplementerDTO> getImplementersTable() {
        return this.cubeDao.getImplementersTable();
    }

    public List<CustomDissagregationDTO> getCustomDissagregationsTable() {
        return this.cubeDao.getCustomDissagregationsTable();


    }
}
