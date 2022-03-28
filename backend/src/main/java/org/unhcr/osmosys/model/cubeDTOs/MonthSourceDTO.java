package org.unhcr.osmosys.model.cubeDTOs;

public class MonthSourceDTO {
    public MonthSourceDTO(Long month_id, String source_type, String source_other, Integer year) {
        this.month_id = month_id;
        this.source_type = source_type;
        this.source_other = source_other;
        this.year = year;
    }

    private Long month_id;
    private String source_type;
    private String source_other;
    private Integer year;

    public Long getMonth_id() {
        return month_id;
    }

    public void setMonth_id(Long month_id) {
        this.month_id = month_id;
    }

    public String getSource_type() {
        return source_type;
    }

    public void setSource_type(String source_type) {
        this.source_type = source_type;
    }

    public String getSource_other() {
        return source_other;
    }

    public void setSource_other(String source_other) {
        this.source_other = source_other;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
