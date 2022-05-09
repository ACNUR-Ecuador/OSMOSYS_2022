package org.unhcr.osmosys.model.cubeDTOs;

import java.math.BigDecimal;

public class FactDTO {
    public FactDTO(Long id, Long ie_id, Long period_id, Integer period_year, String implementation_type, Long assigned_user_id, Long assigned_user_backup_id, Long indicator_id, Long statement_id, Long project_statement_id, String indicator_type, Long organization_id, Long supervisor_id, Long office_id, Long project_id, String month_year_id, Long month_id, String dissagregation_type, String age_type, String age_primary_education_type, String age_tertiary_education_type, String gender_type, String country_of_origin, String population_type, String diversity_type, Long canton_id, BigDecimal total_execution, BigDecimal total_target, BigDecimal total_execution_percentage, BigDecimal quarter_execution, BigDecimal quarter_target, BigDecimal quarter_execution_percentage, BigDecimal month_execution, BigDecimal value) {
        this.id = id;
        this.ie_id = ie_id;
        this.period_id = period_id;
        this.period_year = period_year;
        this.implementation_type = implementation_type;
        this.assigned_user_id = assigned_user_id;
        this.assigned_user_backup_id = assigned_user_backup_id;
        this.indicator_id = indicator_id;
        this.statement_id = statement_id;
        this.project_statement_id = project_statement_id;
        this.indicator_type = indicator_type;
        this.organization_id = organization_id;
        this.supervisor_id = supervisor_id;
        this.office_id = office_id;
        this.project_id = project_id;
        this.month_year_id = month_year_id;
        this.month_id = month_id;
        this.dissagregation_type = dissagregation_type;
        this.age_type = age_type;
        this.age_primary_education_type = age_primary_education_type;
        this.age_tertiary_education_type = age_tertiary_education_type;
        this.gender_type = gender_type;
        this.country_of_origin = country_of_origin;
        this.population_type = population_type;
        this.diversity_type = diversity_type;
        this.canton_id = canton_id;
        this.total_execution = total_execution;
        this.total_target = total_target;
        this.total_execution_percentage = total_execution_percentage;
        this.quarter_execution = quarter_execution;
        this.quarter_target = quarter_target;
        this.quarter_execution_percentage = quarter_execution_percentage;
        this.month_execution = month_execution;
        this.value = value;
    }

    private Long id;
    private Long ie_id;
    private Long period_id;
    private Integer period_year;
    private String implementation_type;
    private Long assigned_user_id;
    private Long assigned_user_backup_id;
    private Long indicator_id;
    private Long statement_id;
    private Long project_statement_id;
    private String indicator_type;
    private Long organization_id;
    private Long supervisor_id;
    private Long office_id;
    private Long project_id;
    private String month_year_id;
    private Long month_id;
    private String dissagregation_type;
    private String age_type;
    private String age_primary_education_type;
    private String age_tertiary_education_type;
    private String gender_type;
    private String country_of_origin;
    private String population_type;
    private String diversity_type;
    private Long canton_id;
    private BigDecimal total_execution;
    private BigDecimal total_target;
    private BigDecimal total_execution_percentage;
    private BigDecimal quarter_execution;
    private BigDecimal quarter_target;
    private BigDecimal quarter_execution_percentage;
    private BigDecimal month_execution;
    private BigDecimal value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Integer getPeriod_year() {
        return period_year;
    }

    public void setPeriod_year(Integer period_year) {
        this.period_year = period_year;
    }

    public String getImplementation_type() {
        return implementation_type;
    }

    public void setImplementation_type(String implementation_type) {
        this.implementation_type = implementation_type;
    }

    public Long getAssigned_user_id() {
        return assigned_user_id;
    }

    public void setAssigned_user_id(Long assigned_user_id) {
        this.assigned_user_id = assigned_user_id;
    }

    public Long getAssigned_user_backup_id() {
        return assigned_user_backup_id;
    }

