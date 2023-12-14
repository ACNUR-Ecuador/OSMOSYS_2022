package org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options;

import com.sagatechs.generics.persistence.model.State;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "dissagregations", name = "diversity_dissagregation_options")
public class DiversityDissagregationOption extends StandardDissagregationOption {

    public DiversityDissagregationOption() {
        super();
    }

    public DiversityDissagregationOption(String name, String groupName, Integer order, State state) {
        super(name,groupName,order,state);
    }
}
