package org.unhcr.osmosys.model.enums;

public enum MeasureType {
	INTEGER("Número enteros"),
	PROPORTION("Proporción"),
	TEXT("Texto"),
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