package org.unhcr.osmosys.model.enums;

public enum DiversityType implements EnumInterface {
    LGBTI("LGBTIQ+", 1),
    DISCAPACITADOS("Discapacitados", 2),
    ADOLESCENTES("Indígenas", 3),
    JOVENES_ADULTOS("Afrodescendientes", 4);

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