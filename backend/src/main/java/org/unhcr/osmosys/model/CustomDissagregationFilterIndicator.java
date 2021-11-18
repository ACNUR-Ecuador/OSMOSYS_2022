package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.enums.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(schema = "osmosys", name = "custom_dissagregation_filter_indicator")
public class CustomDissagregationFilterIndicator extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12)
    private State state;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "custom_dissagregation_assignation_indicator_id", foreignKey = @ForeignKey(name = "fk_cus_diss_ass_indicator"))
    private CustomDissagregationAssignationToIndicator customDissagregationAssignationToIndicator;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(schema ="osmosys" ,name = "custom_dissagregation_filter_indicator_options", joinColumns = @JoinColumn(name = "custom_dissagregation_filter_indicator_id"), inverseJoinColumns = @JoinColumn(name = "custom_dissagregation_option_id"))
    private Set<CustomDissagregationOption> customDissagregationOptions;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public CustomDissagregationAssignationToIndicator getCustomDissagregationAssignationToIndicator() {
        return customDissagregationAssignationToIndicator;
    }

    public void setCustomDissagregationAssignationToIndicator(CustomDissagregationAssignationToIndicator customDissagregationAssignationToIndicator) {
        this.customDissagregationAssignationToIndicator = customDissagregationAssignationToIndicator;
    }

    public Set<CustomDissagregationOption> getCustomDissagregationOptions() {
        return customDissagregationOptions;
    }

    public void setCustomDissagregationOptions(Set<CustomDissagregationOption> customDissagregationOptions) {
        this.customDissagregationOptions = customDissagregationOptions;
    }


}
