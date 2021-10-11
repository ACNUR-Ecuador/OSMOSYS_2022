package org.unhcr.osmosys.model.enums;

public enum Frecuency {
	MENSUAL("Mensual"),
	TRIMESTRAL("Trimestral"),
	SEMESTRAL("Semestral"),
	ANUAL("Anual")
	;


	private String label;

	private Frecuency(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}