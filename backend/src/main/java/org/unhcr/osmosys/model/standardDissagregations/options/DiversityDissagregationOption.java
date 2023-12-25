package org.unhcr.osmosys.model.standardDissagregations.options;

import com.sagatechs.generics.persistence.model.State;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.StringJoiner;

@Entity(name = "DiversityOption")
@DiscriminatorValue("diversity")
public class DiversityDissagregationOption extends StandardDissagregationOption {

    public DiversityDissagregationOption() {
        super();
    }

    public DiversityDissagregationOption(String name, String groupName, Integer order, State state) {
        super(name,groupName,order,state);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DiversityDissagregationOption.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("groupName='" + groupName + "'")
                .add("order=" + order)
                .add("state=" + state)
                .toString();
    }
}
