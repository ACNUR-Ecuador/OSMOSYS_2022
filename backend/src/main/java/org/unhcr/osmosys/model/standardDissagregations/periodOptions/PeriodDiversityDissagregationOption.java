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
        super(period);
        this.diversityDissagregationOption=diversityDissagregationOption;
    }


    @ManyToOne(fetch = FetchType.LAZY, targetEntity = DiversityDissagregationOption.class)
    @JoinColumn(name = "dissagregation_option_id")
    private DiversityDissagregationOption diversityDissagregationOption;

    public DiversityDissagregationOption getDiversityDissagregationOption() {
        return diversityDissagregationOption;
    }

    public void setDiversityDissagregationOption(DiversityDissagregationOption diversityDissagregationOption) {
        this.diversityDissagregationOption = diversityDissagregationOption;
    }



    @Override
    public DiversityDissagregationOption getDissagregationOption() {
        return diversityDissagregationOption;
    }

    @Override
    public void setDissagregationOption(DiversityDissagregationOption dissagregationOption) {
        this.diversityDissagregationOption = dissagregationOption;
    }
}
