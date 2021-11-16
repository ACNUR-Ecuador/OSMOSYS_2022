package org.unhcr.osmosys.webServices.model;

import com.sagatechs.generics.persistence.model.State;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class CustomDissagregationWeb implements Serializable {
    private Long id;
    private String name;
    private String description;
    private Boolean controlTotalValue;
    private State state;
    private List<CustomDissagregationOptionWeb> customDissagregationOptions = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public List<CustomDissagregationOptionWeb> getCustomDissagregationOptions() {
        return customDissagregationOptions;
    }

    public void setCustomDissagregationOptions(List<CustomDissagregationOptionWeb> customDissagregationOptions) {
        this.customDissagregationOptions = customDissagregationOptions;
    }
}
