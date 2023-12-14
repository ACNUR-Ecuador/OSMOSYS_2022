package org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options;

import com.sagatechs.generics.persistence.model.State;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "GenderDissagregationOption")
@DiscriminatorValue("gender_option")
public class GenderDissagregationOption extends StandardDissagregationOption {

    public GenderDissagregationOption() {
        super();
    }

    public GenderDissagregationOption(String name, String groupName, Integer order, State state) {
        super(name, groupName, order, state);
    }
}
