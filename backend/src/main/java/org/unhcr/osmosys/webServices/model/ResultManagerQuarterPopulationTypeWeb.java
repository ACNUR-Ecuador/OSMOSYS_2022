package org.unhcr.osmosys.webServices.model;

import org.unhcr.osmosys.webServices.model.standardDissagregations.StandardDissagregationOptionWeb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class ResultManagerQuarterPopulationTypeWeb implements Serializable {
    public ResultManagerQuarterPopulationTypeWeb() {}

    public ResultManagerQuarterPopulationTypeWeb(Long id, BigDecimal quarterPopulationTypeExecution, StandardDissagregationOptionWeb populationType, boolean confirmation, Integer reportValue) {
        this.id = id;
        this.quarterPopulationTypeExecution = quarterPopulationTypeExecution;
        this.populationType = populationType;
        this.confirmation = confirmation;
        this.reportValue = reportValue;
    }

    private Long id;
    private BigDecimal quarterPopulationTypeExecution;
    private StandardDissagregationOptionWeb populationType;
    private boolean confirmation;
    private Integer reportValue;

    public BigDecimal getQuarterPopulationTypeExecution() {
        return quarterPopulationTypeExecution;
    }

    public void setQuarterPopulationTypeExecution(BigDecimal quarterPopulationTypeExecution) {
        this.quarterPopulationTypeExecution = quarterPopulationTypeExecution;
    }

    public StandardDissagregationOptionWeb getPopulationType() {
        return populationType;
    }

    public void setPopulationType(StandardDissagregationOptionWeb populationType) {
        this.populationType = populationType;
    }

    public boolean isConfirmation() {
        return confirmation;
    }

    public void setConfirmation(boolean confirmation) {
        this.confirmation = confirmation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getReportValue() {
        return reportValue;
    }

    public void setReportValue(Integer reportValue) {
        this.reportValue = reportValue;
    }

    @Override
    public String toString() {
        return "ResultManagerQuarterPopulationTypeWeb{" +
                "id=" + id +
                ", quarterPopulationTypeExecution=" + quarterPopulationTypeExecution +
                ", populationType=" + populationType +
                ", confirmation=" + confirmation +
                ", reportValue=" + reportValue +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultManagerQuarterPopulationTypeWeb that = (ResultManagerQuarterPopulationTypeWeb) o;
        return confirmation == that.confirmation && Objects.equals(id, that.id) && Objects.equals(quarterPopulationTypeExecution, that.quarterPopulationTypeExecution) && Objects.equals(populationType, that.populationType) && Objects.equals(reportValue, that.reportValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quarterPopulationTypeExecution, populationType, confirmation, reportValue);
    }
}
