package org.unhcr.osmosys.model.enums;

public enum AreaType {

    IMPACT("Impacto"), OUTCOME("Outcome"),  Output("Resultado");

    private String label;

    AreaType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
