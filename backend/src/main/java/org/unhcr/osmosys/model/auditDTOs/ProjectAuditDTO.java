package org.unhcr.osmosys.model.auditDTOs;

import com.sagatechs.generics.persistence.model.State;

import java.time.LocalDate;
import java.util.List;

public class ProjectAuditDTO {
    private Long id;
    private String code;
    private String name;
    private String state;
    private String startDate;
    private String endDate;
    private String OrganizationId;
    private String focalPointId;
    private String periodId;
    private List<ProjectLocationAssigmentsDTO> projectLocationAssigments;
    private List<IndicatorExecutionDTO> indicatorExecutions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getOrganizationId() {
        return OrganizationId;
    }

    public void setOrganizationId(String organizationId) {
        OrganizationId = organizationId;
    }

    public String getFocalPointId() {
        return focalPointId;
    }

    public void setFocalPointId(String focalPointId) {
        this.focalPointId = focalPointId;
    }

    public String getPeriodId() {
        return periodId;
    }

    public void setPeriodId(String periodId) {
        this.periodId = periodId;
    }

    public List<ProjectLocationAssigmentsDTO> getProjectLocationAssigments() {
        return projectLocationAssigments;
    }

    public void setProjectLocationAssigments(List<ProjectLocationAssigmentsDTO> projectLocationAssigments) {
        this.projectLocationAssigments = projectLocationAssigments;
    }

    public List<IndicatorExecutionDTO> getIndicatorExecutions() {
        return indicatorExecutions;
    }

    public void setIndicatorExecutions(List<IndicatorExecutionDTO> indicatorExecutions) {
        this.indicatorExecutions = indicatorExecutions;
    }
}
