package org.unhcr.osmosys.webServices.model;

import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.Office;
import org.unhcr.osmosys.model.Provincia;

import javax.persistence.*;
import java.io.Serializable;


public class CantonWeb implements Serializable {
    private Long id;

    private String code;

    private String description;

    private State state;

    private ProvinciaWeb provincia;

    private OfficeWeb office;

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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public ProvinciaWeb getProvincia() {
        return provincia;
    }

    public void setProvincia(ProvinciaWeb provincia) {
        this.provincia = provincia;
    }

    public OfficeWeb getOffice() {
        return office;
    }

    public void setOffice(OfficeWeb office) {
        this.office = office;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CantonWeb cantonWeb = (CantonWeb) o;

        return new EqualsBuilder().append(id, cantonWeb.id).append(code, cantonWeb.code).append(description, cantonWeb.description).append(provincia, cantonWeb.provincia).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(code).append(description).append(provincia).toHashCode();
    }
}
