package org.unhcr.osmosys.model.enums;

public enum QuarterReportCalculation implements EnumInterface {
    ALL_REPORT_SUM("Suma de todo lo reportado",1),
    AGGREGATION_RULE("Regla de Agregación",2);


    private String label;
    private int order;

    QuarterReportCalculation( String label, int order) {
        this.order = order;
        this.label = label;
    }

    @Override
    public String getStringValue() {
        return this.name();
    }

    @Override
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }


}
