package org.unhcr.osmosys.reports.service;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.unhcr.osmosys.model.IndicatorExecution;
import org.unhcr.osmosys.services.IndicatorExecutionService;
import org.unhcr.osmosys.webServices.model.IndicatorExecutionWeb;
import org.unhcr.osmosys.webServices.model.MonthWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "rawtypes"})
@Stateless
public class ReportDataService {

    @Inject
    IndicatorExecutionService indicatorExecutionService;

 /*   public List<LateReportingDto> getLateIndicatorExecutionByProjectId(Long projectId) throws GeneralAppException {
        List<IndicatorExecution> data = this.indicatorExecutionService.getLateIndicatorExecutionByProjectId(projectId);
        return data.stream().map(indicatorExecution -> {
            LateReportingDto dto = new LateReportingDto();
            dto.setProjectName(indicatorExecution.getProject().getName());
            dto.setPartnerCode(indicatorExecution.getProject().getOrganization().getCode());
            dto.setPartnerName(indicatorExecution.getProject().getOrganization().getAcronym() + "-" + indicatorExecution.getProject().getOrganization().getDescription());
            dto.setStatmentName(indicatorExecution.getProjectStatement().getCode() + "-" + indicatorExecution.getProjectStatement().getDescription());
            if (indicatorExecution.getIndicatorType().equals(IndicatorType.GENERAL)) {
                dto.setIndicatorName(indicatorExecution.getPeriod().getGeneralIndicator().getDescription());
            } else {
                dto.setIndicatorName(indicatorExecution.getIndicator().getCode() + "-" + indicatorExecution.getIndicator().getDescription());
            }
            String notReportedMonths = indicatorExecution.getQuarters().stream()
                    .map(quarter -> quarter.getMonths())
                    .flatMap(Collection::stream).sorted((o1, o2) -> {
                        return o1.getOrder().compareTo(o2.getOrder());
                    })
                    .map(month -> {
                        return month.getMonth().getLabel() + "-" + month.getYear();
                    })
                    .collect(Collectors.joining(", "));
            dto.setNotReportedMonths(notReportedMonths);
            return dto;
        }).collect(Collectors.toList());
    }*/

    public List<Map<String, Object>> indicatorExecutionsProjectsReportsByPeriodId(Long periodId) throws GeneralAppException {
        List<IndicatorExecutionWeb> indicatorExecutions = this.indicatorExecutionService.getActiveProjectIndicatorExecutionsByPeriodId(periodId);
        return this.indicatorExecutionsProjectsReports(indicatorExecutions);
    }


    public List<Map<String, Object>> indicatorExecutionsProjectsReports(List<IndicatorExecutionWeb> indicatorExecutions) {
        List<Map<String, Object>> r = new ArrayList();
        for (IndicatorExecutionWeb ie : indicatorExecutions) {
            Map<String, Object> map = new HashMap();
            map.put("partner", ie.getProject().getOrganization().getAcronym() + '-' + ie.getProject().getOrganization().getDescription());
            map.put("project", ie.getProject().getCode() + '-' + ie.getProject().getName());
            map.put("indicatorType", ie.getIndicatorType().getLabel());
            map.put("outcomeStatement", ie.getProjectStatement() != null ? ie.getProjectStatement().getCode() + '-' + ie.getProjectStatement().getDescription() : null);
            map.put("indicator", ie.getIndicator().getCode() + "-" + ie.getIndicator().getDescription());
            map.put("target", ie.getTarget());
            map.put("totalExecution", ie.getTotalExecution());
            map.put("executionPercentage", ie.getExecutionPercentage());
            map.put("late", ie.getLate() ? "Si" : "No");
            // late months
            if (ie.getLate() && CollectionUtils.isNotEmpty(ie.getLateMonths())) {
                List<String> months = ie.getLateMonths()
                        .stream()
                        .filter(monthWeb -> monthWeb.getState().equals(State.ACTIVO))
                        .sorted(Comparator.comparingInt(MonthWeb::getOrder))
                        .map(monthWeb -> monthWeb.getMonth().getLabel() + "-" + monthWeb.getYear())
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(months)) {
                    map.put("lateMonths", String.join("; ", months));
                }
            }
            r.add(map);
        }

        // ordeno
        /*r = r.stream()
                .sorted(Comparator.comparing(o -> ((String) o.get("indicator"))))
                //.sorted(Comparator.comparing(o -> ((String) o.get("outcomeStatement"))))
                .sorted(Comparator.comparing(o -> ((String) o.get("indicatorType"))))
                .sorted(Comparator.comparing(o -> ((String) o.get("project"))))
                .sorted(Comparator.comparing(o -> ((String) o.get("partner"))))
                .collect(Collectors.toList());*/

        return r;

    }


}
