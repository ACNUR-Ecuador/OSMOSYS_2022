package org.unhcr.osmosys.model.enums;

public enum PopulationType {
	REFUGEES("Refugiados"),
	ASYLUM_SEEKERS("Solicitantes de Asilo"),
	HOST_COMUNITY("Comidad de Acogida"),
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