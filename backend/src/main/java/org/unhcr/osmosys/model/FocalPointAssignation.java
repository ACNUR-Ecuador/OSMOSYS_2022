package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntityIdState;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.model.User;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(schema = "osmosys", name = "focal_point_assignation")
public class FocalPointAssignation extends BaseEntityIdState {

    public FocalPointAssignation() {
    }

    public FocalPointAssignation(User focalPointer, Project project, Boolean mainFocalPointer) {
        this.focalPointer = focalPointer;
        this.project = project;
        this.mainFocalPointer = mainFocalPointer;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "focal_pointer_id", foreignKey = @ForeignKey(name = "fk_focalPonter_project"))
    private User focalPointer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", foreignKey = @ForeignKey(name = "fk_por_indicator"))
    private Project project;

    @Column(name = "main_focal_pointer")
    private Boolean mainFocalPointer;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public State getState() {
        return state;
    }

    public User getFocalPointer() {
        return focalPointer;
    }

    public void setFocalPointer(User focalPointer) {
        this.focalPointer = focalPointer;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Boolean getMainFocalPointer() {
        return mainFocalPointer;
    }

    public void setMainFocalPointer(Boolean mainFocalPointer) {
        this.mainFocalPointer = mainFocalPointer;
    }

    public void setState(State state) {
        this.state = state;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FocalPointAssignation that = (FocalPointAssignation) o;
        return Objects.equals(id, that.id) && Objects.equals(focalPointer, that.focalPointer) && Objects.equals(project, that.project) && Objects.equals(mainFocalPointer, that.mainFocalPointer) && state == that.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, focalPointer, project, mainFocalPointer, state);
    }
}