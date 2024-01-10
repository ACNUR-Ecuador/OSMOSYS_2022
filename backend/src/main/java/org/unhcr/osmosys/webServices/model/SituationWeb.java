package org.unhcr.osmosys.webServices.model;

import java.io.Serializable;
import java.util.Objects;


public class SituationWeb extends BaseWebEntity implements Serializable {

    public SituationWeb() {
        super();
    }

    private String code;
    private String shortDescription;
    private String description;

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

    @Override
    public String toString() {
        return "SituationWeb{" +
                "id=" + id +
                ", state=" + state +
                ", code='" + code + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SituationWeb)) return false;
        SituationWeb that = (SituationWeb) o;
        return code.equals(that.code) && description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, description);
    }
}
