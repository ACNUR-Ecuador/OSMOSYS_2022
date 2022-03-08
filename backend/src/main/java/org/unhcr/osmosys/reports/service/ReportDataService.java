package org.unhcr.osmosys.reports.service;

import com.sagatechs.generics.exceptions.GeneralAppException;
import org.apache.commons.lang3.StringUtils;
import org.unhcr.osmosys.services.IndicatorExecutionService;
import org.unhcr.osmosys.webServices.model.IndicatorExecutionWeb;
import org.unhcr.osmosys.webServices.model.LateType;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            map.put("late", ie.getLate().equals(LateType.LATE) ? "Si" : "No");
            r.add(map);
        }

        return r;

    }


}
