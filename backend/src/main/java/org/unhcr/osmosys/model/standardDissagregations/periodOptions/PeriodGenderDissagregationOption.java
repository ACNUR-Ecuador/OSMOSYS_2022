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

        super(period);
        this.genderDissagregationOption=genderDissagregationOption;

    }


    @ManyToOne(fetch = FetchType.LAZY, targetEntity = GenderDissagregationOption.class)
    @JoinColumn(name = "dissagregation_option_id")
    private GenderDissagregationOption genderDissagregationOption;

    public GenderDissagregationOption getGenderDissagregationOption() {
        return genderDissagregationOption;
    }

    public void setGenderDissagregationOption(GenderDissagregationOption genderDissagregationOption) {
        this.genderDissagregationOption = genderDissagregationOption;
    }

    @Override
    public GenderDissagregationOption getDissagregationOption() {
        return genderDissagregationOption;
    }

    @Override
    public void setDissagregationOption(GenderDissagregationOption dissagregationOption) {
        this.genderDissagregationOption = dissagregationOption;
    }
}
