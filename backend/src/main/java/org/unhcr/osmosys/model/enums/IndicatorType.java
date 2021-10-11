package org.unhcr.osmosys.model.enums;

public enum IndicatorType {

    CORE("Core"),
    BUENAS_PRACTICAS("Buenas Prácticas"),
    OPERACION("Operación"),
    GENERAL("General"),

    ;

    private String label;

    IndicatorType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
