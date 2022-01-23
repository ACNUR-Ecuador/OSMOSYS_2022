package org.unhcr.osmosys.model.enums;

public enum SourceType implements EnumInterface {
    PROGRES("ProGres", 1),
    SISTEMA_ORGANIZACION("Sistema de la Organización", 2),
    REGISTRO_HOJA_CALCULO("Registros en Hojas de Cálculo(Ejem: Excel/Google Sheets)", 3),
    FORMULARIOS_EN_LINEA("Formularios en linea (Ejem: Kobo)", 3),
    REGISTROS_MANUALES("Registros Manuales", 4),
    OTROS("Otro", 5)
    ;

    private String label;

    private int order;


    SourceType(String label, int order) {
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