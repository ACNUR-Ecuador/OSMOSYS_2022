package org.unhcr.osmosys.reports.service;

import com.sagatechs.generics.exceptions.GeneralAppException;
import org.unhcr.osmosys.model.IndicatorExecution;
import org.unhcr.osmosys.model.enums.IndicatorType;
import org.unhcr.osmosys.reports.model.LateReportingDto;
import org.unhcr.osmosys.services.IndicatorExecutionService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class ReportDataService {

    @Inject
    IndicatorExecutionService indicatorExecutionService;

    public List<LateReportingDto> getLateIndicatorExecutionByProjectId(Long projectId) throws GeneralAppException {
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
    }


}
