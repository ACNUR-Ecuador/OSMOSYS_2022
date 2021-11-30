package org.unhcr.osmosys.model.enums;

public enum AreaType  implements EnumInterface{

    IMPACTO("Impacto",1),
    RESULTADOS("Resultados",2),
    PRODUCTO("Producto",3),
    APOYO("Apoyo",4),
    ;

    private String label;

    AreaType(String label, int order) {
        this.label = label;
    }


    @Override
    public String getStringValue() {
        return this.name();
    }

    public String getLabel() {
        return label;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
