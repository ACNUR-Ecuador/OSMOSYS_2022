package org.unhcr.osmosys.model.enums;

public enum PopulationType implements EnumInterface {
    REFUGIADOS_SOLICITANTES_DE_ASILO("Población refugiada/solicitante de Asilo",1),
    DESPLAZADOS("Población desplazada",2),
    OTROS_DE_INTERES("Otros de interés",3),
    POBLACION_DE_ACOGIDA("Población de acogida",4),
    ;


    private String label;
    private final int order;

    PopulationType(String label, int order) {
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