package org.unhcr.osmosys.webServices.model;

import org.unhcr.osmosys.webServices.model.standardDissagregations.StandardDissagregationOptionWeb;

import java.io.Serializable;

public class ResultManagerIndicatorDTO implements Serializable {
    public ResultManagerIndicatorDTO() {}

    private Long id;
    private IndicatorWeb indicator;
    private int quarterYearOrder;
    private StandardDissagregationOptionWeb populationType;
    private boolean confirmed;
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

    public StandardDissagregationOptionWeb getPopulationType() {
        return populationType;
    }

    public void setPopulationType(StandardDissagregationOptionWeb populationType) {
        this.populationType = populationType;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public PeriodWeb getPeriod() {
        return period;
    }

    public void setPeriod(PeriodWeb period) {
        this.period = period;
    }
}
