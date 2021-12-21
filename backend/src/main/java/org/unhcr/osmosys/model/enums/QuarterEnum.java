package org.unhcr.osmosys.model.enums;

import com.sagatechs.generics.exceptions.GeneralAppException;

public enum QuarterEnum  implements EnumInterface{
    I("I", 1),
    II("II", 2),
    III("III", 3),
    IV("IV", 4);

    private String label;
    private int order;
    private int quarter;

    QuarterEnum(String label, int order) {
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

    public static  QuarterEnum getByQuarterNumber(int quarterNumber) throws GeneralAppException {
        switch (quarterNumber){
            case 1: return QuarterEnum.I;
            case 2: return QuarterEnum.II;
            case 3: return QuarterEnum.III;
            case 4: return QuarterEnum.IV;
            default:{
                throw new GeneralAppException("Quarter enum no válido "+quarterNumber);
            }
        }
    }
}