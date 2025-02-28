package org.unhcr.osmosys.webServices.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class ResultManagerIndicatorWeb implements Serializable {

    public ResultManagerIndicatorWeb() {}
    private IndicatorWeb indicator;
    private BigDecimal anualTarget;
    private BigDecimal anualExecution;
    private boolean hasExecutions;
    private List<ResultManagerIndicatorQuarterWeb> resultManagerIndicatorQuarter;

    public IndicatorWeb getIndicator() {
        return indicator;
    }

    public void setIndicator(IndicatorWeb indicator) {
        this.indicator = indicator;
    }

    public BigDecimal getAnualTarget() {
        return anualTarget;
    }

    public void setAnualTarget(BigDecimal anualTarget) {
        this.anualTarget = anualTarget;
    }

    public List<ResultManagerIndicatorQuarterWeb> getResultManagerIndicatorQuarter() {
        return resultManagerIndicatorQuarter;
    }

    public void setResultManagerIndicatorQuarter(List<ResultManagerIndicatorQuarterWeb> resultManagerIndicatorQuarter) {
        this.resultManagerIndicatorQuarter = resultManagerIndicatorQuarter;
    }

    public BigDecimal getAnualExecution() {
        return anualExecution;
    }

    public void setAnualExecution(BigDecimal anualExecution) {
        this.anualExecution = anualExecution;
    }

    public boolean isHasExecutions() {
        return hasExecutions;
    }

    public void setHasExecutions(boolean hasExecutions) {
        this.hasExecutions = hasExecutions;
    }

    @Override
    public String toString() {
        return "ResultManagerIndicatorWeb{" +
                "indicator=" + indicator +
                ", anualTarget=" + anualTarget +
                ", anualExecution=" + anualExecution +
                ", hasExecutions=" + hasExecutions +
                ", resultManagerIndicatorQuarter=" + resultManagerIndicatorQuarter +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultManagerIndicatorWeb that = (ResultManagerIndicatorWeb) o;
        return hasExecutions == that.hasExecutions && Objects.equals(indicator, that.indicator) && Objects.equals(anualTarget, that.anualTarget) && Objects.equals(anualExecution, that.anualExecution) && Objects.equals(resultManagerIndicatorQuarter, that.resultManagerIndicatorQuarter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(indicator, anualTarget, anualExecution, hasExecutions, resultManagerIndicatorQuarter);
    }
}
