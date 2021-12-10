package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.enums.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "osmosys", name = "general_indicators")
public class GeneralIndicator extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "description", nullable = false, unique = true)
    private String description;

    @Column(name = "measure_type", nullable = true)
    @Enumerated(EnumType.STRING)
    private MeasureType measureType;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12)
    private State state;


    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "periodo_id", foreignKey = @ForeignKey(name = "fk_general_indicator_period"))
    private Period period;

    @OneToMany(mappedBy = "generalIndicator", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<DissagregationAssignationToGeneralIndicator> dissagregationAssignationsToGeneralIndicator = new HashSet<>();

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MeasureType getMeasureType() {
        return measureType;
    }

    public void setMeasureType(MeasureType measureType) {
        this.measureType = measureType;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Set<DissagregationAssignationToGeneralIndicator> getDissagregationAssignationsToGeneralIndicator() {
        return dissagregationAssignationsToGeneralIndicator;
    }

    public void setDissagregationAssignationsToGeneralIndicator(Set<DissagregationAssignationToGeneralIndicator> dissagregationAssignationsToGeneralIndicator) {
        this.dissagregationAssignationsToGeneralIndicator = dissagregationAssignationsToGeneralIndicator;
    }

    public void addDissagregationAssignationsToGeneralIndicator(DissagregationAssignationToGeneralIndicator dissagregationAssignationToGeneralIndicator) {
        dissagregationAssignationToGeneralIndicator.setGeneralIndicator(this);
        if(!this.dissagregationAssignationsToGeneralIndicator.add(dissagregationAssignationToGeneralIndicator)){
            this.dissagregationAssignationsToGeneralIndicator.remove(dissagregationAssignationToGeneralIndicator);
            this.dissagregationAssignationsToGeneralIndicator.add(dissagregationAssignationToGeneralIndicator);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        GeneralIndicator that = (GeneralIndicator) o;

        return new EqualsBuilder().append(id, that.id).append(description, that.description).append(period, that.period).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(description).append(period).toHashCode();
    }
}
