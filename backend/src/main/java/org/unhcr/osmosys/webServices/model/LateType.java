package org.unhcr.osmosys.webServices.model;

import org.unhcr.osmosys.model.enums.EnumInterface;

public enum LateType implements EnumInterface {

    ON_TIME("A tiempo",1),
    WARNING("Alerta",2),
    LATE("Retrasado",3),
    ;


    private String label;
    private int order;

    private LateType(String label, int order) {
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
