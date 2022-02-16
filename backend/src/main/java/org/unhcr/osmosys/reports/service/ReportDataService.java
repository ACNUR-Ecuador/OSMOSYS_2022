package org.unhcr.osmosys.reports.service;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
            map.put("indicator", ie.getIndicator().getCode() + "-" + ie.getIndicator().getDescription()
                    + (StringUtils.isNotEmpty(ie.getIndicator().getCategory()) ? " (Categoría: " + ie.getIndicator().getCategory() + " )" : ""));
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
