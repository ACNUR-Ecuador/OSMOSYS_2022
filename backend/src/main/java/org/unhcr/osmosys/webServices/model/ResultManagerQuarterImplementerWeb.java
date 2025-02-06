package org.unhcr.osmosys.webServices.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class ResultManagerQuarterImplementerWeb implements Serializable {
    public ResultManagerQuarterImplementerWeb() {}


    private IndicatorExecutionWeb IndicatorExecution;
    private BigDecimal quarterImplementerExecution;

    public IndicatorExecutionWeb getIndicatorExecution() {
        return IndicatorExecution;
    }

    public void setIndicatorExecution(IndicatorExecutionWeb indicatorExecution) {
        IndicatorExecution = indicatorExecution;
    }

    public BigDecimal getQuarterImplementerExecution() {
        return quarterImplementerExecution;
    }

    public void setQuarterImplementerExecution(BigDecimal quarterImplementerExecution) {
        this.quarterImplementerExecution = quarterImplementerExecution;
    }

    @Override
    public String toString() {
        return "ResultManagerQuarterImplementerWeb{" +
                "IndicatorExecution=" + IndicatorExecution +
                ", quarterImplementerExecution=" + quarterImplementerExecution +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultManagerQuarterImplementerWeb that = (ResultManagerQuarterImplementerWeb) o;
        return quarterImplementerExecution == that.quarterImplementerExecution && Objects.equals(IndicatorExecution, that.IndicatorExecution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(IndicatorExecution, quarterImplementerExecution);
    }
}
