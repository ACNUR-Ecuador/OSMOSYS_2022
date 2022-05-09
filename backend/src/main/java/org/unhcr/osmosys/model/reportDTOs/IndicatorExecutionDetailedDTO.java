package org.unhcr.osmosys.model.reportDTOs;

import java.math.BigDecimal;

public class IndicatorExecutionDetailedDTO {

    public IndicatorExecutionDetailedDTO() {
    }

    public IndicatorExecutionDetailedDTO(Long ie_id, Long period_id, Long project_id, Long reporting_office_id, Long organization_id, Long performance_indicator_id, String implementation_type, String area, String statement, String statement_project, String indicator_type, String indicator, String category, String frecuency, String project, String implementers, BigDecimal total_execution, BigDecimal target, BigDecimal execution_percentage, Integer quarter_order, String quarter, BigDecimal quarter_execution, BigDecimal quarter_target, BigDecimal quarter_percentage, String month_order, String month, BigDecimal month_execution, Long iv_id, Long ivc_id, String dissagregation_type, String lugar_canton, String lugar_provincia, String population_type, String gender_type, String age_type, String country_of_origin, String diversity_type, String age_primary_education_type, String age_tertiary_education_type, String custom_dissagregacion, BigDecimal value) {
        this.ie_id = ie_id;
        this.period_id = period_id;
        this.project_id = project_id;
        this.reporting_office_id = reporting_office_id;
        this.organization_id = organization_id;
        this.performance_indicator_id = performance_indicator_id;
        this.implementation_type = implementation_type;
        this.area = area;
        this.statement = statement;
        this.statement_project = statement_project;
        this.indicator_type = indicator_type;
        this.indicator = indicator;
        this.category = category;
        this.frecuency = frecuency;
        this.project = project;
        this.implementers = implementers;
        this.total_execution = total_execution;
        this.target = target;
        this.execution_percentage = execution_percentage;
        this.quarter_order = quarter_order;
        this.quarter = quarter;
        this.quarter_execution = quarter_execution;
        this.quarter_target = quarter_target;
        this.quarter_percentage = quarter_percentage;
        this.month_order = month_order;
        this.month = month;
        this.month_execution = month_execution;
        this.iv_id = iv_id;
        this.ivc_id = ivc_id;
        this.dissagregation_type = dissagregation_type;
        this.lugar_canton = lugar_canton;
        this.lugar_provincia = lugar_provincia;
        this.population_type = population_type;
        this.gender_type = gender_type;
        this.age_type = age_type;
        this.country_of_origin = country_of_origin;
        this.diversity_type = diversity_type;
        this.age_primary_education_type = age_primary_education_type;
        this.age_tertiary_education_type = age_tertiary_education_type;
        this.custom_dissagregacion = custom_dissagregacion;
        this.value = value;
    }

    private Long ie_id;
    private Long period_id;
    private Long project_id;
    private Long reporting_office_id;
    private Long organization_id;
    private Long performance_indicator_id;
    private String implementation_type;
    private String area;
    private String statement;
    private String statement_project;
    private String indicator_type;
    private String indicator;
    private String category;
    private String frecuency;
    private String project;
    private String implementers;
    private BigDecimal total_execution;
    private BigDecimal target;
    private BigDecimal execution_percentage;
    private Integer quarter_order;
    private String quarter;
    private BigDecimal quarter_execution;
    private BigDecimal quarter_target;
    private BigDecimal quarter_percentage;
    private String month_order;
    private String month;
    private BigDecimal month_execution;
    private Long iv_id;
    private Long ivc_id;
    private String dissagregation_type;
    private String lugar_canton;
    private String lugar_provincia;
    private String population_type;
    private String gender_type;
    private String age_type;
    private String country_of_origin;
    private String diversity_type;
    private String age_primary_education_type;
    private String age_tertiary_education_type;
    private String custom_dissagregacion;
    private BigDecimal value;

    public Long getIe_id() {
        return ie_id;
    }

    public void setIe_id(Long ie_id) {
        this.ie_id = ie_id;
    }

    public Long getPeriod_id() {
        return period_id;
    }

    public void setPeriod_id(Long period_id) {
        this.period_id = period_id;
    }

    public Long getProject_id() {
        return project_id;
    }

    public void setProject_id(Long project_id) {
        this.project_id = project_id;
    }

    public Long getReporting_office_id() {
        return reporting_office_id;
    }

    public void setReporting_office_id(Long reporting_office_id) {
        this.reporting_office_id = reporting_office_id;
    }

    public Long getOrganization_id() {
        return organization_id;
    }

    public void setOrganization_id(Long organization_id) {
        this.organization_id = organization_id;
    }

