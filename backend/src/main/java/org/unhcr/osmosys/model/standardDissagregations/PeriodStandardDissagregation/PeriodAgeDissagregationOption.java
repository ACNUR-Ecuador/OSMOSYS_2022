package org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation;

import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.AgeDissagregationOption;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.ids.AgeDissagregationOptionPeriodId;

import javax.persistence.*;

@Entity
@Table(schema = "dissagregations", name = "period_age_dissagregation_options")
public class PeriodAgeDissagregationOption extends PeriodStandardDissagregationOption<AgeDissagregationOption, AgeDissagregationOptionPeriodId> {

    public PeriodAgeDissagregationOption() {
        super();
    }


    public PeriodAgeDissagregationOption(Period period, AgeDissagregationOption ageDissagregationOption) {
        super(period, ageDissagregationOption, new AgeDissagregationOptionPeriodId(period.getId(), ageDissagregationOption.getId()));
    }

    @EmbeddedId
    private AgeDissagregationOptionPeriodId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("dissagregationOptionId")
    private AgeDissagregationOption dissagregationOption;


    @Override
    public AgeDissagregationOption getDissagregationOption() {
        return this.dissagregationOption;
    }

    @Override
    public void setDissagregationOption(AgeDissagregationOption dissagregationOption) {
        this.dissagregationOption = dissagregationOption;
    }

    @Override
    public AgeDissagregationOptionPeriodId getId() {
        return id;
    }

    @Override
    public void setId(AgeDissagregationOptionPeriodId id) {
        this.id = id;
    }
}
