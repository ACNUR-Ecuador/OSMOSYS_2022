package org.unhcr.osmosys.model.standardDissagregations.periodOptions;

import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.standardDissagregations.options.DiversityDissagregationOption;

import javax.persistence.*;

@Entity(name = "PeriodDiversityOption")
@DiscriminatorValue("diversity")
public class PeriodDiversityDissagregationOption extends PeriodStandardDissagregationOption<DiversityDissagregationOption> {

    public PeriodDiversityDissagregationOption() {
    }

    public PeriodDiversityDissagregationOption(Period period, DiversityDissagregationOption diversityDissagregationOption) {
        super(period, diversityDissagregationOption);
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dissagregation_option_id")
    private DiversityDissagregationOption dissagregationOption;



    @Override
    public DiversityDissagregationOption getDissagregationOption() {
        return dissagregationOption;
    }

    @Override
    public void setDissagregationOption(DiversityDissagregationOption dissagregationOption) {
        this.dissagregationOption = dissagregationOption;
    }
}
