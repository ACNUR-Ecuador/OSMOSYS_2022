package org.unhcr.osmosys.webServices.model;

import com.sagatechs.generics.persistence.model.AuditAction;
import com.sagatechs.generics.webservice.webModel.UserWeb;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class AuditWeb extends BaseWebEntity implements Serializable {
    public AuditWeb() {}

    private String entity;
    private String projectCode;
    private String indicatorCode;
    private AuditAction action;
    private UserWeb responsibleUser;
    private LocalDateTime changeDate;
    private String oldData;
    private String newData;

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public AuditAction getAction() {
        return action;
    }

    public void setAction(AuditAction action) {
        this.action = action;
    }

    public UserWeb getResponsibleUser() {
        return responsibleUser;
    }

    public void setResponsibleUser(UserWeb responsibleUser) {
        this.responsibleUser = responsibleUser;
    }

    public LocalDateTime getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(LocalDateTime changeDate) {
        this.changeDate = changeDate;
    }

    public String getOldData() {
        return oldData;
    }

    public void setOldData(String oldData) {
        this.oldData = oldData;
    }

    public String getNewData() {
        return newData;
    }

    public void setNewData(String newData) {
        this.newData = newData;
    }

    public String getIndicatorCode() {
        return indicatorCode;
    }

    public void setIndicatorCode(String indicatorCode) {
        this.indicatorCode = indicatorCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AuditWeb auditWeb = (AuditWeb) o;
        return Objects.equals(entity, auditWeb.entity) && Objects.equals(projectCode, auditWeb.projectCode) && Objects.equals(indicatorCode, auditWeb.indicatorCode) && action == auditWeb.action && Objects.equals(responsibleUser, auditWeb.responsibleUser) && Objects.equals(changeDate, auditWeb.changeDate) && Objects.equals(oldData, auditWeb.oldData) && Objects.equals(newData, auditWeb.newData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), entity, projectCode, indicatorCode, action, responsibleUser, changeDate, oldData, newData);
    }

    @Override
    public String toString() {
        return "AuditWeb{" +
                "id=" + id +
                ", state=" + state +
                ", entity='" + entity + '\'' +
                ", projectCode='" + projectCode + '\'' +
                ", indicatorCode='" + indicatorCode + '\'' +
                ", action=" + action +
                ", responsibleUser=" + responsibleUser +
                ", changeDate=" + changeDate +
                ", oldData='" + oldData + '\'' +
                ", newData='" + newData + '\'' +
                '}';
    }
}
