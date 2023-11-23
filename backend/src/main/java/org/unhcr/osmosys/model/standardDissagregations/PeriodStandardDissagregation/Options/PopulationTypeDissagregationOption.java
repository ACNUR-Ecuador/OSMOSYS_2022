package org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;

@Entity
@Table(schema = "dissagregations", name = "population_type_dissagregation_options")
public class PopulationTypeDissagregationOption extends StandardDissagregationOption {


    public PopulationTypeDissagregationOption() {
        super();
    }

    public PopulationTypeDissagregationOption(String name, String groupName, Integer order, State state) {
        super(name,groupName,order,state);
    }
}
