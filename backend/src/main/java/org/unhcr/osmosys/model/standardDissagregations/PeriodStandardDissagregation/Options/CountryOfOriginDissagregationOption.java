package org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;

import javax.persistence.*;

@Entity(name = "CountryOfOriginDissagregationOption")
@DiscriminatorValue("country_of_origin_option")
public class CountryOfOriginDissagregationOption extends StandardDissagregationOption{

    public CountryOfOriginDissagregationOption() {
        super();
    }

    public CountryOfOriginDissagregationOption(String name, String groupName, Integer order, State state) {
        super(name,groupName,order,state);
    }

}
