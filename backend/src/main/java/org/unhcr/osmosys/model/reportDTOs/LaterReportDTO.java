package org.unhcr.osmosys.model.reportDTOs;

public class LaterReportDTO {

    public LaterReportDTO() {
    }

    public LaterReportDTO(String project, String implementer, String indicator_code, String indicator, String indicator_category, String late_months, String focal_point, String partner_supervisor, String helper) {
        this.project = project;
        this.implementer = implementer;
        this.indicator_code = indicator_code;
        this.indicator = indicator;
        this.indicator_category = indicator_category;
        this.late_months = late_months;
        this.focal_point=focal_point;
        this.partner_supervisor=partner_supervisor;

    }

    public LaterReportDTO(String implementer, String indicator_code, String indicator, String indicator_category, String late_months, String supervisor, String responsible, String helper) {
        this.implementer = implementer;
        this.indicator_code = indicator_code;
        this.indicator = indicator;
        this.indicator_category = indicator_category;
        this.late_months = late_months;
        this.supervisor=supervisor;
        this.responsible=responsible;
    }

    private String project;
    private String implementer;
    private String indicator_code;
    private String indicator;
    private String indicator_category;
    private String late_months;
    private String focal_point;
    private String partner_supervisor;

    private  String supervisor;
    private String responsible;

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getImplementer() {
        return implementer;
    }

    public void setImplementer(String implementer) {
        this.implementer = implementer;
    }

    public String getIndicator_code() {
        return indicator_code;
    }

    public void setIndicator_code(String indicator_code) {
        this.indicator_code = indicator_code;
    }

    public String getIndicator() {
        return indicator;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }

    public String getIndicator_category() {
        return indicator_category;
    }

    public void setIndicator_category(String indicator_category) {
        this.indicator_category = indicator_category;
    }

    public String getLate_months() {
        return late_months;
    }

    public void setLate_months(String late_months) {
        this.late_months = late_months;
    }

    public String getFocal_point() {
        return focal_point;
    }

    public void setFocal_point(String focal_point) {
        this.focal_point = focal_point;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public String getPartner_supervisor() {
        return partner_supervisor;
    }

    public void setPartner_supervisor(String partner_supervisor) {
        this.partner_supervisor = partner_supervisor;
    }
}
