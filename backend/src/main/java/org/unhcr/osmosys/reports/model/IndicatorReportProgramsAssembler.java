package org.unhcr.osmosys.reports.model;

import org.unhcr.osmosys.webServices.model.ProjectResumeWeb;

import java.util.List;

public class IndicatorReportProgramsAssembler {

    ProjectResumeWeb project;
    List<IndicatorReportProgramsDTO> indicatorReportProgramsDTOs;

    public ProjectResumeWeb getProject() {
        return project;
    }

    public void setProject(ProjectResumeWeb project) {
        this.project = project;
    }

    public List<IndicatorReportProgramsDTO> getIndicatorReportProgramsDTOs() {
        return indicatorReportProgramsDTOs;
    }

    public void setIndicatorReportProgramsDTOs(List<IndicatorReportProgramsDTO> indicatorReportProgramsDTOs) {
        this.indicatorReportProgramsDTOs = indicatorReportProgramsDTOs;
    }
}
