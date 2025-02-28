package org.unhcr.osmosys.webServices.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class CustomDissagregationWeb extends BaseWebEntity implements Serializable {

    public CustomDissagregationWeb() {
        super();
    }

    private String name;
    private String description;
    private Boolean controlTotalValue;

    private List<CustomDissagregationOptionWeb> customDissagregationOptions = new ArrayList<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getControlTotalValue() {
        return controlTotalValue;
    }

    public void setControlTotalValue(Boolean controlTotalValue) {
        this.controlTotalValue = controlTotalValue;
    }


    public List<CustomDissagregationOptionWeb> getCustomDissagregationOptions() {
        return customDissagregationOptions;
    }

    public void setCustomDissagregationOptions(List<CustomDissagregationOptionWeb> customDissagregationOptions) {
        this.customDissagregationOptions = customDissagregationOptions;
    }
}
