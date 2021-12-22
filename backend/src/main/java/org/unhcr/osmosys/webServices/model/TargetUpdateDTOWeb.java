package org.unhcr.osmosys.webServices.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class TargetUpdateDTOWeb implements Serializable {

    private Long indicatorExecutionId;
    private List<QuarterResumeWeb> quarters = new ArrayList<>();

    public Long getIndicatorExecutionId() {
        return indicatorExecutionId;
    }

    public void setIndicatorExecutionId(Long indicatorExecutionId) {
        this.indicatorExecutionId = indicatorExecutionId;
    }

    public List<QuarterResumeWeb> getQuarters() {
        return quarters;
    }

    public void setQuarters(List<QuarterResumeWeb> quarters) {
        this.quarters = quarters;
    }
}
