package org.unhcr.osmosys.model.enums;

public enum Frecuency {
	MONTHLY("Mensual"),
	QUARTERLY("Trimestral"),
	HALF("Semestral"),
	ANNUAL("Anual")
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