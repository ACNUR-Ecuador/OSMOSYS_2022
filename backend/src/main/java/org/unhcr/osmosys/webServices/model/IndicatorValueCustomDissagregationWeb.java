package org.unhcr.osmosys.webServices.model;

import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.enums.*;

import java.io.Serializable;
import java.math.BigDecimal;


public class IndicatorValueCustomDissagregationWeb implements Serializable {


    private Long id;
    private State state;
    private MonthEnum monthEnum;
    CustomDissagregationOptionWeb customDissagregationOption;
    private Boolean showValue;
    private BigDecimal value;
    private BigDecimal denominatorValue;
    private BigDecimal numeratorValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public MonthEnum getMonthEnum() {
        return monthEnum;
    }

    public void setMonthEnum(MonthEnum monthEnum) {
        this.monthEnum = monthEnum;
    }

    public CustomDissagregationOptionWeb getCustomDissagregationOption() {
        return customDissagregationOption;
    }

    public void setCustomDissagregationOption(CustomDissagregationOptionWeb customDissagregationOption) {
        this.customDissagregationOption = customDissagregationOption;
    }

    public Boolean getShowValue() {
        return showValue;
    }

    public void setShowValue(Boolean showValue) {
        this.showValue = showValue;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getDenominatorValue() {
        return denominatorValue;
    }

    public void setDenominatorValue(BigDecimal denominatorValue) {
        this.denominatorValue = denominatorValue;
    }

    public BigDecimal getNumeratorValue() {
        return numeratorValue;
    }

    public void setNumeratorValue(BigDecimal numeratorValue) {
        this.numeratorValue = numeratorValue;
    }
}
