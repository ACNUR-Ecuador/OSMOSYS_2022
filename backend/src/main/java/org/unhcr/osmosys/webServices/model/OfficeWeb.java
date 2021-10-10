package org.unhcr.osmosys.webServices.model;

import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.Office;
import org.unhcr.osmosys.model.enums.OfficeType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OfficeWeb implements Serializable {

    private Long id;

    private String description;

    private String acronym;

    private OfficeType type;

    private State state;

    private OfficeWeb parentOffice;

    private List<OfficeWeb> childOffices = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public OfficeType getType() {
        return type;
    }

    public void setType(OfficeType type) {
        this.type = type;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public OfficeWeb getParentOffice() {
        return parentOffice;
    }

    public void setParentOffice(OfficeWeb parentOffice) {
        this.parentOffice = parentOffice;
    }

    public List<OfficeWeb> getChildOffices() {
        return childOffices;
    }

    public void setChildOffices(List<OfficeWeb> childOffices) {
        this.childOffices = childOffices;
    }
}
