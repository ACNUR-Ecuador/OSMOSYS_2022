package org.unhcr.osmosys.model.cubeDTOs;

public class IndicatorExecutionDissagregationSimpleDTO {

    public IndicatorExecutionDissagregationSimpleDTO(Long ie_id, Integer year, String dissagregation_simple) {
        this.ie_id = ie_id;
        this.year = year;
        this.dissagregation_simple = dissagregation_simple;
    }

    private Long ie_id;
    private Integer year;
    private String dissagregation_simple;

    public Long getIe_id() {
        return ie_id;
    }

    public void setIe_id(Long ie_id) {
        this.ie_id = ie_id;
    }

    public String getDissagregation_simple() {
        return dissagregation_simple;
    }

    public void setDissagregation_simple(String dissagregation_simple) {
        this.dissagregation_simple = dissagregation_simple;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
