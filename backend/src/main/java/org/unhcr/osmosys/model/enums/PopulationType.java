package org.unhcr.osmosys.model.enums;

public enum PopulationType implements EnumInterface {
    REFUGIADOS("Refugiados",1),
    SOLICITANTES_DE_ASILO("Solicitantes de Asilo",2),
    VENEZOLANOS_DESPLAZADOS("Venezolanos Desplazados en el Extranjero",3),
    COMUNIDAD_DE_ACOGIDA("Comidad de Acogida",4),
    OTROS_DE_INTERES("Otros de interés",5),
    ;


    private String label;
    private int order;

    private PopulationType(String label, int order) {
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