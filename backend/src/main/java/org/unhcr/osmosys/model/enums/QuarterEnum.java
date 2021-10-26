package org.unhcr.osmosys.model.enums;

public enum QuarterEnum {
    I("I", 1),
    II("II", 2),
    III("III", 3),
    IV("IV", 4);

    private String label;
    private int order;
    private int quarter;

    QuarterEnum(String label, int order) {
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