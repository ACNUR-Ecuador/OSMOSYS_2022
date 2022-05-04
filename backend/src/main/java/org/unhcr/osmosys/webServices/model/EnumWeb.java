package org.unhcr.osmosys.webServices.model;

import java.io.Serializable;


public class EnumWeb implements Serializable {
    private String value;
    private String label;
    private int order;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
