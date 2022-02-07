package org.unhcr.osmosys.webServices.model;

import org.unhcr.osmosys.model.enums.IndicatorType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class TargetUpdateDTOWeb implements Serializable {

    private Long indicatorExecutionId;
    private IndicatorType indicatorType;
    private BigDecimal totalTarget;
    private List<QuarterWeb> quarters = new ArrayList<>();

    public Long getIndicatorExecutionId() {
        return indicatorExecutionId;
    }

    public void setIndicatorExecutionId(Long indicatorExecutionId) {
        this.indicatorExecutionId = indicatorExecutionId;
    }

    public List<QuarterWeb> getQuarters() {
        return quarters;
    }

    public void setQuarters(List<QuarterWeb> quarters) {
        this.quarters = quarters;
    }

    public IndicatorType getIndicatorType() {
        return indicatorType;
    }

    public void setIndicatorType(IndicatorType indicatorType) {
        this.indicatorType = indicatorType;
    }

    public BigDecimal getTotalTarget() {
        return totalTarget;
    }

    public void setTotalTarget(BigDecimal totalTarget) {
        this.totalTarget = totalTarget;
    }
}
