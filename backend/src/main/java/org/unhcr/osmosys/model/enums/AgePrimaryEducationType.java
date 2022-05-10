package org.unhcr.osmosys.model.enums;

public enum AgePrimaryEducationType implements EnumInterface {
    PREPRIMARIA("Pre Primaria (3-4)", 1),
    PRIMARIA("Primaria (5-11)", 2),
    SECUNDARIA("Secundaria (12-17)", 3),
    ADULTOS("Adultos (18-59)", 4),
    ADULTOS_MAYORES("Adultos Mayores (60+)", 5);

    private String label;

    private int order;


    AgePrimaryEducationType(String label, int order) {
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