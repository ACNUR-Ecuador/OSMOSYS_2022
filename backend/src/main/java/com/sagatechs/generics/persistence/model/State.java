package com.sagatechs.generics.persistence.model;
public enum State {
	ACTIVE("ACTIVO"), INACTIVE("INACTIVO");

	private String label;

	private State(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}