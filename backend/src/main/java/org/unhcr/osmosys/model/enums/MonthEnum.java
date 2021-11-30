package org.unhcr.osmosys.model.enums;

public enum MonthEnum  implements EnumInterface{
    ENERO("ENERO", 1, QuarterEnum.I),
    FEBRERO("FEBRERO", 2, QuarterEnum.I),
    MARZO("MARZO", 3, QuarterEnum.I),
    ABRIL("ABRIL", 4, QuarterEnum.II),
    MAYO("MAYO", 5, QuarterEnum.II),
    JUNIO("JUNIO", 6, QuarterEnum.II),
    JULIO("JULIO", 7, QuarterEnum.III),
    AGOSTO("AGOSTO", 8, QuarterEnum.III),
    SEPTIEMBRE("SEPTIEMBRE", 9, QuarterEnum.III),
    OCTUBRE("OCTUBRE", 10, QuarterEnum.IV),
    NOVIEMBRE("NOVIEMBRE", 11, QuarterEnum.IV),
    DICIEMBRE("DICIEMBRE", 12, QuarterEnum.IV);

    private String label;
    private int order;
    private QuarterEnum quarterEnum;

    MonthEnum(String label, int order, QuarterEnum quarterEnum) {
        this.label = label;
        this.order = order;
        this.quarterEnum = quarterEnum;
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