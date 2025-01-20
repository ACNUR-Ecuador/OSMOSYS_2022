package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.utils.DateUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.jboss.logging.Logger;
import org.threeten.extra.YearQuarter;
import org.unhcr.osmosys.daos.QuarterDao;
import org.unhcr.osmosys.model.*;
import org.unhcr.osmosys.model.enums.DissagregationType;
import org.unhcr.osmosys.model.enums.QuarterEnum;
import org.unhcr.osmosys.model.enums.TotalIndicatorCalculationType;
import org.unhcr.osmosys.model.standardDissagregations.options.StandardDissagregationOption;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
public class QuarterService {

    @Inject
    QuarterDao quarterDao;

    @Inject
    MonthService monthService;

    @Inject
    DateUtils dateUtils;

    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(QuarterService.class);

    public Quarter getById(Long id) {
        return this.quarterDao.find(id);
    }

    public Quarter saveOrUpdate(Quarter quarter) {
        if (quarter.getId() == null) {
            this.quarterDao.save(quarter);
        } else {
            this.quarterDao.update(quarter);
        }
        return quarter;
    }



    public Set<Quarter> createQuarters(LocalDate startDate, LocalDate endDate,
                                       Map<DissagregationType,Map<DissagregationType, List<StandardDissagregationOption>>> dissagregationsMap,
                                       List<CustomDissagregation> customDissagregations
                                       ) throws GeneralAppException {
        Set<Quarter> qs = new HashSet<>();
        List<YearQuarter> yearQuarters = this.dateUtils.calculateQuarter(startDate, endDate);
        if (yearQuarters.isEmpty()) {
            throw new GeneralAppException("Error al crear los trimestres ", Response.Status.INTERNAL_SERVER_ERROR);
        }


        for (YearQuarter yearQuarter : yearQuarters) {
            Quarter q = this.createQuarter(yearQuarter, startDate, endDate,dissagregationsMap, customDissagregations);
            qs.add(q);
        }
        return qs;

    }

    public Quarter createQuarter(YearQuarter yearQuarter,
                                 LocalDate startDate, LocalDate endDate,
                                 Map<DissagregationType,Map<DissagregationType, List<StandardDissagregationOption>>> dissagregationMap,
                                 List<CustomDissagregation> customDissagregations

    ) throws GeneralAppException {
        Quarter q = new Quarter();
        q.setYear(yearQuarter.getYear());
        q.setTarget(null);
        q.setOrder(0);
        q.setQuarter(QuarterEnum.getByQuarterNumber(yearQuarter.getQuarterValue()));
        q.setState(State.ACTIVO);
        q.setBlockUpdate(Boolean.FALSE);
        List<Month> ms = this.monthService.createMonthsForQuarter(q, startDate, endDate,
                dissagregationMap, customDissagregations);
        for (Month month : ms) {
            q.addMonth(month);
        }
        return q;
    }

    public void updateQuarterDissagregations(IndicatorExecution ie, Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> dissagregationTypeMapMap,
                                             Set<CustomDissagregationAssignationToIndicator> customDissagregationAssignationToIndicators) throws GeneralAppException {
        Set<Quarter> quarters = ie.getQuarters();
        // reviso q solo esten las del periodo
        if(customDissagregationAssignationToIndicators != null) {
            customDissagregationAssignationToIndicators = customDissagregationAssignationToIndicators
                    .stream()
                    .filter(customDissagregationAssignationToIndicator ->
                            customDissagregationAssignationToIndicator.getPeriod().getId().equals(ie.getPeriod().getId())
                    ).collect(Collectors.toSet());
        }

        for (Quarter quarter : quarters) {
            Set<Month> months = quarter.getMonths();
            for (Month month : months) {
                this.monthService.updateMonthDissagregations(month, dissagregationTypeMapMap);

                this.monthService.updateMonthCustomDissagregations(month, customDissagregationAssignationToIndicators);


            }
        }

    }

    /*************************************************************************************/



    public void updateQuarterTotals(Quarter quarter, TotalIndicatorCalculationType totalIndicatorCalculationType) throws GeneralAppException {
        for (Month month1 : quarter.getMonths()) {
            this.monthService.updateMonthTotals(month1, totalIndicatorCalculationType);
        }

        List<Month> months = quarter.getMonths().stream().filter(month -> month.getState().equals(State.ACTIVO)).collect(Collectors.toList());

        List<BigDecimal> totalMonthValues = months.stream().filter(month -> month.getState().equals(State.ACTIVO)).map(Month::getTotalExecution).filter(Objects::nonNull).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(totalMonthValues)) {
            quarter.setTotalExecution(null);
            quarter.setExecutionPercentage(null);
        } else {
            BigDecimal totalExecution;
            switch (totalIndicatorCalculationType) {
                case SUMA:
                    totalExecution = totalMonthValues.stream().reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
                    break;
                case PROMEDIO:
                    BigDecimal total = totalMonthValues.stream().reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
                    totalExecution = total.divide(new BigDecimal(totalMonthValues.size()), RoundingMode.HALF_UP);
                    break;
                case MAXIMO:
                    totalExecution = totalMonthValues.stream().reduce(BigDecimal::max).orElse(BigDecimal.ZERO);
                    break;
                case MINIMO:
                    totalExecution = totalMonthValues.stream().reduce(BigDecimal::min).orElse(BigDecimal.ZERO);
                    break;
                default:
                    throw new GeneralAppException("Tipo de calculo no soportado, por favor comuniquese con el administrador del sistema", Response.Status.INTERNAL_SERVER_ERROR);
            }
            quarter.setTotalExecution(totalExecution);

            if (quarter.getTotalExecution() != null && quarter.getTarget() != null) {
                // if (quarter.getTarget().equals(BigDecimal.ZERO)) {
                if (quarter.getTarget().compareTo(BigDecimal.ZERO) == 0) {
                    quarter.setTarget(BigDecimal.ZERO);
                } else {
                    quarter.setExecutionPercentage(quarter.getTotalExecution().divide(quarter.getTarget(), 4, RoundingMode.HALF_UP));
                }
            } else {
                quarter.setExecutionPercentage(null);
            }
        }
    }



    public void blockQuarterStateByProjectId(Long projectId, QuarterEnum quarterEnum, Integer year, Boolean blockUpdate) {
        List<Quarter> quarters = this.quarterDao.getQuarterByProjectIdQuarterEnumAndYear(projectId, quarterEnum, year);
        for (Quarter quarter : quarters) {
            quarter.setBlockUpdate(blockUpdate);
            for (Month month : quarter.getMonths()) {
                // no activar si es bloqueo automatico
                if (!(month.getBlockUpdate()
                        && month.getQuarter().getIndicatorExecution().getIndicator() != null
                        && month.getQuarter().getIndicatorExecution().getIndicator().getBlockAfterUpdate()
                        && month.getTotalExecution() != null
                )) {
                    month.setBlockUpdate(blockUpdate);
                }
            }
            this.saveOrUpdate(quarter);
        }
    }


}
