package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.enums.*;

import javax.persistence.*;


@Entity
@Table(schema = "osmosys", name = "dissagregation_filter_indicator_execution")
public class DissagregationFilterIndicatorExecution extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12)
    private State state;

    @Enumerated(EnumType.STRING)
    @Column(name = "dissagregation_type", nullable = false, length = 12)
    private DissagregationType dissagregationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "population_type", nullable = false, length = 12)
    private PopulationType populationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "country_of_origin", nullable = false, length = 12)
    private CountryOfOrigin countryOfOrigin;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender_type", nullable = false, length = 12)
    private GenderType genderType;

    @Enumerated(EnumType.STRING)
    @Column(name = "age_type", nullable = false, length = 12)
    private AgeType ageType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "canton_id",foreignKey = @ForeignKey(name = "fk_filter_canton"))
    private Canton canton;

    @ManyToOne(optional = false)
    @JoinColumn(name = "dissagregation_assignation_to_indicator_execution_id")
    private DissagregationAssignationToIndicatorExecution dissagregationAssignationToIndicatorExecution;

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

    public DissagregationAssignationToIndicatorExecution getDissagregationAssignationToIndicatorExecution() {
        return dissagregationAssignationToIndicatorExecution;
    }

    public void setDissagregationAssignationToIndicatorExecution(DissagregationAssignationToIndicatorExecution dissagregationAssignationToIndicatorExecution) {
        this.dissagregationAssignationToIndicatorExecution = dissagregationAssignationToIndicatorExecution;
    }

    public Canton getCanton() {
        return canton;
    }

    public void setCanton(Canton canton) {
        this.canton = canton;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DissagregationFilterIndicatorExecution that = (DissagregationFilterIndicatorExecution) o;

        return new EqualsBuilder().append(id, that.id).append(dissagregationType, that.dissagregationType).append(populationType, that.populationType).append(countryOfOrigin, that.countryOfOrigin).append(genderType, that.genderType).append(ageType, that.ageType).append(canton, that.canton).append(dissagregationAssignationToIndicatorExecution, that.dissagregationAssignationToIndicatorExecution).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(dissagregationType).append(populationType).append(countryOfOrigin).append(genderType).append(ageType).append(canton).append(dissagregationAssignationToIndicatorExecution).toHashCode();
    }
}
