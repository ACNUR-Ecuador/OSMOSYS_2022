package org.unhcr.osmosys.model.enums;

public enum IndicatorType {

    CORE("Core"),
    GOOD_PRACTICE("Buenas Prácticas"),
    OPERATION_DEFINED("Operación"),
    GENERAL_INDICATOR("Indicador General"),

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