    public void setAssigned_user_backup_id(Long assigned_user_backup_id) {
        this.assigned_user_backup_id = assigned_user_backup_id;
    }

    public Long getIndicator_id() {
        return indicator_id;
    }

    public void setIndicator_id(Long indicator_id) {
        this.indicator_id = indicator_id;
    }

    public Long getStatement_id() {
        return statement_id;
    }

    public void setStatement_id(Long statement_id) {
        this.statement_id = statement_id;
    }

    public Long getProject_statement_id() {
        return project_statement_id;
    }

    public void setProject_statement_id(Long project_statement_id) {
        this.project_statement_id = project_statement_id;
    }

    public String getIndicator_type() {
        return indicator_type;
    }

    public void setIndicator_type(String indicator_type) {
        this.indicator_type = indicator_type;
    }

    public Long getOrganization_id() {
        return organization_id;
    }

    public void setOrganization_id(Long organization_id) {
        this.organization_id = organization_id;
    }

    public Long getSupervisor_id() {
        return supervisor_id;
    }

    public void setSupervisor_id(Long supervisor_id) {
        this.supervisor_id = supervisor_id;
    }

    public Long getOffice_id() {
        return office_id;
    }

    public void setOffice_id(Long office_id) {
        this.office_id = office_id;
    }

    public Long getProject_id() {
        return project_id;
    }

    public void setProject_id(Long project_id) {
        this.project_id = project_id;
    }

    public String getMonth_year_id() {
        return month_year_id;
    }

    public void setMonth_year_id(String month_year_id) {
        this.month_year_id = month_year_id;
    }

    public Long getMonth_id() {
        return month_id;
    }

    public void setMonth_id(Long month_id) {
        this.month_id = month_id;
    }

    public String getDissagregation_type() {
        return dissagregation_type;
    }

    public void setDissagregation_type(String dissagregation_type) {
        this.dissagregation_type = dissagregation_type;
    }

    public String getAge_type() {
        return age_type;
    }

    public void setAge_type(String age_type) {
        this.age_type = age_type;
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

    public String getGender_type() {
        return gender_type;
    }

    public void setGender_type(String gender_type) {
        this.gender_type = gender_type;
    }

    public String getCountry_of_origin() {
        return country_of_origin;
    }

    public void setCountry_of_origin(String country_of_origin) {
        this.country_of_origin = country_of_origin;
    }

    public String getPopulation_type() {
        return population_type;
    }

    public void setPopulation_type(String population_type) {
        this.population_type = population_type;
    }

    public String getDiversity_type() {
        return diversity_type;
    }

    public void setDiversity_type(String diversity_type) {
        this.diversity_type = diversity_type;
    }

    public Long getCanton_id() {
        return canton_id;
    }

    public void setCanton_id(Long canton_id) {
        this.canton_id = canton_id;
    }

    public BigDecimal getTotal_execution() {
        return total_execution;
    }

    public void setTotal_execution(BigDecimal total_execution) {
        this.total_execution = total_execution;
    }

    public BigDecimal getTotal_target() {
        return total_target;
    }

    public void setTotal_target(BigDecimal total_target) {
        this.total_target = total_target;
    }

    public BigDecimal getTotal_execution_percentage() {
        return total_execution_percentage;
    }

    public void setTotal_execution_percentage(BigDecimal total_execution_percentage) {
        this.total_execution_percentage = total_execution_percentage;
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

    public BigDecimal getQuarter_execution_percentage() {
        return quarter_execution_percentage;
    }

    public void setQuarter_execution_percentage(BigDecimal quarter_execution_percentage) {
        this.quarter_execution_percentage = quarter_execution_percentage;
    }

    public BigDecimal getMonth_execution() {
        return month_execution;
    }

    public void setMonth_execution(BigDecimal month_execution) {
        this.month_execution = month_execution;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
