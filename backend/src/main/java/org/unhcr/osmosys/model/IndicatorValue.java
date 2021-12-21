package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.enums.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(schema = "osmosys", name = "indicator_values")
public class IndicatorValue extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;



    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "month_id", foreignKey = @ForeignKey(name = "fk_value_month"))
    private Month month;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12, unique = false)
    private State state;

    @Column(name = "month", nullable = false)
    @Enumerated(EnumType.STRING)
    private MonthEnum monthEnum;


    @Enumerated(EnumType.STRING)
    @Column(name = "dissagregation_type", nullable = false, length = 50, unique = false)
    private DissagregationType dissagregationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "population_type", nullable = true, length = 50, unique = false)
    private PopulationType populationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "country_of_origin", nullable = true, length = 50, unique = false)
    private CountryOfOrigin countryOfOrigin;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender_type", nullable = true, length = 50, unique = false)
    private GenderType genderType;

    @Enumerated(EnumType.STRING)
    @Column(name = "age_type", nullable = true, length = 50, unique = false)
    private AgeType ageType;

    @Enumerated(EnumType.STRING)
    @Column(name = "diversity_type", nullable = true, length = 50, unique = false)
    private DiversityType diversityType;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "canton_id", foreignKey = @ForeignKey(name = "fk_indicator_values_cantones"))
    private Canton location;

    @Column(name = "show_value", nullable = false)
    private Boolean showValue;

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
        this.monthEnum = monthEnum;
    }

    public DissagregationType getDissagregationType() {
        return dissagregationType;
    }

    public void setDissagregationType(DissagregationType dissagregationType) {
        this.dissagregationType = dissagregationType;
    }

    public PopulationType getPopulationType() {
        return populationType;
    }

    public void setPopulationType(PopulationType populationType) {
        this.populationType = populationType;
    }

    public CountryOfOrigin getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(CountryOfOrigin countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public GenderType getGenderType() {
        return genderType;
    }

    public void setGenderType(GenderType genderType) {
        this.genderType = genderType;
    }

    public AgeType getAgeType() {
        return ageType;
    }

    public void setAgeType(AgeType ageType) {
        this.ageType = ageType;
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

    public Boolean getShowValue() {
        return showValue;
    }

    public void setShowValue(Boolean showValue) {
        this.showValue = showValue;
    }

    public DiversityType getDiversityType() {
        return diversityType;
    }

    public void setDiversityType(DiversityType diversityType) {
        this.diversityType = diversityType;
    }

    public Canton getLocation() {
        return location;
    }

    public void setLocation(Canton location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "IndicatorValue{" +
                "state=" + state +
                ", monthEnum=" + monthEnum +
                ", dissagregationType=" + dissagregationType +
                ", populationType=" + populationType +
                ", countryOfOrigin=" + countryOfOrigin +
                ", genderType=" + genderType +
                ", ageType=" + ageType +
                ", diversityType=" + diversityType +
                ", location=" + location +
                ", showValue=" + showValue +
                ", value=" + value +
                '}';
    }
}
