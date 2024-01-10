package org.unhcr.osmosys.webServices.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.unhcr.osmosys.model.enums.DissagregationType;
import org.unhcr.osmosys.webServices.model.standardDissagregations.StandardDissagregationOptionWeb;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public class DissagregationAssignationToIndicatorWeb extends BaseWebEntity implements Serializable {

    private DissagregationType dissagregationType;
    private PeriodWeb period;

    private Boolean useCustomAgeDissagregations= Boolean.FALSE;

    private Set<StandardDissagregationOptionWeb> customIndicatorOptions = new HashSet<>();


    public DissagregationType getDissagregationType() {
        return dissagregationType;
    }

    public void setDissagregationType(DissagregationType dissagregationType) {
        this.dissagregationType = dissagregationType;
    }


    public PeriodWeb getPeriod() {
        return period;
    }

    public void setPeriod(PeriodWeb period) {
        this.period = period;
    }

    public Boolean getUseCustomAgeDissagregations() {
        return useCustomAgeDissagregations;
    }

    public void setUseCustomAgeDissagregations(Boolean useCustomAgeDissagregations) {
        this.useCustomAgeDissagregations = useCustomAgeDissagregations;
    }

    public Set<StandardDissagregationOptionWeb> getCustomIndicatorOptions() {
        return this.customIndicatorOptions;
    }

    public void setCustomIndicatorOptions(Set<StandardDissagregationOptionWeb> customIndicatorOptions) {
        this.customIndicatorOptions = customIndicatorOptions;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("dissagregationType", dissagregationType)
                .append("period", period)
                .append("useCustomAgeDissagregations", useCustomAgeDissagregations)
                .append("customIndicatorOptions", customIndicatorOptions)
                .append("id", id)
                .append("state", state)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof DissagregationAssignationToIndicatorWeb)) return false;

        DissagregationAssignationToIndicatorWeb that = (DissagregationAssignationToIndicatorWeb) o;

        return new EqualsBuilder().appendSuper(super.equals(o)).append(dissagregationType, that.dissagregationType).append(period, that.period).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(dissagregationType).append(period).toHashCode();
    }
}
