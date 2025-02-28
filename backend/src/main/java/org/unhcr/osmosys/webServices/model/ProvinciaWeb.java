package org.unhcr.osmosys.webServices.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;


public class ProvinciaWeb extends BaseWebEntity implements Serializable {

    public ProvinciaWeb() {
        super();
    }

    private String code;

    private String description;


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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ProvinciaWeb that = (ProvinciaWeb) o;

        return new EqualsBuilder().append(id, that.id).append(code, that.code).append(description, that.description).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(code).append(description).toHashCode();
    }
}
