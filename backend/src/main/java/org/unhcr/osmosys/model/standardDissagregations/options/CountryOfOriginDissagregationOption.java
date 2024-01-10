package org.unhcr.osmosys.model.standardDissagregations.options;

import com.sagatechs.generics.persistence.model.State;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.StringJoiner;

@Entity(name = "CountryOfOriginOption")
@DiscriminatorValue("country_of_origin")
public class CountryOfOriginDissagregationOption extends StandardDissagregationOption{

    public CountryOfOriginDissagregationOption() {
        super();
    }

    public CountryOfOriginDissagregationOption(String name, String groupName, Integer order, State state) {
        super(name,groupName,order,state);
    }



    @Override
    public String toString() {
        return new StringJoiner(", ", CountryOfOriginDissagregationOption.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("groupName='" + groupName + "'")
                .add("order=" + order)
                .add("state=" + state)
                .toString();
    }
}
