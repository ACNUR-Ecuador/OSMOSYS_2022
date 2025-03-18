package org.unhcr.osmosys.webServices.model;

import org.unhcr.osmosys.model.standardDissagregations.options.StandardDissagregationOption;
import org.unhcr.osmosys.webServices.model.standardDissagregations.StandardDissagregationOptionWeb;

import java.io.Serializable;
import java.util.Objects;

public class IndicatorExecutionDissagregationAssigmentWeb extends BaseWebEntity implements Serializable {
    public IndicatorExecutionDissagregationAssigmentWeb(){super();}
    private String dissagregationType;
    private StandardDissagregationOptionWeb disagregationOption;

    public String getDissagregationType() {
        return dissagregationType;
    }

    public void setDissagregationType(String dissagregationType) {
        this.dissagregationType = dissagregationType;
    }

    public StandardDissagregationOptionWeb getDisagregationOption() {
        return disagregationOption;
    }

    public void setDisagregationOption(StandardDissagregationOptionWeb disagregationOption) {
        this.disagregationOption = disagregationOption;
    }

    @Override
    public String toString() {
        return "IndicatorExecutionDissagregationAssigmentWeb{" +
                "dissagregationType='" + dissagregationType + '\'' +
                ", disagregationOption=" + disagregationOption +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        IndicatorExecutionDissagregationAssigmentWeb that = (IndicatorExecutionDissagregationAssigmentWeb) o;
        return Objects.equals(dissagregationType, that.dissagregationType) && Objects.equals(disagregationOption, that.disagregationOption);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dissagregationType, disagregationOption);
    }
}
