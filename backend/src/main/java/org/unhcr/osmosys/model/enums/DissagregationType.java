package org.unhcr.osmosys.model.enums;

public enum DissagregationType {
	TIPO_POBLACION("Tipo de población"),
	EDAD("Edad"),
	GENERO("Género"),
	LUGAR("Lugar"),
	PAIS_ORIGEN("País de Origen"),
	DISCAPACIDAD("Discapacidad"),
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