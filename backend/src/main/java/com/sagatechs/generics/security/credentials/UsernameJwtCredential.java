package com.sagatechs.generics.security.credentials;

import com.sagatechs.generics.security.model.RoleType;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.security.enterprise.credential.Credential;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class UsernameJwtCredential implements Credential {

    private final String username;
    private String token;

    private Set<RoleType> roles = new HashSet<>();
    private Set<String> rolesS = new HashSet<>();


    @SuppressWarnings("unused")
    public UsernameJwtCredential(String username, String token, Set<String> roles) {
        this.username = username;
        this.token = token;
    }

    public UsernameJwtCredential(String token) {
        this.username = "";
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @SuppressWarnings("unused")
    public Set<RoleType> getRoles() {
        return roles;
    }

    @SuppressWarnings("unused")
    public void setRoles(Set<RoleType> roles) {
        this.roles = roles;
        if (CollectionUtils.isNotEmpty(roles)) {
            this.rolesS = new HashSet<>();
            for (RoleType roleType : roles) {
                rolesS.add(roleType.name());
            }
        }
    }

    @SuppressWarnings("unused")
    public Set<String> getRolesS() {
        return rolesS;
    }

    @SuppressWarnings("unused")
    public void setRolesS(Set<String> rolesS) {
        this.rolesS = rolesS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsernameJwtCredential that = (UsernameJwtCredential) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, token);
    }

    @Override
    public boolean isCleared() {
        return StringUtils.isNotBlank(this.token);
    }

    @Override
    public void clear() {
        this.token = null;
    }

}
