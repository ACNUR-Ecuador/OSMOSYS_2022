package org.unhcr.osmosys.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OfficeAdministratorId implements Serializable {

    private static final long serialVersionUID = -2765772194175419782L;


    @Column(name = "user_id")
    private Long userId;

    @Column(name = "office_id")
    private Long officeId;

    public OfficeAdministratorId() {
    }

    public OfficeAdministratorId(Long userId, Long officeId) {
        this.userId = userId;
        this.officeId = officeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Long officeId) {
        this.officeId = officeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OfficeAdministratorId)) return false;
        OfficeAdministratorId that = (OfficeAdministratorId) o;
        return userId.equals(that.userId) && officeId.equals(that.officeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, officeId);
    }
}
