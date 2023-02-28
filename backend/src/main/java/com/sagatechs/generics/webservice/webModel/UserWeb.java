package com.sagatechs.generics.webservice.webModel;

import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.webServices.model.OfficeWeb;
import org.unhcr.osmosys.webServices.model.OrganizationWeb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserWeb implements Serializable {

	private Long id;
	private String name;
	private String username;
	private String email;
	private State state;
	private List<RoleWeb> roles = new ArrayList<>();
	private OrganizationWeb organization;
	private OfficeWeb office;
	private List<Long> focalPointProjects;
	private List<OfficeWeb> administratedOffices;


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

	public List<Long> getFocalPointProjects() {
		return focalPointProjects;
	}

	public void setFocalPointProjects(List<Long> focalPointProjects) {
		this.focalPointProjects = focalPointProjects;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (o == null || getClass() != o.getClass()) return false;

		UserWeb userWeb = (UserWeb) o;

		return new EqualsBuilder().append(id, userWeb.id).append(username, userWeb.username).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(id).append(username).toHashCode();
	}

	@Override
	public String toString() {
		return "UserWeb{" +
				"id=" + id +
				", name='" + name + '\'' +
				", username='" + username + '\'' +
				", email='" + email + '\'' +
				", state=" + state +
				", organization=" + organization +
				", office=" + office +
				'}';
	}

	public List<OfficeWeb> getAdministratedOffices() {
		return administratedOffices;
	}

	public void setAdministratedOffices(List<OfficeWeb> administratedOffices) {
		this.administratedOffices = administratedOffices;
	}
}
