package org.unhcr.osmosys.model.reportDTOs;

import java.math.BigDecimal;

public class IndicatorExecutionDetailedWithOfficesDTO extends IndicatorExecutionDetailedDTO {

    public IndicatorExecutionDetailedWithOfficesDTO() {
    }

    public IndicatorExecutionDetailedWithOfficesDTO(Long ie_id, Long period_id, Long project_id, Long reporting_office_id, Long organization_id, Long performance_indicator_id, String implementation_type, String area, String statement, String statement_project, String indicator_type, String indicator, String tags, String category, String frecuency, String project, String implementers, BigDecimal total_execution, BigDecimal target, BigDecimal execution_percentage, Integer quarter_order, String quarter, BigDecimal quarter_execution, BigDecimal quarter_target, BigDecimal quarter_percentage, String month_order, String month, BigDecimal month_execution, Long iv_id, Long ivc_id, String dissagregation_type, String lugar_canton, String lugar_provincia, String population_type, String gender_type, String age_type, String country_of_origin, String diversity_type,  String custom_dissagregacion, BigDecimal value, Long iv_id_office, String office_id, String office, String office_parent_id, String office_parent, String office_implementer_id, String office_implementer, String office_implementer_parent_id, String office_implementer_parent) {
        super(ie_id, period_id, project_id, reporting_office_id, organization_id, performance_indicator_id, implementation_type, area, statement, statement_project, indicator_type, indicator, tags, category, frecuency, project, implementers, total_execution, target, execution_percentage, quarter_order, quarter, quarter_execution, quarter_target, quarter_percentage, month_order, month, month_execution, iv_id, ivc_id, dissagregation_type, lugar_canton, lugar_provincia, population_type, gender_type, age_type, country_of_origin, diversity_type,  custom_dissagregacion, value);
        this.iv_id_office = iv_id_office;
        this.office_id = office_id;
        this.office = office;
        this.office_parent_id = office_parent_id;
        this.office_parent = office_parent;
        this.office_implementer_id = office_implementer_id;
        this.office_implementer = office_implementer;
        this.office_implementer_parent_id = office_implementer_parent_id;
        this.office_implementer_parent = office_implementer_parent;
    }

    private Long iv_id_office;
    private String office_id;
    private String office;
    private String office_parent_id;
    private String office_parent;
    private String office_implementer_id;
    private String office_implementer;
    private String office_implementer_parent_id;
    private String office_implementer_parent;

    public Long getIv_id_office() {
        return iv_id_office;
    }

    public void setIv_id_office(Long iv_id_office) {
        this.iv_id_office = iv_id_office;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getOffice_parent() {
        return office_parent;
    }

    public void setOffice_parent(String office_parent) {
        this.office_parent = office_parent;
    }

    public String getOffice_implementer() {
        return office_implementer;
    }

    public void setOffice_implementer(String office_implementer) {
        this.office_implementer = office_implementer;
    }

    public String getOffice_implementer_parent() {
        return office_implementer_parent;
    }

    public void setOffice_implementer_parent(String office_implementer_parent) {
        this.office_implementer_parent = office_implementer_parent;
    }

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }

    public String getOffice_parent_id() {
        return office_parent_id;
    }

    public void setOffice_parent_id(String office_parent_id) {
        this.office_parent_id = office_parent_id;
    }

    public String getOffice_implementer_id() {
        return office_implementer_id;
    }

    public void setOffice_implementer_id(String office_implementer_id) {
        this.office_implementer_id = office_implementer_id;
    }

    public String getOffice_implementer_parent_id() {
        return office_implementer_parent_id;
    }

    public void setOffice_implementer_parent_id(String office_implementer_parent_id) {
        this.office_implementer_parent_id = office_implementer_parent_id;
    }
}
