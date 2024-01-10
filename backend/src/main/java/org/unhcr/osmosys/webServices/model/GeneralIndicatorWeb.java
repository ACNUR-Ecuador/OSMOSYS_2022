package org.unhcr.osmosys.webServices.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.enums.MeasureType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class GeneralIndicatorWeb extends BaseWebEntity implements Serializable {

    public GeneralIndicatorWeb() {
        super();
    }

    private String description;
    private MeasureType measureType;
    private PeriodWeb period;

    private List<DissagregationAssignationToGeneralIndicatorWeb> dissagregationAssignationsToGeneralIndicator = new ArrayList<>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MeasureType getMeasureType() {
        return measureType;
    }

    public void setMeasureType(MeasureType measureType) {
        this.measureType = measureType;
    }

    public PeriodWeb getPeriod() {
        return period;
    }

    public void setPeriod(PeriodWeb period) {
        this.period = period;
    }

    public List<DissagregationAssignationToGeneralIndicatorWeb> getDissagregationAssignationsToGeneralIndicator() {
        return dissagregationAssignationsToGeneralIndicator;
    }

    public void setDissagregationAssignationsToGeneralIndicator(List<DissagregationAssignationToGeneralIndicatorWeb> dissagregationAssignationsToGeneralIndicator) {
        this.dissagregationAssignationsToGeneralIndicator = dissagregationAssignationsToGeneralIndicator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        GeneralIndicatorWeb that = (GeneralIndicatorWeb) o;

        return new EqualsBuilder().append(id, that.id).append(description, that.description).append(measureType, that.measureType).append(state, that.state).append(period, that.period).append(dissagregationAssignationsToGeneralIndicator, that.dissagregationAssignationsToGeneralIndicator).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(description).append(measureType).append(state).append(period).append(dissagregationAssignationsToGeneralIndicator).toHashCode();
    }
}
