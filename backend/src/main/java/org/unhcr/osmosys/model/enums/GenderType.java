package org.unhcr.osmosys.model.enums;

public enum GenderType {
	MASCULINO("Masculino",1),
	FEMENINO("Femenino",2),
	OTRO("Otros",3);

	private String label;

	private int order;


	GenderType(String label, int order) {
		this.label = label;
		this.order = order;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

}