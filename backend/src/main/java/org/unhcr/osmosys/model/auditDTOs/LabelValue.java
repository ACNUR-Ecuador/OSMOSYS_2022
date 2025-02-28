package org.unhcr.osmosys.model.auditDTOs;

public class LabelValue {
    private String label;
    private String value;

    public LabelValue(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }
}
