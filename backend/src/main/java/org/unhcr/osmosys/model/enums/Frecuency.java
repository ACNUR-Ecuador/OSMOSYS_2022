package org.unhcr.osmosys.model.enums;

public enum Frecuency implements EnumInterface {
    MENSUAL("Mensual",1),
    TRIMESTRAL("Trimestral",2),
    SEMESTRAL("Semestral",3),
    ANUAL("Anual",4);


    private String label;
    private final int order;

    Frecuency(String label, int order) {
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
        return order;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}