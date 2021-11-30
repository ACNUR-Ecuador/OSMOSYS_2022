package org.unhcr.osmosys.webServices.model;

import com.sagatechs.generics.persistence.model.State;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class CustomDissagregationAssignationToIndicatorWeb implements Serializable {

    private Long id;

    private State state;
    private CustomDissagregationWeb customDissagregation;
    private List<CustomDissagregationFilterIndicatorWeb> customDissagregationFilterIndicators = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public CustomDissagregationWeb getCustomDissagregation() {
        return customDissagregation;
    }

    public void setCustomDissagregation(CustomDissagregationWeb customDissagregation) {
        this.customDissagregation = customDissagregation;
    }

    public List<CustomDissagregationFilterIndicatorWeb> getCustomDissagregationFilterIndicators() {
        return customDissagregationFilterIndicators;
    }

    public void setCustomDissagregationFilterIndicators(List<CustomDissagregationFilterIndicatorWeb> customDissagregationFilterIndicators) {
        this.customDissagregationFilterIndicators = customDissagregationFilterIndicators;
    }
}