    public String getImplementation_type() {
        return implementation_type;
    }

    public void setImplementation_type(String implementation_type) {
        this.implementation_type = implementation_type;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public String getStatement_project() {
        return statement_project;
    }

    public void setStatement_project(String statement_project) {
        this.statement_project = statement_project;
    }

    public String getIndicator_type() {
        return indicator_type;
    }

    public void setIndicator_type(String indicator_type) {
        this.indicator_type = indicator_type;
    }

    public String getIndicator() {
        return indicator;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFrecuency() {
        return frecuency;
    }

    public void setFrecuency(String frecuency) {
        this.frecuency = frecuency;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getImplementers() {
        return implementers;
    }

    public void setImplementers(String implementers) {
        this.implementers = implementers;
    }

    public BigDecimal getTotal_execution() {
        return total_execution;
    }

    public void setTotal_execution(BigDecimal total_execution) {
        this.total_execution = total_execution;
    }

    public BigDecimal getTarget() {
        return target;
    }

    public void setTarget(BigDecimal target) {
        this.target = target;
    }

    public BigDecimal getExecution_percentage() {
        return execution_percentage;
    }

    public void setExecution_percentage(BigDecimal execution_percentage) {
        this.execution_percentage = execution_percentage;
    }

    public Integer getQuarter_order() {
        return quarter_order;
    }

    public void setQuarter_order(Integer quarter_order) {
        this.quarter_order = quarter_order;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public BigDecimal getQuarter_execution() {
        return quarter_execution;
    }

    public void setQuarter_execution(BigDecimal quarter_execution) {
        this.quarter_execution = quarter_execution;
    }

    public BigDecimal getQuarter_target() {
        return quarter_target;
    }

    public void setQuarter_target(BigDecimal quarter_target) {
        this.quarter_target = quarter_target;
    }

    public BigDecimal getQuarter_percentage() {
        return quarter_percentage;
    }

    public void setQuarter_percentage(BigDecimal quarter_percentage) {
        this.quarter_percentage = quarter_percentage;
    }

    public String getMonth_order() {
        return month_order;
    }

    public void setMonth_order(String month_order) {
        this.month_order = month_order;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public BigDecimal getMonth_execution() {
        return month_execution;
    }

    public void setMonth_execution(BigDecimal month_execution) {
        this.month_execution = month_execution;
    }

    public Long getIv_id() {
        return iv_id;
    }

    public void setIv_id(Long iv_id) {
        this.iv_id = iv_id;
    }

    public Long getIvc_id() {
        return ivc_id;
    }

    public void setIvc_id(Long ivc_id) {
        this.ivc_id = ivc_id;
    }

    public String getDissagregation_type() {
        return dissagregation_type;
    }

    public void setDissagregation_type(String dissagregation_type) {
        this.dissagregation_type = dissagregation_type;
    }

    public String getLugar_canton() {
        return lugar_canton;
    }

    public void setLugar_canton(String lugar_canton) {
        this.lugar_canton = lugar_canton;
    }

    public String getLugar_provincia() {
        return lugar_provincia;
    }

    public void setLugar_provincia(String lugar_provincia) {
        this.lugar_provincia = lugar_provincia;
    }

    public String getPopulation_type() {
        return population_type;
    }

    public void setPopulation_type(String population_type) {
        this.population_type = population_type;
    }

    public String getGender_type() {
        return gender_type;
    }

    public void setGender_type(String gender_type) {
        this.gender_type = gender_type;
    }

    public String getAge_type() {
        return age_type;
    }

    public void setAge_type(String age_type) {
        this.age_type = age_type;
    }

    public String getCountry_of_origin() {
        return country_of_origin;
    }

    public void setCountry_of_origin(String country_of_origin) {
        this.country_of_origin = country_of_origin;
    }

    public String getDiversity_type() {
        return diversity_type;
    }

    public void setDiversity_type(String diversity_type) {
        this.diversity_type = diversity_type;
    }

    public String getAge_primary_education_type() {
        return age_primary_education_type;
    }

    public void setAge_primary_education_type(String age_primary_education_type) {
        this.age_primary_education_type = age_primary_education_type;
    }

    public String getAge_tertiary_education_type() {
        return age_tertiary_education_type;
    }

    public void setAge_tertiary_education_type(String age_tertiary_education_type) {
        this.age_tertiary_education_type = age_tertiary_education_type;
    }

    public String getCustom_dissagregacion() {
        return custom_dissagregacion;
    }

    public void setCustom_dissagregacion(String custom_dissagregacion) {
        this.custom_dissagregacion = custom_dissagregacion;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
