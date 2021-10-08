package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.enums.AreaType;
import org.unhcr.osmosys.model.enums.Frecuency;
import org.unhcr.osmosys.model.enums.IndicatorType;
import org.unhcr.osmosys.model.enums.MeasureType;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "statement_id")
    private Statement statement;


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

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "indicators_markers", joinColumns = @JoinColumn(name = "indicator_id"), inverseJoinColumns = @JoinColumn(name = "marker_id"))
    private Set<Marker> markers = new HashSet<>();

    @Column(name = "is_monitored", nullable = false)
    private Boolean isMonitored;

    @Column(name = "is_calculated", nullable = false)
    private Boolean isCalculated;

    @OneToMany(mappedBy = "indicator", fetch = FetchType.LAZY)
    private Set<DissagregationAssignationToIndicator> dissagregationsAssignationToIndicator = new HashSet<>();

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
        this.markers.add(marker);
    }

    public void removeMarker(Marker marker){
        this.markers.remove(marker);
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

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
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

            dissagregationAssignationToIndicator.setState(State.INACTIVE);
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
                "id=" + id +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", statement=" + statement +
                ", guidePartners='" + guidePartners + '\'' +
                ", guideDirectImplementation='" + guideDirectImplementation + '\'' +
                ", state=" + state +
                ", indicatorType=" + indicatorType +
                ", measureType=" + measureType +
                ", frecuency=" + frecuency +
                ", areaType=" + areaType +
                ", isMonitored=" + isMonitored +
                ", isCalculated=" + isCalculated +
                '}';
    }
}
