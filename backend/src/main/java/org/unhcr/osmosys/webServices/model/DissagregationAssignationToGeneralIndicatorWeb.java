package org.unhcr.osmosys.webServices.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.enums.DissagregationType;

import java.io.Serializable;


public class DissagregationAssignationToGeneralIndicatorWeb extends BaseWebEntity implements Serializable {

    public DissagregationAssignationToGeneralIndicatorWeb() {
        super();
    }

    private DissagregationType dissagregationType;

    public DissagregationType getDissagregationType() {
        return dissagregationType;
    }

    public void setDissagregationType(DissagregationType dissagregationType) {
        this.dissagregationType = dissagregationType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DissagregationAssignationToGeneralIndicatorWeb that = (DissagregationAssignationToGeneralIndicatorWeb) o;

        return new EqualsBuilder().append(id, that.id).append(dissagregationType, that.dissagregationType).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(dissagregationType).toHashCode();
    }
}
