package org.unhcr.osmosys.model.enums;

public enum DissagregationType  implements EnumInterface{
	TIPO_POBLACION("Tipo de población",1),
	EDAD("Edad",2),
	GENERO("Género",3),
	LUGAR("Lugar",4),
	PAIS_ORIGEN("País de Origen",5),
	DISCAPACIDAD("Discapacidad",6),
	;


	private String label;
	private int order;

	private DissagregationType(String label, int order) {
		this.label = label;
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