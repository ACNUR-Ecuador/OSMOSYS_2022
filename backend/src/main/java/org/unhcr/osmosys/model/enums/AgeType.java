package org.unhcr.osmosys.model.enums;

public enum AgeType implements EnumInterface{
    INFANTES("Infantes(0-4)", 1),
    NINOS("Niña/os(5-11)", 2),
    ADOLESCENTES("Adolescentes(12-17)", 3),
    JOVENES_ADULTOS("Jóvenes Adultos(18-24)",4),
    ADULTOS("Adultos(25-49)",5),
    ADULTOS_MAYORES("Adultos Mayores(50-59)",6),
    TERCERA_EDAD("Tercera Edad(60+)",7);

    private String label;

    private int order;


    AgeType(String label, int order) {
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