package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.enums.DissagregationType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "osmosys", name = "custom_dissagregation_assignation_indicator",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_custom_dissagregation_assignation_indicator", columnNames = {"indicator_id", "custom_dissagregation_id"})
        }
)
public class CustomDissagregationAssignationToIndicator extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "indicator_id", foreignKey = @ForeignKey(name = "fk_dissagregation_asignation_indicator"))
    private Indicator indicator;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12, unique = false)
    private State state;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "custom_dissagregation_id", foreignKey = @ForeignKey(name = "fk_custom_dissagregation_asignation_indicator"))
    private CustomDissagregation customDissagregation;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customDissagregationAssignationToIndicator")
    private Set<CustomDissagregationFilterIndicator> customDissagregationFilterIndicators = new HashSet<>();

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
}
