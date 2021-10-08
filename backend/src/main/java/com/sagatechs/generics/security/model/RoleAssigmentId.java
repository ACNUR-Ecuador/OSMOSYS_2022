package com.sagatechs.generics.security.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@SuppressWarnings("JpaDataSourceORMInspection")
@Embeddable
public class RoleAssigmentId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "role_id")
    private Long roleId;

    public RoleAssigmentId() {
    }

    @SuppressWarnings("WeakerAccess")
    public RoleAssigmentId(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    @SuppressWarnings("unused")
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RoleAssigmentId that = (RoleAssigmentId) o;

        return new EqualsBuilder()
                .append(userId, that.userId)
                .append(roleId, that.roleId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(userId)
                .append(roleId)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "RoleAssigmentId{" +
                "userId=" + userId +
                ", roleId=" + roleId +
                '}';
    }
}
