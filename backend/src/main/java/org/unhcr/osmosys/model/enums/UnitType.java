package org.unhcr.osmosys.model.enums;

public enum UnitType implements EnumInterface {
    PERSONAS_INTERES("Personas de Interés", 1),
    PERSONAS("Personas", 2),
    ORGANIZACIONES_INSTITUCIONES("Organizaciones o Instituciones", 3),
    TRANSFERENCIAS("Transferencias", 4);

    private String label;

    private int order;


    UnitType(String label, int order) {
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