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
        super(period, ageDissagregationOption);
    }


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dissagregation_option_id")
    private AgeDissagregationOption dissagregationOption;


    @Override
    public AgeDissagregationOption getDissagregationOption() {
        return this.dissagregationOption;
    }

    @Override
    public void setDissagregationOption(AgeDissagregationOption dissagregationOption) {
        this.dissagregationOption = dissagregationOption;
    }


}
