package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntityIdState;
import com.sagatechs.generics.persistence.model.State;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(schema = "osmosys", name = "tags")
public class Tags extends BaseEntityIdState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12)
    private State state;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "text", unique = true)
    private String description;

    @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<PeriodTagAssignation> periodTagAssignations = new HashSet<>();

    public void setId(Long id) {
        this.id = id;
    }

    public void setState(State state) {
        this.state = state;
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

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public State getState() {
        return state;
    }

    public Set<PeriodTagAssignation> getPeriodTagAssignations() {
        return periodTagAssignations;
    }

    public void setPeriodTagAssignations(Set<PeriodTagAssignation> periodTagAssignations) {
        this.periodTagAssignations = periodTagAssignations;
    }

    public void addPeriodTagAssignation(PeriodTagAssignation periodTagAssignation) {
        periodTagAssignation.setTag(this);
        if (!this.periodTagAssignations.add(periodTagAssignation)) {
            this.periodTagAssignations.remove(periodTagAssignation);
            this.periodTagAssignations.add(periodTagAssignation);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tags tags = (Tags) o;
        return Objects.equals(id, tags.id) && state == tags.state && Objects.equals(name, tags.name) && Objects.equals(description, tags.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, state, name, description);
    }

    @Override
    public String toString() {
        return "Tags{" +
                "id=" + id +
                ", state=" + state +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
