package org.unhcr.osmosys.model.cubeDTOs;

public class MonthQuarterYearDTO {


    public MonthQuarterYearDTO(String month_year_id, Integer year, String quarter, Integer quarter_year_order, String month, Integer month_year_order) {
        this.month_year_id = month_year_id;
        this.year = year;
        this.quarter = quarter;
        this.quarter_year_order = quarter_year_order;
        this.month = month;
        this.month_year_order = month_year_order;
    }

    private String month_year_id;
    private Integer year;
    private String quarter;
    private Integer quarter_year_order;
    private String month;
    private Integer month_year_order;

    public String getMonth_year_id() {
        return month_year_id;
    }

    public void setMonth_year_id(String month_year_id) {
        this.month_year_id = month_year_id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public Integer getQuarter_year_order() {
        return quarter_year_order;
    }

    public void setQuarter_year_order(Integer quarter_year_order) {
        this.quarter_year_order = quarter_year_order;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getMonth_year_order() {
        return month_year_order;
    }

    public void setMonth_year_order(Integer month_year_order) {
        this.month_year_order = month_year_order;
    }
}
