package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntityIdState;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.standardDissagregations.periodOptions.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "osmosys", name = "periods")
public class Period extends BaseEntityIdState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "year", unique = true, nullable = false)
    private Integer year;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "period")
    private GeneralIndicator generalIndicator;

    @OneToMany(mappedBy = "period", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, targetEntity = PeriodAgeDissagregationOption.class)
    private Set<PeriodAgeDissagregationOption> periodAgeDissagregationOptions = new HashSet<>();

    @OneToMany(mappedBy = "period", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, targetEntity = PeriodGenderDissagregationOption.class)
    private Set<PeriodGenderDissagregationOption> periodGenderDissagregationOptions = new HashSet<>();

    @OneToMany(mappedBy = "period", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, targetEntity = PeriodPopulationTypeDissagregationOption.class)
    private Set<PeriodPopulationTypeDissagregationOption> periodPopulationTypeDissagregationOptions = new HashSet<>();

    @OneToMany(mappedBy = "period", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, targetEntity = PeriodDiversityDissagregationOption.class)
    private Set<PeriodDiversityDissagregationOption> periodDiversityDissagregationOptions = new HashSet<>();

    @OneToMany(mappedBy = "period", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, targetEntity = PeriodCountryOfOriginDissagregationOption.class)
    private Set<PeriodCountryOfOriginDissagregationOption> periodCountryOfOriginDissagregationOptions = new HashSet<>();

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public GeneralIndicator getGeneralIndicator() {
        return generalIndicator;
    }

    public void setGeneralIndicator(GeneralIndicator generalIndicator) {
        this.generalIndicator = generalIndicator;
    }

    public Set<PeriodAgeDissagregationOption> getPeriodAgeDissagregationOptions() {
        return periodAgeDissagregationOptions;
    }

    public void setPeriodAgeDissagregationOptions(Set<PeriodAgeDissagregationOption> periodAgeDissagregationOptions) {
        this.periodAgeDissagregationOptions = periodAgeDissagregationOptions;
    }

    public void addPeriodAgeDissagregationOption(PeriodAgeDissagregationOption periodAgeDissagregationOption){
        periodAgeDissagregationOption.setPeriod(this);
        if(!this.periodAgeDissagregationOptions.add(periodAgeDissagregationOption)){
            this.periodAgeDissagregationOptions.remove(periodAgeDissagregationOption);
            this.periodAgeDissagregationOptions.add(periodAgeDissagregationOption);
        }
    }

    public Set<PeriodGenderDissagregationOption> getPeriodGenderDissagregationOptions() {
        return periodGenderDissagregationOptions;
    }

    public void setPeriodGenderDissagregationOptions(Set<PeriodGenderDissagregationOption> periodGenderDissagregationOptions) {
        this.periodGenderDissagregationOptions = periodGenderDissagregationOptions;
    }
    public void addPeriodGenderDissagregationOption(PeriodGenderDissagregationOption periodGenderDissagregationOption){
        periodGenderDissagregationOption.setPeriod(this);
        if(!this.periodGenderDissagregationOptions.add(periodGenderDissagregationOption)){
            this.periodGenderDissagregationOptions.remove(periodGenderDissagregationOption);
            this.periodGenderDissagregationOptions.add(periodGenderDissagregationOption);
        }
    }

    public Set<PeriodPopulationTypeDissagregationOption> getPeriodPopulationTypeDissagregationOptions() {
        return periodPopulationTypeDissagregationOptions;
    }

    public void setPeriodPopulationTypeDissagregationOptions(Set<PeriodPopulationTypeDissagregationOption> periodPopulationTypeDissagregationOptions) {
        this.periodPopulationTypeDissagregationOptions = periodPopulationTypeDissagregationOptions;
    }

    public void addPeriodPopulationTypeDissagregationOption(PeriodPopulationTypeDissagregationOption periodPopulationTypeDissagregationOption){
        periodPopulationTypeDissagregationOption.setPeriod(this);
        if(!this.periodPopulationTypeDissagregationOptions.add(periodPopulationTypeDissagregationOption)){
            this.periodPopulationTypeDissagregationOptions.remove(periodPopulationTypeDissagregationOption);
            this.periodPopulationTypeDissagregationOptions.add(periodPopulationTypeDissagregationOption);
        }
    }
    public Set<PeriodDiversityDissagregationOption> getPeriodDiversityDissagregationOptions() {
        return periodDiversityDissagregationOptions;
    }


    public void addPeriodDiversityDissagregationOption(PeriodDiversityDissagregationOption periodDiversityDissagregationOption){
        periodDiversityDissagregationOption.setPeriod(this);
        if(!this.periodDiversityDissagregationOptions.add(periodDiversityDissagregationOption)){
            this.periodDiversityDissagregationOptions.remove(periodDiversityDissagregationOption);
            this.periodDiversityDissagregationOptions.add(periodDiversityDissagregationOption);
        }
    }
    public void setPeriodDiversityDissagregationOptions(Set<PeriodDiversityDissagregationOption> periodDiversityDissagregationOptions) {
        this.periodDiversityDissagregationOptions = periodDiversityDissagregationOptions;
    }

    public Set<PeriodCountryOfOriginDissagregationOption> getPeriodCountryOfOriginDissagregationOptions() {
        return periodCountryOfOriginDissagregationOptions;
    }

    public void setPeriodCountryOfOriginDissagregationOptions(Set<PeriodCountryOfOriginDissagregationOption> periodCountryOfOriginDissagregationOptions) {
        this.periodCountryOfOriginDissagregationOptions = periodCountryOfOriginDissagregationOptions;
    }

    public void addPeriodCountryOfOriginDissagregationOption(PeriodCountryOfOriginDissagregationOption periodCountryOfOriginDissagregationOption){
        periodCountryOfOriginDissagregationOption.setPeriod(this);
        if(!this.periodCountryOfOriginDissagregationOptions.add(periodCountryOfOriginDissagregationOption)){
            this.periodCountryOfOriginDissagregationOptions.remove(periodCountryOfOriginDissagregationOption);
            this.periodCountryOfOriginDissagregationOptions.add(periodCountryOfOriginDissagregationOption);
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Period period = (Period) o;

        return new EqualsBuilder().append(id, period.id).append(year, period.year).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(year).toHashCode();
    }

    @Override
    public String toString() {
        return "Period{" +
                "id=" + id +
                ", year=" + year +
                ", state=" + state +
                '}';
    }
}