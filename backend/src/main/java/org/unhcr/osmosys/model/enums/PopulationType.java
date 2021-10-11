package org.unhcr.osmosys.model.enums;

public enum PopulationType {
	REFUGIADOS("Refugiados"),
	SOLICITANTES_DE_ASILO("Solicitantes de Asilo"),
	COMUNIDAD_DE_ACOGIDA("Comidad de Acogida"),
	;


	private String label;

	private PopulationType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}