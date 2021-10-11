package org.unhcr.osmosys.model.enums;

public enum MeasureType {
	NUMERO("Número enteros"),
	PROPORCION("Proporción"),
	TEXTO("Texto"),
	;


	private String label;

	private MeasureType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}