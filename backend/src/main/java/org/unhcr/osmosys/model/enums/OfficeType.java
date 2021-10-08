package org.unhcr.osmosys.model.enums;

public enum OfficeType {
    BO("BO", 1),
    SUB_OFFICE("Sub oficina", 2),
    FIELD_OFFICE("Oficina de campo", 3),
    FIELD_UNIT("Unidad de campo", 4);


    private String label;

    private int order;


    OfficeType(String label, int order) {
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