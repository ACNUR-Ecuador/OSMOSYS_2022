package org.unhcr.osmosys.webServices.model;

import com.sagatechs.generics.persistence.model.State;

import java.io.Serializable;
import java.util.Objects;


public class PillarWeb implements Serializable {
    private Long id;
    private String code;
    private String shortDescription;
    private String description;
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

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
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

    @Override
    public String toString() {
        return "PillarWeb{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", description='" + description + '\'' +
                ", state=" + state +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PillarWeb)) return false;
        PillarWeb pillarWeb = (PillarWeb) o;
        return code.equals(pillarWeb.code) && description.equals(pillarWeb.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, description);
    }
}
