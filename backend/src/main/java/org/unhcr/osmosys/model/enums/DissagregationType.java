package org.unhcr.osmosys.model.enums;

public enum DissagregationType {
	POPULATION_TYPE("Tipo de población"),
	AGE("Edade"),
	GENDER("Género"),
	SITE("Lugar"),
	COUNTRY_OF_ORIGIN("Pís de Origen"),
	DISABILITY("Discapacidad"),
	;


	private String label;

	private DissagregationType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}