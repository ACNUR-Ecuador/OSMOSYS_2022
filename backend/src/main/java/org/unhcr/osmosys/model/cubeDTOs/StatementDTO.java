package org.unhcr.osmosys.model.cubeDTOs;

public class StatementDTO {

    public StatementDTO(Long area_impact_id, String area_impact_code, String area_impact, Long statement_impact_id, String statement_impact_code, String statement_impact, Long area_outcome_id, String area_outcome_code, String area_outcome, Long statement_outcome_id, String statement_outcome_code, String statement_outcome, Long statement_output_id, String statement_output_code, String statement_output_product_code, String statement_output) {
        this.area_impact_id = area_impact_id;
        this.area_impact_code = area_impact_code;
        this.area_impact = area_impact;
        this.statement_impact_id = statement_impact_id;
        this.statement_impact_code = statement_impact_code;
        this.statement_impact = statement_impact;
        this.area_outcome_id = area_outcome_id;
        this.area_outcome_code = area_outcome_code;
        this.area_outcome = area_outcome;
        this.statement_outcome_id = statement_outcome_id;
        this.statement_outcome_code = statement_outcome_code;
        this.statement_outcome = statement_outcome;
        this.statement_output_id = statement_output_id;
        this.statement_output_code = statement_output_code;
        this.statement_output_product_code = statement_output_product_code;
        this.statement_output = statement_output;
    }

    private Long area_impact_id;
    private String area_impact_code;
    private String area_impact;
    private Long statement_impact_id;
    private String statement_impact_code;
    private String statement_impact;
    private Long area_outcome_id;
    private String area_outcome_code;
    private String area_outcome;
    private Long statement_outcome_id;
    private String statement_outcome_code;
    private String statement_outcome;
    private Long statement_output_id;
    private String statement_output_code;
    private String statement_output_product_code;
    private String statement_output;

    public Long getArea_impact_id() {
        return area_impact_id;
    }

    public void setArea_impact_id(Long area_impact_id) {
        this.area_impact_id = area_impact_id;
    }

    public String getArea_impact_code() {
        return area_impact_code;
    }

    public void setArea_impact_code(String area_impact_code) {
        this.area_impact_code = area_impact_code;
    }

    public String getArea_impact() {
        return area_impact;
    }

    public void setArea_impact(String area_impact) {
        this.area_impact = area_impact;
    }

    public Long getStatement_impact_id() {
        return statement_impact_id;
    }

    public void setStatement_impact_id(Long statement_impact_id) {
        this.statement_impact_id = statement_impact_id;
    }

    public String getStatement_impact_code() {
        return statement_impact_code;
    }

    public void setStatement_impact_code(String statement_impact_code) {
        this.statement_impact_code = statement_impact_code;
    }

    public String getStatement_impact() {
        return statement_impact;
    }

    public void setStatement_impact(String statement_impact) {
        this.statement_impact = statement_impact;
    }

    public Long getArea_outcome_id() {
        return area_outcome_id;
    }

    public void setArea_outcome_id(Long area_outcome_id) {
        this.area_outcome_id = area_outcome_id;
    }

    public String getArea_outcome_code() {
        return area_outcome_code;
    }

    public void setArea_outcome_code(String area_outcome_code) {
        this.area_outcome_code = area_outcome_code;
    }

    public String getArea_outcome() {
        return area_outcome;
    }

    public void setArea_outcome(String area_outcome) {
        this.area_outcome = area_outcome;
    }

    public Long getStatement_outcome_id() {
        return statement_outcome_id;
    }

    public void setStatement_outcome_id(Long statement_outcome_id) {
        this.statement_outcome_id = statement_outcome_id;
    }

    public String getStatement_outcome_code() {
        return statement_outcome_code;
    }

    public void setStatement_outcome_code(String statement_outcome_code) {
        this.statement_outcome_code = statement_outcome_code;
    }

    public String getStatement_outcome() {
        return statement_outcome;
    }

    public void setStatement_outcome(String statement_outcome) {
        this.statement_outcome = statement_outcome;
    }

    public Long getStatement_output_id() {
        return statement_output_id;
    }

    public void setStatement_output_id(Long statement_output_id) {
        this.statement_output_id = statement_output_id;
    }

    public String getStatement_output_code() {
        return statement_output_code;
    }

    public void setStatement_output_code(String statement_output_code) {
        this.statement_output_code = statement_output_code;
    }

    public String getStatement_output_product_code() {
        return statement_output_product_code;
    }

    public void setStatement_output_product_code(String statement_output_product_code) {
        this.statement_output_product_code = statement_output_product_code;
    }

    public String getStatement_output() {
        return statement_output;
    }

    public void setStatement_output(String statement_output) {
        this.statement_output = statement_output;
    }
}
