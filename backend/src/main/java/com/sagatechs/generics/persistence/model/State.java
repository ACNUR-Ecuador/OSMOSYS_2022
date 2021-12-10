package com.sagatechs.generics.persistence.model;

import org.unhcr.osmosys.model.enums.EnumInterface;

public enum State implements EnumInterface {
    ACTIVO("Activo",1), INACTIVO("Inactivo",2);

    private String label;
    private int order;

    private State(String label, int order) {
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