package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;

@Entity
@Table(schema = "osmosys", name = "project_location_assigments",
        uniqueConstraints = @UniqueConstraint(name = "unique_project_location_assigments", columnNames = {"project_id", "canton_id"}))
public class ProjectLocationAssigment extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false, foreignKey = @ForeignKey(name = "fk_project_location"))
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "canton_id", nullable = false, foreignKey = @ForeignKey(name = "fk_project_canton"))
    private Canton location;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Canton getLocation() {
        return location;
    }

    public void setLocation(Canton location) {
        this.location = location;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof ProjectLocationAssigment)) return false;

        ProjectLocationAssigment that = (ProjectLocationAssigment) o;

        return new EqualsBuilder().append(id, that.id).append(project, that.project).append(location, that.location).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(project).append(location).toHashCode();
    }
}