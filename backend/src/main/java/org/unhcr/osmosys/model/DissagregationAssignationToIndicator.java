package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntityIdState;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.enums.DissagregationType;
import org.unhcr.osmosys.model.standardDissagregations.DissagregationAssignationToIndicatorPeriodCustomization;
import org.unhcr.osmosys.model.standardDissagregations.options.AgeDissagregationOption;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;

@Entity
@Table(schema = "osmosys", name = "dissagregation_assignation_indicator",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_dissagregation_assignation_indicator", columnNames = {"indicator_id", "dissagregation_type", "period_id"})
        }
)
public class DissagregationAssignationToIndicator extends BaseEntityIdState  implements  DissagregationAssignationToIndicatorInterface{

    public DissagregationAssignationToIndicator() {
        this.useCustomAgeDissagregations=Boolean.FALSE;
    }

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

    @Column(name = "use_custom_age_dissagregations")
    private Boolean useCustomAgeDissagregations;

    @OneToMany(
            mappedBy = "dissagregationAssignationToIndicator",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private Set<DissagregationAssignationToIndicatorPeriodCustomization> dissagregationAssignationToIndicatorPeriodCustomizations = new HashSet<>();

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


    public Indicator getIndicator() {
        return indicator;
    }

    public void setIndicator(Indicator indicator) {
        this.indicator = indicator;
    }


    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }


    public Boolean getUseCustomAgeDissagregations() {
        return useCustomAgeDissagregations;
    }

    public void setUseCustomAgeDissagregations(Boolean useCustomAgeDissagregations) {
        this.useCustomAgeDissagregations = useCustomAgeDissagregations;
    }

    public Set<DissagregationAssignationToIndicatorPeriodCustomization> getDissagregationAssignationToIndicatorPeriodCustomizations() {
        return dissagregationAssignationToIndicatorPeriodCustomizations;
    }

    public void setDissagregationAssignationToIndicatorPeriodCustomizations(Set<DissagregationAssignationToIndicatorPeriodCustomization> dissagregationAssignationToIndicatorPeriodCustomizations) {
        this.dissagregationAssignationToIndicatorPeriodCustomizations = dissagregationAssignationToIndicatorPeriodCustomizations;
    }

    public void addAgeDissagregationCustomizations(AgeDissagregationOption ageDissagregationOption) {
        Optional<DissagregationAssignationToIndicatorPeriodCustomization> optionalOption = this.dissagregationAssignationToIndicatorPeriodCustomizations.stream()
                .filter(dissagregationAssignationToIndicatorPeriodCustomization -> dissagregationAssignationToIndicatorPeriodCustomization.getAgeDissagregationOption().getId().equals(ageDissagregationOption.getId()))
                .findFirst();

        if(optionalOption.isPresent()){
            optionalOption.get().setState(State.ACTIVO);
        }else {
            DissagregationAssignationToIndicatorPeriodCustomization customization = new DissagregationAssignationToIndicatorPeriodCustomization(this,ageDissagregationOption);
            this.dissagregationAssignationToIndicatorPeriodCustomizations.add(customization);
        }
    }

    public void removeAgeDissagregationCustomizations(AgeDissagregationOption ageDissagregationOption) {
        Optional<DissagregationAssignationToIndicatorPeriodCustomization> optionalOption = this.dissagregationAssignationToIndicatorPeriodCustomizations.stream()
                .filter(dissagregationAssignationToIndicatorPeriodCustomization -> dissagregationAssignationToIndicatorPeriodCustomization.getAgeDissagregationOption().getId().equals(ageDissagregationOption.getId()))
                .findFirst();

        optionalOption.ifPresent(dissagregationAssignationToIndicatorPeriodCustomization -> dissagregationAssignationToIndicatorPeriodCustomization.setState(State.INACTIVO));
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", DissagregationAssignationToIndicator.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("period=" + period)
                .add("indicator=" + indicator)
                .add("state=" + state)
                .add("dissagregationType=" + dissagregationType)
                .add("useCustomAgeDissagregations=" + useCustomAgeDissagregations)
                .toString();
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
