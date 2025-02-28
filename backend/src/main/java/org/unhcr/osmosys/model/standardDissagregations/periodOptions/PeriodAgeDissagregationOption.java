package org.unhcr.osmosys.model.standardDissagregations.periodOptions;

import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.standardDissagregations.options.AgeDissagregationOption;

import javax.persistence.*;

@Entity(name = "PeriodAgeOption")
@DiscriminatorValue("age")
public class PeriodAgeDissagregationOption extends PeriodStandardDissagregationOption<AgeDissagregationOption> {

    public PeriodAgeDissagregationOption() {
        super();
    }


    public PeriodAgeDissagregationOption(Period period, AgeDissagregationOption ageDissagregationOption) {
        super(period);
        this.ageDissagregationOption=ageDissagregationOption;
    }


    @ManyToOne(fetch = FetchType.LAZY, targetEntity = AgeDissagregationOption.class)
    @JoinColumn(name = "dissagregation_option_id")
    private AgeDissagregationOption ageDissagregationOption;

    public AgeDissagregationOption getAgeDissagregationOption() {
        return ageDissagregationOption;
    }

    public void setAgeDissagregationOption(AgeDissagregationOption ageDissagregationOption) {
        this.ageDissagregationOption = ageDissagregationOption;
    }

    @Override
    public AgeDissagregationOption getDissagregationOption() {
        return this.ageDissagregationOption;
    }

    @Override
    public void setDissagregationOption(AgeDissagregationOption dissagregationOption) {
        this.ageDissagregationOption = dissagregationOption;
    }


}
