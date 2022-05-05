package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.enums.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(schema = "osmosys", name = "indicator_values_custom_dissagregation")
public class IndicatorValueCustomDissagregation extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "month_id", foreignKey = @ForeignKey(name = "fk_value_month"))
    private Month month;


    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12)
    private State state;

    @Column(name = "month", nullable = false)
    @Enumerated(EnumType.STRING)
    private MonthEnum monthEnum;

    @Column(name = "month_year_order", nullable = false)
    private Integer monthYearOrder;


    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "custom_dissagregation_option", foreignKey = @ForeignKey(name = "fk_indicator_values_custom_dissagregation_option"))
    private CustomDissagregationOption customDissagregationOption;


    @Column(name = "value")
    private BigDecimal value;

    @Column(name = "denominator_value")
    private BigDecimal denominatorValue;

    @Column(name = "numerator_value")
    private BigDecimal numeratorValue;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
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

        if(monthEnum !=null){
            this.monthYearOrder=monthEnum.getOrder();
        } else {
            this.monthYearOrder = null;
        }
        this.monthEnum = monthEnum;
    }

    public CustomDissagregationOption getCustomDissagregationOption() {
        return customDissagregationOption;
    }

    public void setCustomDissagregationOption(CustomDissagregationOption customDissagregationOption) {
        this.customDissagregationOption = customDissagregationOption;
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

    public Integer getMonthYearOrder() {
        return monthYearOrder;
    }

    public void setMonthYearOrder(Integer monthYearOrder) {
        this.monthYearOrder = monthYearOrder;
    }
}
