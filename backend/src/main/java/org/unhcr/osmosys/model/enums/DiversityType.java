package org.unhcr.osmosys.model.enums;

public enum DiversityType implements EnumInterface {
    DISCAPACITADOS("Personas con Discapacidad", 1),
    LGBTI("LGBTIQ+", 2),
    INDIGENAS("Indígenas", 3),
    AFRODESCENDIENTES("Afrodescendientes", 4);

    private String label;

    private int order;


    DiversityType(String label, int order) {
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