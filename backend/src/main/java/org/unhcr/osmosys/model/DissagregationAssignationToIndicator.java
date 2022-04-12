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
@Table(schema = "osmosys", name = "dissagregation_assignation_indicator",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_dissagregation_assignation_indicator", columnNames = {"indicator_id","dissagregation_type", "period_id"})
        }
)
public class DissagregationAssignationToIndicator extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "period_id", foreignKey = @ForeignKey(name = "fk_dissagregation_asignation_period"))
    private Period period;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "indicator_id", foreignKey = @ForeignKey(name = "fk_dissagregation_asignation_indicator"))
    private Indicator indicator;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12)
    private State state;

    @Enumerated(EnumType.STRING)
    @Column(name = "dissagregation_type", nullable = false, length = 60)
    private DissagregationType dissagregationType;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dissagregationAssignationToIndicator")
    private Set<DissagregationFilterIndicator> dissagregationFilterIndicators = new HashSet<>();

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

    public DissagregationType getDissagregationType() {
        return dissagregationType;
    }

    public void setDissagregationType(DissagregationType dissagregationType) {
        this.dissagregationType = dissagregationType;
    }

    public void addDissagregationFilterIndicator(DissagregationFilterIndicator dissagregationFilterIndicator){
        dissagregationFilterIndicator.setDissagregationAssignationToIndicator(this);
        if(!this.dissagregationFilterIndicators.add(dissagregationFilterIndicator)){
            this.dissagregationFilterIndicators.remove(dissagregationFilterIndicator);
            this.dissagregationFilterIndicators.add(dissagregationFilterIndicator);
        }
    }

    public void removeDissagregationFilterIndicator(DissagregationFilterIndicator dissagregationFilterIndicator){
        if (dissagregationFilterIndicator.getId() != null) {
            dissagregationFilterIndicator.setState(State.INACTIVO);
        } else {
            this.dissagregationFilterIndicators.remove(dissagregationFilterIndicator);
        }
    }

    public Set<DissagregationFilterIndicator> getDissagregationFilters() {
        return dissagregationFilterIndicators;
    }

    public void setDissagregationFilters(Set<DissagregationFilterIndicator> dissagregationFilterIndicators) {
        this.dissagregationFilterIndicators = dissagregationFilterIndicators;
    }

    public Indicator getIndicator() {
        return indicator;
    }

    public void setIndicator(Indicator indicator) {
        this.indicator = indicator;
    }

    public Set<DissagregationFilterIndicator> getDissagregationFilterIndicators() {
        return dissagregationFilterIndicators;
    }

    public void setDissagregationFilterIndicators(Set<DissagregationFilterIndicator> dissagregationFilterIndicators) {
        this.dissagregationFilterIndicators = dissagregationFilterIndicators;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    @Override
    public String toString() {
        return "DissagregationAssignationToIndicator{" +
                "id=" + id +
                ", indicator=" + indicator +
                ", state=" + state +
                ", dissagregationType=" + dissagregationType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof DissagregationAssignationToIndicator)) return false;

        DissagregationAssignationToIndicator that = (DissagregationAssignationToIndicator) o;

        return new EqualsBuilder().append(id, that.id).append(period, that.period).append(indicator, that.indicator).append(dissagregationType, that.dissagregationType).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(period).append(indicator).append(dissagregationType).toHashCode();
    }
}
