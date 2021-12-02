package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.enums.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "osmosys", name = "indicators")
public class Indicator extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "description", nullable = false, unique = true)
    private String description;

    @ManyToMany@JoinTable(name = "statement_indicator_assignations", schema = "osmosys",
            joinColumns = {@JoinColumn(name = "indicator_id")},
            inverseJoinColumns = {@JoinColumn(name = "statement_id")}
    )
    private Set<Statement> statements = new HashSet<>();


    @Column(name = "guide_partners", columnDefinition = "text")
    private String guidePartners;

    @Column(name = "guide_direct_implementation", columnDefinition = "text")
    private String guideDirectImplementation;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12)
    private State state;

    @Column(name = "indicator_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private IndicatorType indicatorType;

    @Column(name = "measure_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MeasureType measureType;

    @Column(name = "frecuency", nullable = false)
    @Enumerated(EnumType.STRING)
    private Frecuency frecuency;

    @Column(name = "area_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AreaType areaType;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(schema ="osmosys" ,name = "indicators_markers", joinColumns = @JoinColumn(name = "indicator_id"), inverseJoinColumns = @JoinColumn(name = "marker_id"))
    private Set<Marker> markers = new HashSet<>();

    @Column(name = "is_monitored", nullable = false)
    private Boolean isMonitored;

    @Column(name = "is_calculated", nullable = false)
    private Boolean isCalculated;

    @Column(name = "total_indicator_calculation_type", nullable = false)
    private TotalIndicatorCalculationType totalIndicatorCalculationType;

    @OneToMany(mappedBy = "indicator", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<DissagregationAssignationToIndicator> dissagregationsAssignationToIndicator = new HashSet<>();

    @OneToMany(mappedBy = "indicator", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<CustomDissagregationAssignationToIndicator> customDissagregationAssignationToIndicators = new HashSet<>();

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGuidePartners() {
        return guidePartners;
    }

    public void setGuidePartners(String guidePartners) {
        this.guidePartners = guidePartners;
    }

    public String getGuideDirectImplementation() {
        return guideDirectImplementation;
    }

    public void setGuideDirectImplementation(String guideDirectImplementation) {
        this.guideDirectImplementation = guideDirectImplementation;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public IndicatorType getIndicatorType() {
        return indicatorType;
    }

    public void setIndicatorType(IndicatorType indicatorType) {
        this.indicatorType = indicatorType;
    }

    public MeasureType getMeasureType() {
        return measureType;
    }

    public void setMeasureType(MeasureType measureType) {
        this.measureType = measureType;
    }

    public Frecuency getFrecuency() {
        return frecuency;
    }

    public void setFrecuency(Frecuency frecuency) {
        this.frecuency = frecuency;
    }

    public AreaType getAreaType() {
        return areaType;
    }

    public void setAreaType(AreaType areaType) {
        this.areaType = areaType;
    }

    public void addMarker(Marker marker){
        if(!this.markers.add(marker)){
            this.markers.remove(marker);
            this.markers.add(marker);
        }
    }



    public Set<Marker> getMarkers() {
        return markers;
    }

    public void setMarkers(Set<Marker> markers) {
        this.markers = markers;
    }

    public Boolean getMonitored() {
        return isMonitored;
    }

    public void setMonitored(Boolean monitored) {
        isMonitored = monitored;
    }

    public Boolean getCalculated() {
        return isCalculated;
    }

    public void setCalculated(Boolean calculated) {
        isCalculated = calculated;
    }


    public Set<Statement> getStatements() {
        return statements;
    }

    public void setStatements(Set<Statement> statements) {
        this.statements = statements;
    }
    
    public void addStatement(Statement statement){
        statement.getIndicators().add(this);
        if (!this.statements.add(statement)) {
            this.statements.remove(statement);
            this.statements.add(statement);
        }
    }
    
    

    public Set<CustomDissagregationAssignationToIndicator> getCustomDissagregationAssignationToIndicators() {
        return customDissagregationAssignationToIndicators;
    }

    public void setCustomDissagregationAssignationToIndicators(Set<CustomDissagregationAssignationToIndicator> customDissagregationAssignationToIndicators) {
        this.customDissagregationAssignationToIndicators = customDissagregationAssignationToIndicators;
    }

    public void addCustomDissagregationAssignationToIndicator(CustomDissagregationAssignationToIndicator customDissagregationAssignationToIndicator) {
        customDissagregationAssignationToIndicator.setIndicator(this);
        if (!this.customDissagregationAssignationToIndicators.add(customDissagregationAssignationToIndicator)) {
            this.customDissagregationAssignationToIndicators.remove(customDissagregationAssignationToIndicator);
            this.customDissagregationAssignationToIndicators.add(customDissagregationAssignationToIndicator);
        }
    }
    public void addDissagregationAssignationToIndicator(DissagregationAssignationToIndicator dissagregationAssignationToIndicator) {
        dissagregationAssignationToIndicator.setIndicator(this);
        if (!this.dissagregationsAssignationToIndicator.add(dissagregationAssignationToIndicator)) {
            this.dissagregationsAssignationToIndicator.remove(dissagregationAssignationToIndicator);
            this.dissagregationsAssignationToIndicator.add(dissagregationAssignationToIndicator);
        }
    }

    public void removeDissagregationAssignationToIndicator(DissagregationAssignationToIndicator dissagregationAssignationToIndicator) {

        if (dissagregationAssignationToIndicator.getId() != null) {

            dissagregationAssignationToIndicator.setState(State.INACTIVO);
        } else {
            this.dissagregationsAssignationToIndicator.remove(dissagregationAssignationToIndicator);
        }
    }

    public Set<DissagregationAssignationToIndicator> getDissagregationsAssignationToIndicator() {
        return dissagregationsAssignationToIndicator;
    }

    public void setDissagregationsAssignationToIndicator(Set<DissagregationAssignationToIndicator> dissagregationsAssignationToIndicator) {
        this.dissagregationsAssignationToIndicator = dissagregationsAssignationToIndicator;
    }

    public TotalIndicatorCalculationType getTotalIndicatorCalculationType() {
        return totalIndicatorCalculationType;
    }

    public void setTotalIndicatorCalculationType(TotalIndicatorCalculationType totalIndicatorCalculationType) {
        this.totalIndicatorCalculationType = totalIndicatorCalculationType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Indicator indicator = (Indicator) o;

        return new EqualsBuilder().append(id, indicator.id).append(code, indicator.code).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(code).toHashCode();
    }

    @Override
    public String toString() {
        return "Indicator{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", statements=" + statements +
                ", guidePartners='" + guidePartners + '\'' +
                ", guideDirectImplementation='" + guideDirectImplementation + '\'' +
                ", state=" + state +
                ", indicatorType=" + indicatorType +
                ", measureType=" + measureType +
                ", frecuency=" + frecuency +
                ", areaType=" + areaType +
                ", markers=" + markers +
                ", isMonitored=" + isMonitored +
                ", isCalculated=" + isCalculated +
                '}';
    }
}
