package org.unhcr.osmosys.webServices.model;

import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.enums.DissagregationType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class DissagregationAssignationToIndicatorWeb implements Serializable {
    private Long id;
    private State state;
    private DissagregationType dissagregationType;
    private List<DissagregationFilterIndicatorWeb> dissagregationFilterIndicators = new ArrayList<>();

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

    public DissagregationType getDissagregationType() {
        return dissagregationType;
    }

    public void setDissagregationType(DissagregationType dissagregationType) {
        this.dissagregationType = dissagregationType;
    }

    public List<DissagregationFilterIndicatorWeb> getDissagregationFilterIndicators() {
        return dissagregationFilterIndicators;
    }

    public void setDissagregationFilterIndicators(List<DissagregationFilterIndicatorWeb> dissagregationFilterIndicators) {
        this.dissagregationFilterIndicators = dissagregationFilterIndicators;
    }
}
