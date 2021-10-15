package com.sagatechs.generics.security.model;


import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author sebas
 */
@Entity
@Table(name = "RoleAssigment", schema = "security")
public class RoleAssigment extends BaseEntity<RoleAssigmentId> {

    @EmbeddedId
    private RoleAssigmentId id;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private @NotNull(message = "El estado es un dato obligatorio") State state;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("roleId")
    private Role role;

    public RoleAssigment() {
    }

    @SuppressWarnings("WeakerAccess")
    public RoleAssigment(User user, Role role) {
        this.user = user;
        this.role = role;
        this.id = new RoleAssigmentId(user.getId(), role.getId());
        this.state = State.ACTIVO;
    }

    public RoleAssigment(User user, Role role, State state) {
        this.user = user;
        this.role = role;
        this.id = new RoleAssigmentId(user.getId(), role.getId());
        this.state = state;
    }

    @Override
    public RoleAssigmentId getId() {
        return id;
    }

    public void setId(RoleAssigmentId id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RoleAssigment that = (RoleAssigment) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(id, that.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(id)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "RoleAssigment{" +
                "id=" + id +
                ", state=" + state +
                '}';
    }
}
