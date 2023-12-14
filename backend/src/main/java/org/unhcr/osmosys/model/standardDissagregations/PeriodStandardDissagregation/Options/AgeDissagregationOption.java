package org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options;

import com.sagatechs.generics.persistence.model.State;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "AgeDissagregationOption")
@DiscriminatorValue("age_option")
public class AgeDissagregationOption extends StandardDissagregationOption {

    public AgeDissagregationOption() {
        super();
    }

    public AgeDissagregationOption(String name, String ageRange, String groupName, Integer order, State state) {
        super(name,groupName,order,state);
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
}
