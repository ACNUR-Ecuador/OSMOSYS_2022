package org.unhcr.osmosys.model.standardDissagregations.options;

import com.sagatechs.generics.persistence.model.State;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.StringJoiner;

@Entity(name = "GenderOption")
@DiscriminatorValue("gender")
public class GenderDissagregationOption extends StandardDissagregationOption {

    public GenderDissagregationOption() {
        super();
    }

    public GenderDissagregationOption(String name, String groupName, Integer order, State state) {
        super(name, groupName, order, state);
    }



    @Override
    public String toString() {
        return new StringJoiner(", ", GenderDissagregationOption.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("groupName='" + groupName + "'")
                .add("order=" + order)
                .add("state=" + state)
                .toString();
    }
}
