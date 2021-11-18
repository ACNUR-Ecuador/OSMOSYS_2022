package org.unhcr.osmosys.webServices.model;

import com.sagatechs.generics.persistence.model.State;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class CustomDissagregationFilterIndicatorWeb implements Serializable {

    private Long id;
    private State state;
    private List<CustomDissagregationOptionWeb> customDissagregationOptions = new ArrayList<>();

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

    public List<CustomDissagregationOptionWeb> getCustomDissagregationOptions() {
        return customDissagregationOptions;
    }

    public void setCustomDissagregationOptions(List<CustomDissagregationOptionWeb> customDissagregationOptions) {
        this.customDissagregationOptions = customDissagregationOptions;
    }
}
