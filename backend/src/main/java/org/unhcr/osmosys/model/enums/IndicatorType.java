package org.unhcr.osmosys.model.enums;

public enum IndicatorType  implements EnumInterface{

    CORE("Core",1),
    BUENAS_PRACTICAS("Buenas Prácticas",2),
    OPERACION("Operación",3),
    GENERAL("General",4),

    ;

    private String label;
    private final int order;

    IndicatorType(String label, int order) {
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
