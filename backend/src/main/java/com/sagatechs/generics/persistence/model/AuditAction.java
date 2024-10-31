package com.sagatechs.generics.persistence.model;

import org.unhcr.osmosys.model.enums.EnumInterface;

public enum AuditAction implements EnumInterface {
    INSERT("Inserción",1), UPDATE("Actualización",2),DEACTIVE("Desactivación",3), ACTIVE("Activación",4), LOCK("Bloqueo",5), UNLOCK("Desbloqueo",6), REPORT("Reporte", 7);

    private String label;
    private final int order;

    AuditAction(String label, int order) {
        this.label = label;
        this.order = order;
    }

    @Override
    public String getStringValue() {
        return this.name();
    }

    @Override
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
