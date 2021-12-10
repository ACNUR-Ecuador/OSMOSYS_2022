package org.unhcr.osmosys.model.enums;

public enum DissagregationType  implements EnumInterface{
	TIPO_POBLACION("Tipo de población",1),
	EDAD("Edad",2),
	GENERO("Género",3),
	LUGAR("Lugar",4),
	PAIS_ORIGEN("País de Origen",5),
	DISCAPACIDAD("Discapacidad",6),
	TIPO_POBLACION_Y_GENERO("Tipo de población	y género",7),
	TIPO_POBLACION_Y_EDAD("Tipo de población y edad",8),
	TIPO_POBLACION_Y_DIVERSIDAD("Tipo de población y diversidad",9),
	TIPO_POBLACION_Y_PAIS_ORIGEN("Tipo de población y país de origen",10),
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