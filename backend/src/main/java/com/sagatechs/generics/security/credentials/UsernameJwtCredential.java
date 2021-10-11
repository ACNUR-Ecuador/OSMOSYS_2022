package com.sagatechs.generics.security.credentials;

import com.sagatechs.generics.security.CustomPrincipal;
import com.sagatechs.generics.security.model.RoleType;
import com.sagatechs.generics.webservice.webModel.UserWeb;
import org.apache.commons.collections4.CollectionUtils;

import javax.security.enterprise.credential.UsernamePasswordCredential;
import java.util.HashSet;
import java.util.Set;

public class UsernameJwtCredential extends UsernamePasswordCredential {

    private final String username;
    private String token;
    private UserWeb userWeb;

    private CustomPrincipal customPrincipal;

    private Set<RoleType> roles = new HashSet<>();
    private Set<String> rolesS = new HashSet<>();



    @SuppressWarnings("unused")
    public UsernameJwtCredential(UserWeb userWeb, String token, Set<String> roles) {
        super(userWeb.getUsername(), token);
        this.username = userWeb.getUsername();
        this.token = token;
        this.userWeb = userWeb;
        this.customPrincipal = new CustomPrincipal(userWeb);
        this.customPrincipal.setUser(userWeb);
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



    public UserWeb getUserWeb() {
        return userWeb;
    }

    public void setUserWeb(UserWeb userWeb) {
        this.userWeb = userWeb;
    }

    public CustomPrincipal getCustomPrincipal() {
        return customPrincipal;
    }

    public void setCustomPrincipal(CustomPrincipal customPrincipal) {
        this.customPrincipal = customPrincipal;
    }
}
