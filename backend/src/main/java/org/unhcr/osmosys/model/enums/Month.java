package org.unhcr.osmosys.model.enums;

public enum Month {
    ENERO("ENERO", 1,1),
    FEBRERO("FEBRERO", 2,1),
    MARZO("MARZO", 3,1),
    ABRIL("ABRIL", 4,2),
    MAYO("MAYO", 5,2),
    JUNIO("JUNIO", 6,2),
    JULIO("JULIO", 7,3),
    AGOSTO("AGOSTO", 8,3),
    SEPTIEMBRE("SEPTIEMBRE", 9,3),
    OCTUBRE("OCTUBRE", 10,4),
    NOVIEMBRE("NOVIEMBRE", 11,4),
    DICIEMBRE("DICIEMBRE", 12,4);

    private String label;
    private int order;
    private int quarter;

    Month(String label, int order, int quarter) {
        this.label = label;
        this.order = order;
        this.quarter = quarter;
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