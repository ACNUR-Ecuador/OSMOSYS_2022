package org.unhcr.osmosys.model.enums;

public enum MarkerType {
	ORGANIZATIONAL("Operacional"),
	OPERATIONAL("Organizacional");


	private String label;

	private MarkerType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}