package org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation;

import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.DiversityDissagregationOption;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids.DiversityDissagregationOptionPeriodId;

import javax.persistence.*;

@Entity
@Table(schema = "dissagregations", name = "period_diversity_dissagregation_options")
public class PeriodDiversityDissagregationOption extends PeriodStandardDissagregationOption<DiversityDissagregationOption, DiversityDissagregationOptionPeriodId> {

    public PeriodDiversityDissagregationOption() {
    }

    public PeriodDiversityDissagregationOption(Period period, DiversityDissagregationOption diversityDissagregationOption) {
        super(period, diversityDissagregationOption, new DiversityDissagregationOptionPeriodId(period.getId(), diversityDissagregationOption.getId()));
    }

    @EmbeddedId
    private DiversityDissagregationOptionPeriodId id = new DiversityDissagregationOptionPeriodId();

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("dissagregationOptionId")
    private DiversityDissagregationOption dissagregationOption;

    @Override
    public DiversityDissagregationOptionPeriodId getId() {
        return id;
    }

    @Override
    public void setId(DiversityDissagregationOptionPeriodId id) {
        this.id = id;
    }

    @Override
    public DiversityDissagregationOption getDissagregationOption() {
        return dissagregationOption;
    }

    @Override
    public void setDissagregationOption(DiversityDissagregationOption dissagregationOption) {
        this.dissagregationOption = dissagregationOption;
    }
}
