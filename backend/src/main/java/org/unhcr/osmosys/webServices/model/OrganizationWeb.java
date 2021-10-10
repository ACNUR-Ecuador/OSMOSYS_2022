package org.unhcr.osmosys.webServices.model;

import com.sagatechs.generics.persistence.model.State;

import javax.ejb.Stateless;
import java.io.Serializable;

@Stateless
public class OrganizationWeb implements Serializable {

    private Long id;
    private String code;
    private String description;
    private String acronym;
    private State state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
