package org.unhcr.osmosys.model.enums;

public enum CountryOfOrigin {
	VENEZUELAN("Venezuela",1),
	COLOMBIAN("Colombia",2),
	ECUADORIAN("Ecuatoriana",3),
	OTHER("Otros países",4);


	private String label;
	private int order;

	CountryOfOrigin(String label, int order) {
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