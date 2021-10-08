package com.sagatechs.generics.security.model;

public enum RoleType {

    SUPER_ADMINISTRADOR("Super Administrador"),
	ADMINISTRADOR("Administrador"),
	MONITOR_DE_PROGRAMAS("Monitor de Programas"),
    EJECUTOR_DE_PROGRAMAS("Ejecutor de Programas"),
	EJECUTOR_PROYECTOS("Ejecutar de Proyectos"),
    MONITOR_PROYECTOS("Monitor de Proyectos"),
    EJECUTOR_ID("Ejecutor Implementación Directa"),
    MONITOR_ID("Monitor Implementación Directa");

    RoleType(String label) {
        this.label = label;
    }

    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
