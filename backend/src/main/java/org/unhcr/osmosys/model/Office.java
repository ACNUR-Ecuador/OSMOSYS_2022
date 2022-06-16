package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.enums.OfficeType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "osmosys", name = "offices")
public class Office extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "description", unique = true, nullable = false)
    private String description;

    @Column(name = "acronym", unique = true, nullable = false)
    private String acronym;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private OfficeType type;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_office",foreignKey = @ForeignKey(name = "fk_office_parentoffice"))
    private Office parentOffice;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "parentOffice")
    private Set<Office> childOffices =new HashSet<>();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public OfficeType getType() {
        return type;
    }

    public void setType(OfficeType type) {
        this.type = type;
    }

    public Office getParentOffice() {
        return parentOffice;
    }

    public void setParentOffice(Office parentOffice) {
        this.parentOffice = parentOffice;
    }

    public Set<Office> getChildOffices() {
        return childOffices;
    }

    public void setChildOffices(Set<Office> childOffices) {
        this.childOffices = childOffices;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Office office = (Office) o;

        return new EqualsBuilder().append(id, office.id).append(description, office.description).append(acronym, office.acronym).append(type, office.type).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(description).append(acronym).append(type).toHashCode();
    }

    @Override
    public String toString() {
        return "Office{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", acronym='" + acronym + '\'' +
                ", type=" + type +
                ", state=" + state +
                '}';
    }
}