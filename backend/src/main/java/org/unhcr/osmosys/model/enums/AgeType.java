package org.unhcr.osmosys.model.enums;

public enum AgeType implements EnumInterface {
    INFANTES("Infantes(0-4)", 1),
    NINOS("Niña/os y Adolescentes (5-17)", 2),
    ADULTOS("Adultos(18-59)", 3),
    ADULTOS_MAYORES("Adultos Mayores(60+)", 4);

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