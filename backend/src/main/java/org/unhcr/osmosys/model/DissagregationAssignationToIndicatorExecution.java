package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.enums.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "osmosys", name = "dissagregation_assignation_indicator_execution")
public class DissagregationAssignationToIndicatorExecution extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "indicator_execution_id", foreignKey = @ForeignKey(name = "fk_dissagregation_aignation_inicator"))
    private IndicatorExecution indicatorExecution;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12, unique = false)
    private State state;

    @Enumerated(EnumType.STRING)
    @Column(name = "dissagregation_type", nullable = false, length = 12, unique = false)
    private DissagregationType dissagregationType;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "dissagregationAssignationToIndicatorExecution")
    private Set<DissagregationFilterIndicatorExecution> dissagregationFilterIndicatorsExecutions = new HashSet<>();

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IndicatorExecution getIndicatorExecution() {
        return indicatorExecution;
    }

    public void setIndicatorExecution(IndicatorExecution indicatorExecution) {
        this.indicatorExecution = indicatorExecution;
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

    public void addDissagregationFilterIndicatorExecution(DissagregationFilterIndicatorExecution dissagregationFilterIndicatorExecution){
        dissagregationFilterIndicatorExecution.setDissagregationAssignationToIndicatorExecution(this);
        dissagregationFilterIndicatorExecution.setState(State.ACTIVE);

        if(!this.dissagregationFilterIndicatorsExecutions.add(dissagregationFilterIndicatorExecution)){
            this.dissagregationFilterIndicatorsExecutions.remove(dissagregationFilterIndicatorExecution);
            this.dissagregationFilterIndicatorsExecutions.add(dissagregationFilterIndicatorExecution);
        }
    }


    public void removeDissagregationFilterIndicatorExecution(DissagregationFilterIndicatorExecution dissagregationFilterIndicatorExecution){
        if (dissagregationFilterIndicatorExecution.getId() != null) {

            dissagregationFilterIndicatorExecution.setState(State.INACTIVE);
        } else {
            this.dissagregationFilterIndicatorsExecutions.remove(dissagregationFilterIndicatorExecution);
        }
    }

    public Set<DissagregationFilterIndicatorExecution> getDissagregationFilterIndicatorsExecutions() {
        return dissagregationFilterIndicatorsExecutions;
    }

    public void setDissagregationFilterIndicatorsExecutions(Set<DissagregationFilterIndicatorExecution> dissagregationFilterIndicatorsExecutions) {
        this.dissagregationFilterIndicatorsExecutions = dissagregationFilterIndicatorsExecutions;
    }
}
