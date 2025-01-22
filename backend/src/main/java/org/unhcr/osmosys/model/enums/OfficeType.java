package org.unhcr.osmosys.model.enums;

public enum OfficeType  implements EnumInterface{
    OFICINA_MULTI_PAIS("Oficina Multi-País", 1),
    BO("Oficina de País", 2),
    OFICINA_NACIONAL("Oficina Nacional", 3),
    SUB_OFICINA("Sub-Oficina", 4),
    OFICINA_DE_CAMPO("Oficina de Terreno", 5),
    UNIDAD_DE_CAMPO("Unidad de Terreno", 6),
    PRESENCIA("Presencia", 7),
    UNIDAD("Unidad o Área Técnica", 8);



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