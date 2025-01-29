package com.sagatechs.generics.security.model;

import org.unhcr.osmosys.model.enums.EnumInterface;

public enum RoleType implements EnumInterface {

    SUPER_ADMINISTRADOR("Super Administrador", 1),
    ADMINISTRADOR_REGIONAL("Administrador Regional", 2),
    ADMINISTRADOR_LOCAL("Administrador Local", 3),
    //ADMINISTRADOR("Administrador", 4),
    // MONITOR_DE_PROGRAMAS("Monitor de Programas", 3),
    // EJECUTOR_DE_PROGRAMAS("Ejecutor de Programas", 4),
    EJECUTOR_PROYECTOS("Responsable de Reporte (Socio)", 5),
    MONITOR_PROYECTOS("Monitor (Socio)", 6),
    EJECUTOR_ID("Responsable de Reporte Implementación Directa", 7),
    MONITOR_ID("Monitor (ACNUR)", 8),
    PUNTO_FOCAL("Responsable del Proyecto (ACNUR)", 9),
    ADMINISTRADOR_OFICINA("Jefe de Oficina/Unidad (ACNUR)", 10),
    RESULT_MANAGER("Mánager de Resultados (ACNUR)", 11),
    SUPERVISOR_REPORTE_SOCIO("Supervisor de Reporte (Socio)", 12),
    SUPERVISOR_REPORTE_ID("Supervisor de Reporte Implemetación Directa",13);

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
