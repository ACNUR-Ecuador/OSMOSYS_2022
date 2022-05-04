package org.unhcr.osmosys.model.enums;

import com.sagatechs.generics.exceptions.GeneralAppException;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum MonthEnum implements EnumInterface {
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

    public QuarterEnum getQuarterEnum() {
        return quarterEnum;
    }

    public void setQuarterEnum(QuarterEnum quarterEnum) {
        this.quarterEnum = quarterEnum;
    }

    public static MonthEnum getMonthByNumber(int monthNumber) throws GeneralAppException {
        switch (monthNumber) {
            case 1:
                return MonthEnum.ENERO;
            case 2:
                return MonthEnum.FEBRERO;
            case 3:
                return MonthEnum.MARZO;
            case 4:
                return MonthEnum.ABRIL;
            case 5:
                return MonthEnum.MAYO;
            case 6:
                return MonthEnum.JUNIO;
            case 7:
                return MonthEnum.JULIO;
            case 8:
                return MonthEnum.AGOSTO;
            case 9:
                return MonthEnum.SEPTIEMBRE;
            case 10:
                return MonthEnum.OCTUBRE;
            case 11:
                return MonthEnum.NOVIEMBRE;
            case 12:
                return MonthEnum.DICIEMBRE;
            default: {
                throw new GeneralAppException("Month enum no válido " + monthNumber);
            }
        }
    }

    public static QuarterEnum getQuarterByMonthNumber(int monthNumber) throws GeneralAppException {
        switch (monthNumber) {
            case 1:
            case 2:
            case 3:
                return QuarterEnum.I;
            case 4:
            case 5:
            case 6:
                return QuarterEnum.II;
            case 7:
            case 8:
            case 9:
                return QuarterEnum.III;
            case 10:
            case 11:
            case 12:
                return QuarterEnum.IV;
            default: {
                throw new GeneralAppException("Month number no válido " + monthNumber);
            }
        }
    }

    public static List<MonthEnum> getMonthsByQuarter(QuarterEnum quarterEnum) {
        return Arrays.stream(MonthEnum.values()).filter(monthEnum -> monthEnum.getQuarterEnum().equals(quarterEnum)).sorted(Comparator.comparingInt(MonthEnum::getOrder)).collect(Collectors.toList());
    }
}