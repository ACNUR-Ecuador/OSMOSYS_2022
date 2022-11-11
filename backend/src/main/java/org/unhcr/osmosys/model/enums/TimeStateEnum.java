package org.unhcr.osmosys.model.enums;

public enum TimeStateEnum implements EnumInterface {
    ON_TIME("A tiempo", 1),
    SOON_REPORT("Próximo a reportar", 2),
    LATE("Atrasado", 3),
    NO_TIME("Por reportar", 4),
    INVALID("No válido", 5);

    private String label;
    private int order;

    TimeStateEnum(String label, int order) {
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

    public static TimeStateEnum addStates(TimeStateEnum timeStateEnum1, TimeStateEnum timeStateEnum2) {
        if (timeStateEnum1.equals(TimeStateEnum.INVALID) || timeStateEnum2.equals(TimeStateEnum.INVALID)) {
            return TimeStateEnum.INVALID;
        } else if (timeStateEnum1.equals(TimeStateEnum.LATE) || timeStateEnum2.equals(TimeStateEnum.LATE)) {
            return TimeStateEnum.LATE;
        } else if (timeStateEnum1.equals(TimeStateEnum.SOON_REPORT) || timeStateEnum2.equals(TimeStateEnum.SOON_REPORT)) {
            return TimeStateEnum.SOON_REPORT;
        } else if (timeStateEnum1.equals(TimeStateEnum.ON_TIME) || timeStateEnum2.equals(TimeStateEnum.ON_TIME)) {
            return TimeStateEnum.ON_TIME;
        } else {
            return TimeStateEnum.NO_TIME;
        }
    }

}