package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.AuditAction;
import com.sagatechs.generics.persistence.model.BaseEntityIdState;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(schema = "osmosys", name = "audits")
public class Audit extends BaseEntityIdState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "entity",  nullable = false)
    private String entity;

    @Column(name = "record_id", nullable = false)
    private Long recordId;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false, length = 22)
    private AuditAction action;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsible_user_id", foreignKey = @ForeignKey(name = "fk_audit_user_responsible"))
    private User responsibleUser;

    @Column(name = "change_date")
    private LocalDateTime changeDate;

    @Column(name = "old_data", columnDefinition = "TEXT")
    private String oldData;

    @Column(name = "new_data",  nullable = false, columnDefinition = "TEXT")
    private String newData;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12)
    private State state;

    public Long getId() {
        return id;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public State getState() {
        return state;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public AuditAction getAction() {
        return action;
    }

    public void setAction(AuditAction action) {
        this.action = action;
    }

    public User getResponsibleUser() {
        return responsibleUser;
    }

    public void setResponsibleUser(User responsibleUser) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Audit audit = (Audit) o;
        return Objects.equals(id, audit.id) && Objects.equals(entity, audit.entity) && Objects.equals(recordId, audit.recordId) && action == audit.action && Objects.equals(responsibleUser, audit.responsibleUser) && Objects.equals(changeDate, audit.changeDate) && Objects.equals(oldData, audit.oldData) && Objects.equals(newData, audit.newData) && state == audit.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entity, recordId, action, responsibleUser, changeDate, oldData, newData, state);
    }

    @Override
    public String toString() {
        return "Audit{" +
                "id=" + id +
                ", entity='" + entity + '\'' +
                ", recordId=" + recordId +
                ", action=" + action +
                ", responsibleUser=" + responsibleUser +
                ", changeDate=" + changeDate +
                ", oldData='" + oldData + '\'' +
                ", newData='" + newData + '\'' +
                ", state=" + state +
                '}';
    }
}
