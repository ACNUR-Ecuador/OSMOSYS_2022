package org.unhcr.osmosys.model.reportDTOs;

import java.util.List;

public class ReportFiltersDTO {

    public ReportFiltersDTO() {
    }

    public ReportFiltersDTO(List<Long> monthsId, List<Long> areasId, List<Long> projectsId, List<Long> indicatorsId, List<Long> tagsId) {
        this.monthsId = monthsId;
        this.areasId = areasId;
        this.projectsId = projectsId;
        this.indicatorsId = indicatorsId;
        this.tagsId = tagsId;
    }

    private List<Long> monthsId;
    private List<Long> areasId;
    private List<Long> projectsId;
    private List<Long> indicatorsId;
    private List<Long> tagsId;

    public List<Long> getMonthsId() {
        return monthsId;
    }

    public void setMonthsId(List<Long> monthsId) {
        this.monthsId = monthsId;
    }

    public List<Long> getAreasId() {
        return areasId;
    }

    public void setAreasId(List<Long> areasId) {
        this.areasId = areasId;
    }

    public List<Long> getProjectsId() {
        return projectsId;
    }

    public void setProjectsId(List<Long> projectsId) {
        this.projectsId = projectsId;
    }

    public List<Long> getIndicatorsId() {
        return indicatorsId;
    }

    public void setIndicatorsId(List<Long> indicatorsId) {
        this.indicatorsId = indicatorsId;
    }

    public List<Long> getTagsId() {
        return tagsId;
    }

    public void setTagsId(List<Long> tagsId) {
        this.tagsId = tagsId;
    }
}
