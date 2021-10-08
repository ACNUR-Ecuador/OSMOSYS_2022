package org.unhcr.osmosys.model.enums;

public enum AgeType {
    INFANTS("Infantes(0-4)", 1),
    CHILDREN("Niña/os(5-11)", 2),
    TEENAGERS("Adolescentes(12-17)", 3),
    YOUNG_ADULTS("Jóvenes Adultos(18-24)",4),
    ADULTS("Adultos(25-49)",5),
    OLDER_ADULTS("Adultos Mayores(50-59)",6),
    ELDERLY("Tercera Edad(60+)",7);

    private String label;

    private int order;


    AgeType(String label, int order) {
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