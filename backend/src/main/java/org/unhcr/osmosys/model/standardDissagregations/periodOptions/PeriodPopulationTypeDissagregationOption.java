package org.unhcr.osmosys.model.standardDissagregations.periodOptions;

import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.standardDissagregations.options.PopulationTypeDissagregationOption;

import javax.persistence.*;

@Entity(name = "PeriodPopulationTypeOption")
@DiscriminatorValue("population_type")
public class PeriodPopulationTypeDissagregationOption extends PeriodStandardDissagregationOption<PopulationTypeDissagregationOption> {

    public PeriodPopulationTypeDissagregationOption() {
    }

    public PeriodPopulationTypeDissagregationOption(Period period, PopulationTypeDissagregationOption populationTypeDissagregationOption) {
        super(period);
        this.populationTypeDissagregationOption = populationTypeDissagregationOption;
    }


    @ManyToOne(fetch = FetchType.LAZY, targetEntity = PopulationTypeDissagregationOption.class)
    @JoinColumn(name = "dissagregation_option_id")
    private PopulationTypeDissagregationOption populationTypeDissagregationOption;


    @Override
    public PopulationTypeDissagregationOption getDissagregationOption() {
        return this.populationTypeDissagregationOption;
    }

    @Override
    public void setDissagregationOption(PopulationTypeDissagregationOption dissagregationOption) {
        this.populationTypeDissagregationOption = dissagregationOption;
    }

    public PopulationTypeDissagregationOption getPopulationTypeDissagregationOption() {
        return populationTypeDissagregationOption;
    }

    public void setPopulationTypeDissagregationOption(PopulationTypeDissagregationOption populationTypeDissagregationOption) {
        this.populationTypeDissagregationOption = populationTypeDissagregationOption;
    }
}
