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
    @JoinColumn(name = "indicator_execution_id", foreignKey = @ForeignKey(name = "fk_value_indicator_execution"))
    private IndicatorExecution indicatorExecution;

    @Column(name = "commentary", columnDefinition = "text")
    private String commentary;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12, unique = false)
    private State state;

    @Column(name = "month", nullable = false)
    @Enumerated(EnumType.STRING)
    private Month month;

    @Enumerated(EnumType.STRING)
    @Column(name = "dissagregation_type", nullable = false, length = 12, unique = false)
    private DissagregationType dissagregationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "population_type", nullable = false, length = 12, unique = false)
    private PopulationType populationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "country_of_origin", nullable = false, length = 12, unique = false)
    private CountryOfOrigin countryOfOrigin;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender_type", nullable = false, length = 12, unique = false)
    private GenderType genderType;

    @Enumerated(EnumType.STRING)
    @Column(name = "age_type", nullable = false, length = 12, unique = false)
    private AgeType ageType;

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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public IndicatorExecution getIndicatorExecution() {
        return indicatorExecution;
    }

    public void setIndicatorExecution(IndicatorExecution indicatorExecution) {
        this.indicatorExecution = indicatorExecution;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        IndicatorValue that = (IndicatorValue) o;

        return new EqualsBuilder().append(id, that.id).append(indicatorExecution, that.indicatorExecution).append(month, that.month).append(dissagregationType, that.dissagregationType).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(indicatorExecution).append(month).append(dissagregationType).toHashCode();
    }

    @Override
    public String toString() {
        return "IndicatorValue{" +
                "id=" + id +
                ", indicatorExecution=" + indicatorExecution +
                ", commentary='" + commentary + '\'' +
                ", state=" + state +
                ", month=" + month +
                ", dissagregationType=" + dissagregationType +
                ", populationType=" + populationType +
                ", countryOfOrigin=" + countryOfOrigin +
                ", genderType=" + genderType +
                ", ageType=" + ageType +
                ", value=" + value +
                ", denominatorValue=" + denominatorValue +
                ", numeratorValue=" + numeratorValue +
                '}';
    }
}
