package org.unhcr.osmosys.model.standardDissagregations.options;

import com.sagatechs.generics.persistence.model.State;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.StringJoiner;

@Entity(name = "AgeOption")
@DiscriminatorValue("age")
public class AgeDissagregationOption extends StandardDissagregationOption {

    public AgeDissagregationOption() {
        super();
    }

    public AgeDissagregationOption(String name, String ageRange, String groupName, Integer order, State state) {
        super(name, groupName, order, state);
        this.ageRange = ageRange;
    }



    @Column(name = "age_range")
    private String ageRange;

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", AgeDissagregationOption.class.getSimpleName() + "[", "]")
                .add("ageRange='" + ageRange + "'")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("groupName='" + groupName + "'")
                .add("order=" + order)
                .toString();
    }
}
