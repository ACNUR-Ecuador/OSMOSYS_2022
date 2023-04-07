package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(schema = "osmosys", name = "custom_dissagregation_options")
public class CustomDissagregationOption extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12)
    private State state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "custom_dissagregation_id", foreignKey = @ForeignKey(name = "fk_diss_optiop_dissagretion"))
    private CustomDissagregation customDissagregation;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(schema ="osmosys" ,name = "custom_dissagregation_option_markers", joinColumns = @JoinColumn(name = "custom_dissagregation_option_id"), inverseJoinColumns = @JoinColumn(name = "marker_id"))
    private Set<Marker> markers = new HashSet<>();

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

    public Set<Marker> getMarkers() {
        return markers;
    }

    public void setMarkers(Set<Marker> markers) {
        this.markers = markers;
    }

    public void addMarker(Marker marker){
        marker.getCustomDissagregationOptions().add(this);

        if(!this.markers.add(marker)){
            this.markers.remove(marker);
            this.markers.add(marker);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomDissagregationOption)) return false;
        CustomDissagregationOption that = (CustomDissagregationOption) o;
        return Objects.equals(id, that.id) && name.equals(that.name) && description.equals(that.description) && customDissagregation.equals(that.customDissagregation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, customDissagregation);
    }


}
