package org.unhcr.osmosys.webServices.model;

import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.enums.AreaType;

import java.io.Serializable;
import java.util.Objects;


public class AreaWeb extends BaseWebEntity implements Serializable {

    public AreaWeb() {
    }

    private AreaType areaType;
    private String code;
    private String shortDescription;
    private String description;
    private String definition;


    public AreaType getAreaType() {
        return areaType;
    }

    public void setAreaType(AreaType areaType) {
        this.areaType = areaType;
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

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    @Override
    public String toString() {
        return "AreaWeb{" +
                "id=" + id +
                ", state=" + state +
                ", areaType=" + areaType +
                ", code='" + code + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", description='" + description + '\'' +
                ", definition='" + definition + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AreaWeb)) return false;
        AreaWeb areaWeb = (AreaWeb) o;
        return areaType == areaWeb.areaType && code.equals(areaWeb.code) && description.equals(areaWeb.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(areaType, code, description);
    }
}
