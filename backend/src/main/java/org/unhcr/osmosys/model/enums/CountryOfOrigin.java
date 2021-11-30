package org.unhcr.osmosys.model.enums;

public enum CountryOfOrigin implements EnumInterface{
	VENEZUELA("Venezuela",1),
	COLOMBIA("Colombia",2),
	ECUADOR("Ecuatoriana",3),
	OTRO("Otros países",4);


	private String label;
	private int order;

	CountryOfOrigin(String label, int order) {
		this.label = label;
		this.order = order;
	}

	@Override
	public String getStringValue() {
		return this.name();
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