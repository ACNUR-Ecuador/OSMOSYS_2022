package org.unhcr.osmosys.webServices.model;

import java.io.Serializable;

public class ResultManagerIndicatorQuarterReportDTO implements Serializable {
    public ResultManagerIndicatorQuarterReportDTO() {}

    private Long id;
    private IndicatorWeb indicator;
    private int quarterYearOrder;
    private Boolean allReportSumConfirmation;
    private String reportComment;
    private Integer newReportValue;
    private PeriodWeb period;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IndicatorWeb getIndicator() {
        return indicator;
    }

    public void setIndicator(IndicatorWeb indicator) {
        this.indicator = indicator;
    }

    public int getQuarterYearOrder() {
        return quarterYearOrder;
    }

    public void setQuarterYearOrder(int quarterYearOrder) {
        this.quarterYearOrder = quarterYearOrder;
    }

    public Boolean isAllReportSumConfirmation() {
        return allReportSumConfirmation;
    }

    public void setAllReportSumConfirmation(Boolean allReportSumConfirmation) {
        this.allReportSumConfirmation = allReportSumConfirmation;
    }

    public String getReportComment() {
        return reportComment;
    }

    public void setReportComment(String reportComment) {
        this.reportComment = reportComment;
    }

    public Integer getNewReportValue() {
        return newReportValue;
    }

    public void setNewReportValue(Integer newReportValue) {
        this.newReportValue = newReportValue;
    }

    public PeriodWeb getPeriod() {
        return period;
    }

    public void setPeriod(PeriodWeb period) {
        this.period = period;
    }
}
