package org.unhcr.osmosys.webServices.model;

import org.unhcr.osmosys.webServices.model.standardDissagregations.StandardDissagregationOptionWeb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class ResultManagerQuarterPopulationTypeWeb implements Serializable {
    public ResultManagerQuarterPopulationTypeWeb() {}

    public ResultManagerQuarterPopulationTypeWeb(BigDecimal quarterPopulationTypeExecution, StandardDissagregationOptionWeb populationType) {
        this.quarterPopulationTypeExecution = quarterPopulationTypeExecution;
        this.populationType = populationType;
    }

    private BigDecimal quarterPopulationTypeExecution;
    private StandardDissagregationOptionWeb populationType;

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

    @Override
    public String toString() {
        return "ResultManagerQuarterPopulationTypeWeb{" +
                "quarterPopulationTypeExecution=" + quarterPopulationTypeExecution +
                ", populationType=" + populationType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultManagerQuarterPopulationTypeWeb that = (ResultManagerQuarterPopulationTypeWeb) o;
        return quarterPopulationTypeExecution == that.quarterPopulationTypeExecution && Objects.equals(populationType, that.populationType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quarterPopulationTypeExecution, populationType);
    }
}
