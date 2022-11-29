package org.unhcr.osmosys.webServices.model;

import java.io.Serializable;
import java.util.List;

public class AreaResumeWeb implements Serializable {

    AreaWeb area;
    Integer numberOfIndicators;
    Integer numberOfLateIndicators;
    Integer numberOfSoonReportIndicators;
    List<IndicatorWeb> indicators;
    private List<Long> indicatorExecutionIds;

    public Integer getNumberOfIndicators() {
        return numberOfIndicators;
    }

    public void setNumberOfIndicators(Integer numberOfIndicators) {
        this.numberOfIndicators = numberOfIndicators;
    }

    public Integer getNumberOfLateIndicators() {
        return numberOfLateIndicators;
    }

    public void setNumberOfLateIndicators(Integer numberOfLateIndicators) {
        this.numberOfLateIndicators = numberOfLateIndicators;
    }

    public List<IndicatorWeb> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<IndicatorWeb> indicators) {
        this.indicators = indicators;
    }

    public AreaWeb getArea() {
        return area;
    }

    public void setArea(AreaWeb area) {
        this.area = area;
    }

    public List<Long> getIndicatorExecutionIds() {
        return indicatorExecutionIds;
    }

    public void setIndicatorExecutionIds(List<Long> indicatorExecutionIds) {
        this.indicatorExecutionIds = indicatorExecutionIds;
    }

    public Integer getNumberOfSoonReportIndicators() {
        return numberOfSoonReportIndicators;
    }

    public void setNumberOfSoonReportIndicators(Integer numberOfSoonReportIndicators) {
        this.numberOfSoonReportIndicators = numberOfSoonReportIndicators;
    }
}
