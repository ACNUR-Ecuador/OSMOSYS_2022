package org.unhcr.osmosys.reports.model;

import java.math.BigDecimal;

public class PartnerIndicatorReportDTO {
    Long ie_id;
    String implementation_type;
    String area;
    String statement;
    String statement_project;
    String indicator_type;
    String indicator;
    String category;
    String frecuency;
    String implementers;
    String dissagregations;
    String custom_dissagregations;
    BigDecimal total_execution;
    BigDecimal target;
    BigDecimal execution_percentage;
    String is_late;
    String months_late;

    public Long getIe_id() {
        return ie_id;
    }

    public void setIe_id(Long ie_id) {
        this.ie_id = ie_id;
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

    public String getImplementers() {
        return implementers;
    }

    public void setImplementers(String implementers) {
        this.implementers = implementers;
    }

    public String getDissagregations() {
        return dissagregations;
    }

    public void setDissagregations(String dissagregations) {
        this.dissagregations = dissagregations;
    }

    public String getCustom_dissagregations() {
        return custom_dissagregations;
    }

    public void setCustom_dissagregations(String custom_dissagregations) {
        this.custom_dissagregations = custom_dissagregations;
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

    public String getIs_late() {
        return is_late;
    }

    public void setIs_late(String is_late) {
        this.is_late = is_late;
    }

    public String getMonths_late() {
        return months_late;
    }

    public void setMonths_late(String months_late) {
        this.months_late = months_late;
    }
}