package org.unhcr.osmosys.model.enums;

public enum GenderType  implements EnumInterface{
	MASCULINO("Masculino",1),
	FEMENINO("Femenino",2),
	NO_BINARIE("No Binario",3);

	private String label;

	private int order;


	GenderType(String label, int order) {
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