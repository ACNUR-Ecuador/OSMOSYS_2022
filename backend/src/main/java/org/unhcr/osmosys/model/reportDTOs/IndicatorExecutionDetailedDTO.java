package org.unhcr.osmosys.model.reportDTOs;

import java.math.BigDecimal;

@SuppressWarnings("unused")
public class IndicatorExecutionDetailedDTO {

    public IndicatorExecutionDetailedDTO(Long ie_id, String implementation_type, String area, String statement, String statement_project, String indicator_type, String indicator, String category, String frecuency, String implementers, BigDecimal total_execution, BigDecimal target, BigDecimal execution_percentage, Integer quarter_order, String quarter, BigDecimal quarter_execution, BigDecimal quarter_target, BigDecimal quarter_percentage, String month_order, String month, BigDecimal month_execution, Long iv_id, Long ivc_id, String dissagregation_type, String dissagregation_level1, String dissagregation_level2, BigDecimal value) {
        this.ie_id = ie_id;
        this.implementation_type = implementation_type;
        this.area = area;
        this.statement = statement;
        this.statement_project = statement_project;
        this.indicator_type = indicator_type;
        this.indicator = indicator;
        this.category = category;
        this.frecuency = frecuency;
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
        this.dissagregation_level1 = dissagregation_level1;
        this.dissagregation_level2 = dissagregation_level2;
        this.value = value;
    }

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
    BigDecimal total_execution;
    BigDecimal target;
    BigDecimal execution_percentage;
    Integer quarter_order;
    String quarter;
    BigDecimal quarter_execution;
    BigDecimal quarter_target;
    BigDecimal quarter_percentage;
    String month_order;
    String month;
    BigDecimal month_execution;
    Long iv_id;
    Long ivc_id;
    String dissagregation_type;
    String dissagregation_level1;
    String dissagregation_level2;
    BigDecimal value;

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

    public String getDissagregation_level1() {
        return dissagregation_level1;
    }

    public void setDissagregation_level1(String dissagregation_level1) {
        this.dissagregation_level1 = dissagregation_level1;
    }

    public String getDissagregation_level2() {
        return dissagregation_level2;
    }

    public void setDissagregation_level2(String dissagregation_level2) {
        this.dissagregation_level2 = dissagregation_level2;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
