package org.unhcr.osmosys.model.enums;

public enum TotalIndicatorCalculationType {
    SUMA("Suma", 1),
    PROMEDIO("Promedio", 2),
    MAXIMO("Máximo", 3),
    MINIMO("Mínimo", 4),
    ;

    private String label;

    private int order;


    TotalIndicatorCalculationType(String label, int order) {
        this.label = label;
        this.order = order;
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