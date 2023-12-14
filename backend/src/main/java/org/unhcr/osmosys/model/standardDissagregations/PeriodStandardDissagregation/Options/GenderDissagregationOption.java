package org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options;

import com.sagatechs.generics.persistence.model.State;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "dissagregations", name = "gender_dissagregation_options")
public class GenderDissagregationOption extends StandardDissagregationOption {

    public GenderDissagregationOption() {
        super();
    }

    public GenderDissagregationOption(String name, String groupName, Integer order, State state) {
        super(name, groupName, order, state);
    }
}
