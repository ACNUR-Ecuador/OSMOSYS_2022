package org.unhcr.osmosys.model.standardDissagregations;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.BaseEntityIdState;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.DissagregationAssignationToIndicator;
import org.unhcr.osmosys.model.standardDissagregations.options.AgeDissagregationOption;

import javax.persistence.*;
import java.util.StringJoiner;

@Entity
@Table(schema = "dissagregations", name = "dissagregation_assignation_indicator_period_customizations",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_dissagregation_assignation_indicator_option",
                        columnNames = {"standard_dissagregation_options_id","dissagregation_assignation_indicator_id"})
        }
)
public class DissagregationAssignationToIndicatorPeriodCustomization extends BaseEntityIdState {

    public DissagregationAssignationToIndicatorPeriodCustomization() {
    }

    public DissagregationAssignationToIndicatorPeriodCustomization(DissagregationAssignationToIndicator dissagregationAssignationToIndicator, AgeDissagregationOption ageDissagregationOption) {
        this.dissagregationAssignationToIndicator = dissagregationAssignationToIndicator;
        this.ageDissagregationOption = ageDissagregationOption;
        this.state=State.ACTIVO;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false )
    @JoinColumn(name = "dissagregation_assignation_indicator_id", foreignKey = @ForeignKey(name = "fk_dissagregation_assignation_indicator"))
    private DissagregationAssignationToIndicator dissagregationAssignationToIndicator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "standard_dissagregation_options_id", foreignKey = @ForeignKey(name = "standard_dissagregation_options_indicator"))
    private AgeDissagregationOption ageDissagregationOption;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12)
    private State state;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DissagregationAssignationToIndicator getDissagregationAssignationToIndicator() {
        return dissagregationAssignationToIndicator;
    }

    public void setDissagregationAssignationToIndicator(DissagregationAssignationToIndicator dissagregationAssignationToIndicator) {
        this.dissagregationAssignationToIndicator = dissagregationAssignationToIndicator;
    }

    public AgeDissagregationOption getAgeDissagregationOption() {
        return ageDissagregationOption;
    }

    public void setAgeDissagregationOption(AgeDissagregationOption ageDissagregationOption) {
        this.ageDissagregationOption = ageDissagregationOption;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DissagregationAssignationToIndicatorPeriodCustomization.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("dissagregationAssignationToIndicator=" + dissagregationAssignationToIndicator)
                .add("ageDissagregationOption=" + ageDissagregationOption)
                .add("state=" + state)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof DissagregationAssignationToIndicatorPeriodCustomization)) return false;

        DissagregationAssignationToIndicatorPeriodCustomization that = (DissagregationAssignationToIndicatorPeriodCustomization) o;

        return new EqualsBuilder().append(id, that.id).append(dissagregationAssignationToIndicator, that.dissagregationAssignationToIndicator).append(ageDissagregationOption, that.ageDissagregationOption).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(dissagregationAssignationToIndicator).append(ageDissagregationOption).toHashCode();
    }
}
