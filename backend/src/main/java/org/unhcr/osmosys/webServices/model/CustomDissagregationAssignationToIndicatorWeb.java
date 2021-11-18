package org.unhcr.osmosys.webServices.model;

import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.CustomDissagregation;
import org.unhcr.osmosys.model.CustomDissagregationFilterIndicator;
import org.unhcr.osmosys.model.Indicator;
import org.unhcr.osmosys.model.enums.AreaType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class CustomDissagregationAssignationToIndicatorWeb implements Serializable {

    private Long id;
    private IndicatorWeb indicator;
    private State state;
    private CustomDissagregationWeb customDissagregation;
    private List<CustomDissagregationFilterIndicatorWeb> customDissagregationFilterIndicators = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IndicatorWeb getIndicator() {
        return indicator;
    }

    public void setIndicator(IndicatorWeb indicator) {
        this.indicator = indicator;
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
