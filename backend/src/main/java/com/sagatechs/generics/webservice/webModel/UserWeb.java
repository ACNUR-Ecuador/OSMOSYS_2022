package com.sagatechs.generics.webservice.webModel;

import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.webServices.model.OfficeWeb;
import org.unhcr.osmosys.webServices.model.OrganizationWeb;

import java.util.ArrayList;
import java.util.List;

public class UserWeb {

	private Long id;
	private String name;
	private String username;
	private String email;
	private State state;
	private List<RoleWeb> roles = new ArrayList<>();
	private OrganizationWeb organization;
	private OfficeWeb office;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public List<RoleWeb> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleWeb> roles) {
		this.roles = roles;
	}

	public OrganizationWeb getOrganization() {
		return organization;
	}

	public void setOrganization(OrganizationWeb organization) {
		this.organization = organization;
	}

	public OfficeWeb getOffice() {
		return office;
	}

	public void setOffice(OfficeWeb office) {
		this.office = office;
	}
}
