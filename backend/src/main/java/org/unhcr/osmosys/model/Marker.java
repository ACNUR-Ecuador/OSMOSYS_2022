package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.enums.AreaType;
import org.unhcr.osmosys.model.enums.MarkerType;

import javax.persistence.*;


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
}
