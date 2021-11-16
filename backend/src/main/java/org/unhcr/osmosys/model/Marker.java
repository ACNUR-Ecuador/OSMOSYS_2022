package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.enums.AreaType;
import org.unhcr.osmosys.model.enums.MarkerType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(schema = "osmosys", name = "markers",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_marker_unique", columnNames = {"type","subType","short_description"})
        })
public class Marker extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12, unique = false)
    private State state;

    @Column(name = "type", nullable = false, length = 50, unique = false)
    @Enumerated(EnumType.STRING)
    private MarkerType type;

    @Column(name = "subtype", nullable = true, length = 255, unique = false)
    private String subType;

    @Column(name = "short_description", unique = false)
    private String shortDescription;


    @Column(name = "description", columnDefinition = "text", unique = false)
    private String description;

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "markers")
    private Set<CustomDissagregationOption> customDissagregationOptions = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "markers")
    private Set<Indicator> indicators = new HashSet<>();

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

    public MarkerType getType() {
        return type;
    }

    public void setType(MarkerType type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<CustomDissagregationOption> getCustomDissagregationOptions() {
        return customDissagregationOptions;
    }

    public void setCustomDissagregationOptions(Set<CustomDissagregationOption> customDissagregationOptions) {
        this.customDissagregationOptions = customDissagregationOptions;
    }

    public Set<Indicator> getIndicators() {
        return indicators;
    }

    public void setIndicators(Set<Indicator> indicators) {
        this.indicators = indicators;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Marker marker = (Marker) o;

        return new EqualsBuilder().append(id, marker.id).append(type, marker.type).append(subType, marker.subType).append(shortDescription, marker.shortDescription).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(type).append(subType).append(shortDescription).toHashCode();
    }
}
