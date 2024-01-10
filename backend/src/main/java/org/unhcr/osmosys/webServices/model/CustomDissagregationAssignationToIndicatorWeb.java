package org.unhcr.osmosys.webServices.model;

import java.io.Serializable;


public class CustomDissagregationAssignationToIndicatorWeb extends BaseWebEntity implements Serializable {

    public CustomDissagregationAssignationToIndicatorWeb() {
        super();
    }

    private CustomDissagregationWeb customDissagregation;
    private PeriodWeb period;


    public CustomDissagregationWeb getCustomDissagregation() {
        return customDissagregation;
    }

    public void setCustomDissagregation(CustomDissagregationWeb customDissagregation) {
        this.customDissagregation = customDissagregation;
    }

    public PeriodWeb getPeriod() {
        return period;
    }

    public void setPeriod(PeriodWeb period) {
        this.period = period;
    }
}
