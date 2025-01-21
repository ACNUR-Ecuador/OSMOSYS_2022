package org.unhcr.osmosys.model.enums;

public enum OfficeType  implements EnumInterface{
    BO("Oficina de País", 1),
    SUB_OFICINA("Sub-Oficina", 2),
    OFICINA_DE_CAMPO("Oficina de Terreno", 3),
    UNIDAD_DE_CAMPO("Unidad de Terreno", 4),
    OFICINA_NACIONAL("Oficina Nacional", 5),
    PRESENCIA("Presencia", 6),
    UNIDAD("Unidad o Área Técnica", 7),
    OFICINA_MULTI_PAIS("Oficina Multi-País", 8);


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