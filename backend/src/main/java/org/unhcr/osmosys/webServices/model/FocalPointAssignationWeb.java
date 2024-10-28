package org.unhcr.osmosys.webServices.model;

import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.model.User;
import org.unhcr.osmosys.model.Project;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


public class FocalPointAssignationWeb extends BaseWebEntity implements Serializable {
    public FocalPointAssignationWeb() {
        super();
    }

    private User focalPointer;
    private Project project;
    private Boolean mainFocalPointer;

    public FocalPointAssignationWeb(User focalPointer, Project project, Boolean mainFocalPointer) {
        this.focalPointer = focalPointer;
        this.project = project;
        this.mainFocalPointer = mainFocalPointer;
    }

    public FocalPointAssignationWeb(Long id, State state, User focalPointer, Project project, Boolean mainFocalPointer) {
        super(id, state);
        this.focalPointer = focalPointer;
        this.project = project;
        this.mainFocalPointer = mainFocalPointer;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FocalPointAssignationWeb that = (FocalPointAssignationWeb) o;
        return Objects.equals(focalPointer, that.focalPointer) && Objects.equals(project, that.project) && Objects.equals(mainFocalPointer, that.mainFocalPointer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), focalPointer, project, mainFocalPointer);
    }
}


