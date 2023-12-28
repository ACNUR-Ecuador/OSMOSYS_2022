package org.unhcr.osmosys.model.standardDissagregations.periodOptions;

import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.standardDissagregations.options.GenderDissagregationOption;

import javax.persistence.*;

@Entity(name = "PeriodGenderOption")
@DiscriminatorValue("gender")
public class PeriodGenderDissagregationOption
        extends PeriodStandardDissagregationOption<GenderDissagregationOption> {

    public PeriodGenderDissagregationOption() {
    }




    public PeriodGenderDissagregationOption(Period period, GenderDissagregationOption genderDissagregationOption) {

        super(period, genderDissagregationOption);

    }


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dissagregation_option_id")
    private GenderDissagregationOption dissagregationOption;

    @Override
    public GenderDissagregationOption getDissagregationOption() {
        return this.dissagregationOption;
    }

    @Override
    public void setDissagregationOption(GenderDissagregationOption dissagregationOption) {
        this.dissagregationOption = dissagregationOption;
    }

}
