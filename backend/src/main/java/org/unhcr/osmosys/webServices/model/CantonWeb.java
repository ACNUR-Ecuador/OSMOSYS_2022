package org.unhcr.osmosys.webServices.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.webServices.model.standardDissagregations.StandardDissagregationOptionWeb;

import java.io.Serializable;


public class CantonWeb extends StandardDissagregationOptionWeb implements Serializable {

    public CantonWeb() {
        super();
    }

    private String code;


    private ProvinciaWeb provincia;

    private OfficeWeb office;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

        return new EqualsBuilder().append(id, cantonWeb.id).append(code, cantonWeb.code).append(name, cantonWeb.name).append(provincia, cantonWeb.provincia).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(code).append(name).append(provincia).toHashCode();
    }
}
