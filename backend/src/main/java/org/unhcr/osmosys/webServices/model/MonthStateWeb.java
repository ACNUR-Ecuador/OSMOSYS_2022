package org.unhcr.osmosys.webServices.model;

import org.unhcr.osmosys.model.enums.MonthEnum;

import java.io.Serializable;
import java.util.Objects;


public class MonthStateWeb implements Serializable {

    public MonthStateWeb() {
    }

    public MonthStateWeb(Integer year, String month, Integer order, Boolean blockUpdate) {
        this.year = year;
        this.month = MonthEnum.valueOf(month);
        this.order = order;
        this.blockUpdate = blockUpdate;
    }

    private int year;
    private MonthEnum month;
    private int order;
    private Boolean blockUpdate;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public MonthEnum getMonth() {
        return month;
    }

    public void setMonth(MonthEnum month) {
        this.month = month;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Boolean getBlockUpdate() {
        return blockUpdate;
    }

    public void setBlockUpdate(Boolean blockUpdate) {
        this.blockUpdate = blockUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonthStateWeb that = (MonthStateWeb) o;
        return year == that.year && order == that.order && month == that.month && Objects.equals(blockUpdate, that.blockUpdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, order, blockUpdate);
    }

    @Override
    public String toString() {
        return "MonthStateWeb{" +
                "year=" + year +
                ", month=" + month +
                ", order=" + order +
                ", blockUpdate=" + blockUpdate +
                '}';
    }
}
