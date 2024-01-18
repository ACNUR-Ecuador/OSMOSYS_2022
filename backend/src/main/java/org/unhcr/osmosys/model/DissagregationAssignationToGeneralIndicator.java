package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.BaseEntityIdState;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.enums.DissagregationType;
import org.unhcr.osmosys.model.standardDissagregations.DissagregationAsignationInterface;
import org.unhcr.osmosys.model.standardDissagregations.DissagregationAssignationToIndicatorPeriodCustomization;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(schema = "osmosys", name = "dissagregation_assignation_general_indicator",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_dissagregation_assignation_general_indicator", columnNames = {"general_indicator_id","dissagregation_type"})
        }
)
public class DissagregationAssignationToGeneralIndicator extends BaseEntityIdState implements DissagregationAsignationInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "general_indicator_id", foreignKey = @ForeignKey(name = "fk_dissagregation_asignation_indicator"))
    private GeneralIndicator generalIndicator;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12)
    private State state;

    @Enumerated(EnumType.STRING)
    @Column(name = "dissagregation_type", nullable = false, length = 60)
    private DissagregationType dissagregationType;


    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GeneralIndicator getGeneralIndicator() {
        return generalIndicator;
    }

    public void setGeneralIndicator(GeneralIndicator generalIndicator) {
        this.generalIndicator = generalIndicator;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public DissagregationType getDissagregationType() {
        return dissagregationType;
    }

    @Override
    public Boolean getUseCustomAgeDissagregations() {
        return null;
    }

    @Override
    public Set<DissagregationAssignationToIndicatorPeriodCustomization> getDissagregationAssignationToIndicatorPeriodCustomizations() {
        return null;
    }

    public void setDissagregationType(DissagregationType dissagregationType) {
        this.dissagregationType = dissagregationType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DissagregationAssignationToGeneralIndicator that = (DissagregationAssignationToGeneralIndicator) o;

        return new EqualsBuilder().append(id, that.id).append(generalIndicator, that.generalIndicator).append(dissagregationType, that.dissagregationType).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(generalIndicator).append(dissagregationType).toHashCode();
    }
}
