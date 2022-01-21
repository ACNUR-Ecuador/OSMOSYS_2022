package org.unhcr.osmosys.webServices.model;

import java.util.List;


public class CustomDissagregationValuesWeb {

    CustomDissagregationWeb customDissagregation;
    List<IndicatorValueCustomDissagregationWeb> indicatorValuesCustomDissagregation;

    public List<IndicatorValueCustomDissagregationWeb> getIndicatorValuesCustomDissagregation() {
        return indicatorValuesCustomDissagregation;
    }

    public void setIndicatorValuesCustomDissagregation(List<IndicatorValueCustomDissagregationWeb> indicatorValuesCustomDissagregation) {
        this.indicatorValuesCustomDissagregation = indicatorValuesCustomDissagregation;
    }

    public CustomDissagregationWeb getCustomDissagregation() {
        return customDissagregation;
    }

    public void setCustomDissagregation(CustomDissagregationWeb customDissagregation) {
        this.customDissagregation = customDissagregation;
    }
}
