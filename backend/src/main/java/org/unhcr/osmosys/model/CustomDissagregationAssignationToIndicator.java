package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.BaseEntityIdState;
import com.sagatechs.generics.persistence.model.State;

import javax.persistence.*;

@Entity
@Table(schema = "osmosys", name = "custom_dissagregation_assignation_indicator",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_custom_dissagregation_assignation_indicator", columnNames = {"indicator_id", "custom_dissagregation_id", "period_id"})
        }
)
public class CustomDissagregationAssignationToIndicator extends BaseEntityIdState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "indicator_id", foreignKey = @ForeignKey(name = "fk_dissagregation_asignation_indicator"))
    private Indicator indicator;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "period_id", foreignKey = @ForeignKey(name = "fk_dissagregation_asignation_period"))
    private Period period;


    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12)
    private State state;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "custom_dissagregation_id", foreignKey = @ForeignKey(name = "fk_custom_dissagregation_asignation_indicator"))
    private CustomDissagregation customDissagregation;


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

    public Indicator getIndicator() {
        return indicator;
    }

    public void setIndicator(Indicator indicator) {
        this.indicator = indicator;
    }

    public CustomDissagregation getCustomDissagregation() {
        return customDissagregation;
    }

    public void setCustomDissagregation(CustomDissagregation customDissagregation) {
        this.customDissagregation = customDissagregation;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }
}
