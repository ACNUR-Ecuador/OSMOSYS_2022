package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.model.User;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "offices_administrators", schema = "osmosys")
public class OfficeAdministrator {

    public OfficeAdministrator() {

    }

    public OfficeAdministrator(User administrator, Office office) {
        this.office = office;
        this.administrator = administrator;
        this.id=new OfficeAdministratorId(administrator.getId(), office.getId());
        this.state=State.ACTIVO;
    }

    @EmbeddedId
    private OfficeAdministratorId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("officeId")
    private Office office;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User administrator;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    public OfficeAdministratorId getId() {
        return id;
    }

    public void setId(OfficeAdministratorId id) {
        this.id = id;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public User getAdministrator() {
        return administrator;
    }

    public void setAdministrator(User administrator) {
        this.administrator = administrator;
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
        if (!(o instanceof OfficeAdministrator)) return false;
        OfficeAdministrator that = (OfficeAdministrator) o;
        return id.equals(that.id) && office.equals(that.office) && administrator.equals(that.administrator) && state == that.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, office, administrator, state);
    }
}