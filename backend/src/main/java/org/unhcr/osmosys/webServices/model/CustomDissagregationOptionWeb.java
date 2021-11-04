package org.unhcr.osmosys.webServices.model;

import com.sagatechs.generics.persistence.model.State;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class CustomDissagregationOptionWeb implements Serializable {
    private Long id;
    private String name;
    private String description;
    private State state;
    private List<MarkerWeb> markers = new ArrayList<>();

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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public List<MarkerWeb> getMarkers() {
        return markers;
    }

    public void setMarkers(List<MarkerWeb> markers) {
        this.markers = markers;
    }
}
