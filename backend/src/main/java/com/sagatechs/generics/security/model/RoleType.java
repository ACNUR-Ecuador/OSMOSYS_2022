package com.sagatechs.generics.security.model;

import org.unhcr.osmosys.model.enums.EnumInterface;

public enum RoleType implements EnumInterface {

    SUPER_ADMINISTRADOR("Super Administrador", 1),
    ADMINISTRADOR_REGIONAL("Administrador Regional", 2),
    ADMINISTRADOR_LOCAL("Administrador Local", 3),
    //ADMINISTRADOR("Administrador", 4),
    // MONITOR_DE_PROGRAMAS("Monitor de Programas", 3),
    // EJECUTOR_DE_PROGRAMAS("Ejecutor de Programas", 4),
    EJECUTOR_PROYECTOS("Ejecutor de Proyectos", 5),
    MONITOR_PROYECTOS("Monitor de Proyectos", 6),
    EJECUTOR_ID("Ejecutor Implementación Directa", 7),
    MONITOR_ID("Monitor Implementación Directa", 8),
    PUNTO_FOCAL("Punto Focal", 9),
    ADMINISTRADOR_OFICINA("Administrador de Oficina", 10);


    RoleType(String label, int order) {
        this.label = label;
        this.order = order;
    }

    private String label;
    private int order;

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
