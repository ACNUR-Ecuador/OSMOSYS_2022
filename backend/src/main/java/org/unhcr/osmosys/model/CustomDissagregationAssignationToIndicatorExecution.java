package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;

import javax.persistence.*;

@Entity
@Table(schema = "osmosys", name = "custom_dissagregation_assignations_indicator_execution")
public class CustomDissagregationAssignationToIndicatorExecution extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "indicator_execution_id", foreignKey = @ForeignKey(name = "fk_dissagregation_aignation_inicator"))
    private IndicatorExecution indicatorExecution;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12)
    private State state;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "custom_dissagregation_id", foreignKey = @ForeignKey(name = "fk_custom_dissagregation_asignation_indicator_execution"))
    private CustomDissagregation customDissagregation;


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

    public CustomDissagregation getCustomDissagregation() {
        return customDissagregation;
    }

    public void setCustomDissagregation(CustomDissagregation customDissagregation) {
        this.customDissagregation = customDissagregation;
    }



}
