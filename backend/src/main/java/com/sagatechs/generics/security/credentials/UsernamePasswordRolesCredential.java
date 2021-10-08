package com.sagatechs.generics.security.credentials;


import com.sagatechs.generics.security.model.RoleType;
import org.apache.commons.collections4.CollectionUtils;

import javax.security.enterprise.credential.UsernamePasswordCredential;
import java.util.HashSet;
import java.util.Set;

public class UsernamePasswordRolesCredential extends UsernamePasswordCredential {

	private Set<RoleType> roles = new HashSet<>();
	private Set<String> rolesS = new HashSet<>();



	public UsernamePasswordRolesCredential(String callerName, String password) {
		super(callerName, password);

	}


	@SuppressWarnings("unused")
	public UsernamePasswordRolesCredential(String callerName, String password, Set<RoleType> roles) {
		super(callerName, password);
		this.roles = roles;
		if (CollectionUtils.isNotEmpty(roles)) {
			setRoles(roles);
		}
	}

	@SuppressWarnings("unused")
	public Set<RoleType>  getRoles() {
		return roles;
	}

	@SuppressWarnings("WeakerAccess")
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

}