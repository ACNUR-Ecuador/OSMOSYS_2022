package org.unhcr.osmosys.model.enums;

public enum Month {
    JANUARY("ENERO", 1,1),
    FEBRUARY("FEBRERO", 2,1),
    MARCH("MARZO", 3,1),
    APRIL("ABRIL", 4,2),
    MAY("MAYO", 5,2),
    JUNE("JUNIO", 6,2),
    JULY("JULIO", 7,3),
    AUGUST("AGOSTO", 8,3),
    SEPTEMBER("SEPTIEMBRE", 9,3),
    OCTOBER("OCTUBRE", 10,4),
    NOVEMBER("NOVIEMBRE", 11,4),
    DECEMBER("DICIEMBRE", 12,4);

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