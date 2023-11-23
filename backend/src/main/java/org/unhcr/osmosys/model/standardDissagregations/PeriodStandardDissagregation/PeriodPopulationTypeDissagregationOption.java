package org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation;

import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.AgeDissagregationOption;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.PopulationTypeDissagregationOption;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids.AgeDissagregationOptionPeriodId;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids.PopulationTypeDissagregationOptionPeriodId;

import javax.persistence.*;

@Entity
@Table(schema = "dissagregations", name = "period_population_type_dissagregation_options")
public class PeriodPopulationTypeDissagregationOption extends PeriodStandardDissagregationOption<PopulationTypeDissagregationOption, PopulationTypeDissagregationOptionPeriodId>{

    public PeriodPopulationTypeDissagregationOption() {
    }

    public PeriodPopulationTypeDissagregationOption(Period period, PopulationTypeDissagregationOption populationTypeDissagregationOption) {
        super(period,populationTypeDissagregationOption, new PopulationTypeDissagregationOptionPeriodId(period.getId(), populationTypeDissagregationOption.getId()));
    }

    @EmbeddedId
    private PopulationTypeDissagregationOptionPeriodId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("dissagregationOptionId")
    private PopulationTypeDissagregationOption dissagregationOption;

    @Override
    public PopulationTypeDissagregationOptionPeriodId getId() {
        return id;
    }

    @Override
    public void setId(PopulationTypeDissagregationOptionPeriodId id) {
        this.id = id;
    }

    @Override
    public PopulationTypeDissagregationOption getDissagregationOption() {
        return dissagregationOption;
    }

    @Override
    public void setDissagregationOption(PopulationTypeDissagregationOption dissagregationOption) {
        this.dissagregationOption = dissagregationOption;
    }
}
