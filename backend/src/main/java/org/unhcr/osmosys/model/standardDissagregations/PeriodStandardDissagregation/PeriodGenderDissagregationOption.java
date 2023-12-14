package org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation;

import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.GenderDissagregationOption;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids.GenderDissagregationOptionPeriodId;

import javax.persistence.*;

@Entity
@Table(schema = "dissagregations", name = "period_gender_dissagregation_options")
public class PeriodGenderDissagregationOption
        extends PeriodStandardDissagregationOption<GenderDissagregationOption, GenderDissagregationOptionPeriodId> {

    public PeriodGenderDissagregationOption() {
    }




    public PeriodGenderDissagregationOption(Period period, GenderDissagregationOption genderDissagregationOption) {

        super(period, genderDissagregationOption, new GenderDissagregationOptionPeriodId(period.getId(), genderDissagregationOption.getId()));

    }

    @EmbeddedId
    private GenderDissagregationOptionPeriodId id = new GenderDissagregationOptionPeriodId();

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("dissagregationOptionId")
    private GenderDissagregationOption dissagregationOption;

    @Override
    public GenderDissagregationOption getDissagregationOption() {
        return this.dissagregationOption;
    }

    @Override
    public void setDissagregationOption(GenderDissagregationOption dissagregationOption) {
        this.dissagregationOption = dissagregationOption;
    }

    @Override
    public GenderDissagregationOptionPeriodId getId() {
        return id;
    }

    @Override
    public void setId(GenderDissagregationOptionPeriodId id) {
        this.id = id;
    }
}
