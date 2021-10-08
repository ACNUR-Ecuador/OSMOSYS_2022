package com.sagatechs.generics.security.model;


import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role", schema = "security")
public class Role extends BaseEntity<Long> {

    public Role() {
        super();

    }


    public Role(@NotNull(message = "El rol es un dato obligatorio") RoleType roleType,
                @NotNull(message = "El estado es un dato obligatorio") State state) {
        super();
        this.roleType = roleType;
        this.state = state;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El rol es un dato obligatorio")
    @Column(name = "tipo_rol", nullable = false, length = 50, unique = true)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @OneToMany( mappedBy = "role" )
    private Set<RoleAssigment> roleAssigments = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public State getState() {
        return state;
    }

    public void setState(State estado) {
        this.state = estado;
    }

    @SuppressWarnings("WeakerAccess")
    public Set<RoleAssigment> getRoleAssigments() {
        return roleAssigments;
    }

    public void setRoleAssigments(Set<RoleAssigment> roleAssigments) {
        this.roleAssigments = roleAssigments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(roleType, role.roleType)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(roleType)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", roleType=" + roleType +
                ", state=" + state +
                '}';
    }
}
