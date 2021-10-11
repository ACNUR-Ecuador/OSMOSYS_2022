package org.unhcr.osmosys.model.enums;

public enum AreaType {

    IMPACTO("Impacto"),
    RESULTADOS("Resultados"),
    PRODUCTO("Producto"),
    APOYO("Apoyo"),
    ;

    private String label;

    AreaType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
