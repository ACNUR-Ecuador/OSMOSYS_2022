package org.unhcr.osmosys.model.enums;

public enum MeasureType implements EnumInterface {
    NUMERO("Número enteros",1),
    PROPORCION("Proporción",2),
    TEXTO("Texto",3),
    ;


    private String label;
    private int order;

    private MeasureType(String label, int order) {
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

    @Override
    public int getOrder() {
        return this.order;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}