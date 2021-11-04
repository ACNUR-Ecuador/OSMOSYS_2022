package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "osmosys", name = "custom_dissagregations")
public class CustomDissagregation extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "control_total_value", nullable = false)
    private Boolean controlTotalValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12, unique = false)
    private State state;

    @OneToMany(mappedBy = "customDissagregation",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<CustomDissagregationOption> customDissagregationOptions = new HashSet<>();

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getControlTotalValue() {
        return controlTotalValue;
    }

    public void setControlTotalValue(Boolean controlTotalValue) {
        this.controlTotalValue = controlTotalValue;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Set<CustomDissagregationOption> getCustomDissagregationOptions() {
        return customDissagregationOptions;
    }

    public void setCustomDissagregationOptions(Set<CustomDissagregationOption> customDissagregationOptions) {
        this.customDissagregationOptions = customDissagregationOptions;
    }
}
