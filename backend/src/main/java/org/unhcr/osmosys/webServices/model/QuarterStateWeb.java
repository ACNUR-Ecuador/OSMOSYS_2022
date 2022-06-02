package org.unhcr.osmosys.webServices.model;

import java.io.Serializable;
import java.util.Objects;


public class QuarterStateWeb implements Serializable {

    public QuarterStateWeb() {
    }

    public QuarterStateWeb(String quarter, Integer year, Boolean blockUpdate, Integer quarterYearOrder) {
        this.quarter = quarter;
        this.year = year;
        this.blockUpdate = blockUpdate;
        this.quarterYearOrder = quarterYearOrder;
    }

    private String quarter;
    private Integer year;
    private Boolean blockUpdate;
    private Integer quarterYearOrder;

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Boolean getBlockUpdate() {
        return blockUpdate;
    }

    public void setBlockUpdate(Boolean blockUpdate) {
        this.blockUpdate = blockUpdate;
    }

    public Integer getQuarterYearOrder() {
        return quarterYearOrder;
    }

    public void setQuarterYearOrder(Integer quarterYearOrder) {
        this.quarterYearOrder = quarterYearOrder;
    }

    public Integer getQuarter_year_order() {
        return quarterYearOrder;
    }

    public void setQuarter_year_order(Integer quarterYearOrder) {
        this.quarterYearOrder = quarterYearOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuarterStateWeb that = (QuarterStateWeb) o;
        return Objects.equals(quarter, that.quarter) && Objects.equals(year, that.year) && blockUpdate == that.blockUpdate && Objects.equals(quarterYearOrder, that.quarterYearOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quarter, year, blockUpdate, quarterYearOrder);
    }

    @Override
    public String toString() {
        return "QuarterStateWeb{" +
                "quarter='" + quarter + '\'' +
                ", year=" + year +
                ", blockUpdate=" + blockUpdate +
                ", quarterYearOrder=" + quarterYearOrder +
                '}';
    }
}
