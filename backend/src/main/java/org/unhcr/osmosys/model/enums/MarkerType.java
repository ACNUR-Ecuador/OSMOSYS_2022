package org.unhcr.osmosys.model.enums;

public enum MarkerType  implements EnumInterface{
	ORGANIZACIONAL("Organizacional",1),
	OPERACIONAL("Operacional",2);


	private String label;
	private final int order;

	MarkerType(String label, int order) {
		this.label = label;
		this.order=order;
	}

	@Override
	public String getStringValue() {
		return this.name();
	}

	public String getLabel() {
		return label;
	}

	@Override
	public int getOrder() {
		return this.order;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}