package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntityIdState;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.enums.DissagregationType;
import org.unhcr.osmosys.model.enums.MonthEnum;
import org.unhcr.osmosys.model.standardDissagregations.options.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(schema = "osmosys", name = "indicator_values")
public class IndicatorValue extends BaseEntityIdState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
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


    @Enumerated(EnumType.STRING)
    @Column(name = "dissagregation_type", nullable = false, length = 60)
    private DissagregationType dissagregationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "population_type_option_id", foreignKey = @ForeignKey(name = "fk_iv_pto"))
    private PopulationTypeDissagregationOption populationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_of_origin_option_id", foreignKey = @ForeignKey(name = "fk_iv_coo"))
    private CountryOfOriginDissagregationOption countryOfOrigin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gender_option_id", foreignKey = @ForeignKey(name = "fk_iv_go"))
    private GenderDissagregationOption genderType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "age_option_id", foreignKey = @ForeignKey(name = "fk_iv_ao"))
    private AgeDissagregationOption ageType;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diversity_option_id", foreignKey = @ForeignKey(name = "fk_iv_do"))
    private DiversityDissagregationOption diversityType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "canton_id", foreignKey = @ForeignKey(name = "fk_indicator_values_cantones"))
    private Canton location;

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
        if (monthEnum != null) {
            this.monthYearOrder = monthEnum.getOrder();
        } else {
            this.monthYearOrder = null;
        }
        this.monthEnum = monthEnum;
    }

    public DissagregationType getDissagregationType() {
        return dissagregationType;
    }

    public void setDissagregationType(DissagregationType dissagregationType) {
        this.dissagregationType = dissagregationType;
    }

    public PopulationTypeDissagregationOption getPopulationType() {
        return populationType;
    }

    public void setPopulationType(PopulationTypeDissagregationOption populationType) {
        this.populationType = populationType;
    }

    public CountryOfOriginDissagregationOption getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(CountryOfOriginDissagregationOption countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public GenderDissagregationOption getGenderType() {
        return genderType;
    }

    public void setGenderType(GenderDissagregationOption genderType) {
        this.genderType = genderType;
    }

    public AgeDissagregationOption getAgeType() {
        return ageType;
    }

    public void setAgeType(AgeDissagregationOption ageType) {
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

    public DiversityDissagregationOption getDiversityType() {
        return diversityType;
    }

    public void setDiversityType(DiversityDissagregationOption diversityType) {
        this.diversityType = diversityType;
    }

    public Canton getLocation() {
        return location;
    }

    public void setLocation(Canton location) {
        this.location = location;
    }

    public Integer getMonthYearOrder() {
        return monthYearOrder;
    }

    public void setMonthYearOrder(Integer monthYearOrder) {
        this.monthYearOrder = monthYearOrder;
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
                ", value=" + value +
                '}';
    }


    @Transient()
    private IndicatorValueOptionsDTO indicatorValueOptionsDTO = null;

    public IndicatorValueOptionsDTO getDissagregationDTO() {
        if (this.indicatorValueOptionsDTO == null) {
            this.indicatorValueOptionsDTO = new IndicatorValueOptionsDTO(this.populationType,this.countryOfOrigin,this.genderType, this.ageType, this.diversityType, this.location);
            this.indicatorValueOptionsDTO.setAgeType(ageType);
        }
        return this.indicatorValueOptionsDTO;
    }

    public void setBytDTO(IndicatorValueOptionsDTO indicatorValueOptionsDTO){
        this.populationType=indicatorValueOptionsDTO.getPopulationType();
        this.countryOfOrigin=indicatorValueOptionsDTO.getCountryOfOrigin();
        this.genderType=indicatorValueOptionsDTO.getGenderType();
        this.ageType=indicatorValueOptionsDTO.getAgeType();
        this.diversityType=indicatorValueOptionsDTO.getDiversityType();
        this.location=indicatorValueOptionsDTO.getLocation();
    }
}
