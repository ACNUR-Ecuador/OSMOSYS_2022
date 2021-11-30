package org.unhcr.osmosys.model.enums;

public enum OfficeType  implements EnumInterface{
    BO("BO", 1),
    SUB_OFICINA("Sub oficina", 2),
    OFICINA_DE_CAMPO("Oficina de campo", 3),
    UNIDAD_DE_CAMPO("Unidad de campo", 4);


    private String label;

    private int order;


    OfficeType(String label, int order) {
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