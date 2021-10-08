package com.sagatechs.generics.security.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.NaturalId;
import org.unhcr.osmosys.model.Office;
import org.unhcr.osmosys.model.Organization;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user", schema = "security",
        indexes = {@Index(name = "index_user_username",  columnList="username", unique = true)})
public class User extends BaseEntity<Long> {

    public User() {
        super();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @NaturalId
    @NotEmpty(message = "El nombre de usuario es un dato obligatorio")
    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @Email(message = "El correo no es una dirección válida")
    @Column(name = "email", length = 50, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private byte[] password;

    @NotNull(message = "El estado es un dato obligatorio")
    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @OneToMany(mappedBy = "user")
    private Set<RoleAssigment> roleAssigments = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "organization_id", foreignKey = @ForeignKey(name = "fk_user_organization"))
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "office_id", foreignKey = @ForeignKey(name = "fk_user_office"))
    private Office office;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Set<RoleAssigment> getRoleAssigments() {
        return roleAssigments;
    }

    public void setRoleAssigments(Set<RoleAssigment> roleAssigments) {
        this.roleAssigments = roleAssigments;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addRole(Role role){


        // busco si setá incativo, y los activo
        for (RoleAssigment roleAssigment : roleAssigments) {
            if (roleAssigment.getUser().equals(this) && roleAssigment.getRole().equals(role)) {
                roleAssigment.setState(State.ACTIVE);
                return;
            }
        }


        RoleAssigment roleAssigment = new RoleAssigment(this, role);
        this.roleAssigments.add(roleAssigment);
        role.getRoleAssigments().add(roleAssigment);
    }

    public void removeRole(Role role){

        for (RoleAssigment roleAssigment : roleAssigments) {
            if (roleAssigment.getUser().equals(this) && roleAssigment.getRole().equals(role)) {
                roleAssigment.setState(State.INACTIVE);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(username, user.username)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(username)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", state=" + state +
                '}';
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }
}
