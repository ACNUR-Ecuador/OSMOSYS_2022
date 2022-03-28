package org.unhcr.osmosys.model.cubeDTOs;

public class MonthCualitativeDataDTO {


    public MonthCualitativeDataDTO(Long month_id, String month_cualitative_data, Integer year) {
        this.month_id = month_id;
        this.month_cualitative_data = month_cualitative_data;
        this.year = year;
    }

    private Long month_id;
    private String month_cualitative_data;
    private Integer year;

    public Long getMonth_id() {
        return month_id;
    }

    public void setMonth_id(Long month_id) {
        this.month_id = month_id;
    }

    public String getMonth_cualitative_data() {
        return month_cualitative_data;
    }

    public void setMonth_cualitative_data(String month_cualitative_data) {
        this.month_cualitative_data = month_cualitative_data;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
