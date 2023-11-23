package org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;

import javax.persistence.*;

@Entity
@Table(schema = "dissagregations", name = "country_of_origin_dissagregation_options")
public class CountryOfOriginDissagregationOption extends StandardDissagregationOption{

    public CountryOfOriginDissagregationOption() {
        super();
    }

    public CountryOfOriginDissagregationOption(String name, String groupName, Integer order, State state) {
        super(name,groupName,order,state);
    }

}
