package com.sagatechs.generics.persistence.model;
public enum State {
	ACTIVO("ACTIVO"), INACTIVO("INACTIVO");

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