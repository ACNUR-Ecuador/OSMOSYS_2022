package org.unhcr.osmosys.webServices.model;

import org.unhcr.osmosys.webServices.model.standardDissagregations.StandardDissagregationOptionWeb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class ResultManagerQuarterPopulationTypeWeb implements Serializable {
    public ResultManagerQuarterPopulationTypeWeb() {}

    public ResultManagerQuarterPopulationTypeWeb(BigDecimal quarterPopulationTypeExecution, StandardDissagregationOptionWeb populationType, boolean confirmation) {
        this.quarterPopulationTypeExecution = quarterPopulationTypeExecution;
        this.populationType = populationType;
        this.confirmation = confirmation;
    }

    private Long id;
    private BigDecimal quarterPopulationTypeExecution;
    private StandardDissagregationOptionWeb populationType;
    private boolean confirmation;

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

    @Override
    public String toString() {
        return "ResultManagerQuarterPopulationTypeWeb{" +
                "id=" + id +
                ", quarterPopulationTypeExecution=" + quarterPopulationTypeExecution +
                ", populationType=" + populationType +
                ", confirmation=" + confirmation +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultManagerQuarterPopulationTypeWeb that = (ResultManagerQuarterPopulationTypeWeb) o;
        return confirmation == that.confirmation && Objects.equals(id, that.id) && Objects.equals(quarterPopulationTypeExecution, that.quarterPopulationTypeExecution) && Objects.equals(populationType, that.populationType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quarterPopulationTypeExecution, populationType, confirmation);
    }
}
