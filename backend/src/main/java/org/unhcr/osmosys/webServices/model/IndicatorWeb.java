package org.unhcr.osmosys.webServices.model;

import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.CustomDissagregationAssignationToIndicator;
import org.unhcr.osmosys.model.DissagregationAssignationToIndicator;
import org.unhcr.osmosys.model.Marker;
import org.unhcr.osmosys.model.Statement;
import org.unhcr.osmosys.model.enums.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public class IndicatorWeb implements Serializable {
    private Long id;
    private String code;
    private String description;
    private Set<Statement> statements = new HashSet<>();
    private String guidePartners;
    private String guideDirectImplementation;
    private State state;
    private IndicatorType indicatorType;
    private MeasureType measureType;
    private Frecuency frecuency;
    private AreaType areaType;
    private Set<MarkerWeb> markers = new HashSet<>();//todo
    private Boolean isMonitored;
    private Boolean isCalculated;
    private TotalIndicatorCalculationType totalIndicatorCalculationType;
    private Set<DissagregationAssignationToIndicator> dissagregationsAssignationToIndicator = new HashSet<>();// todo
    private Set<CustomDissagregationAssignationToIndicator> customDissagregationAssignationToIndicators = new HashSet<>(); // todo


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

    public Set<Statement> getStatements() {
        return statements;
    }

    public void setStatements(Set<Statement> statements) {
        this.statements = statements;
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

    public Set<MarkerWeb> getMarkers() {
        return markers;
    }

    public void setMarkers(Set<MarkerWeb> markers) {
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

    public Set<DissagregationAssignationToIndicator> getDissagregationsAssignationToIndicator() {
        return dissagregationsAssignationToIndicator;
    }

    public void setDissagregationsAssignationToIndicator(Set<DissagregationAssignationToIndicator> dissagregationsAssignationToIndicator) {
        this.dissagregationsAssignationToIndicator = dissagregationsAssignationToIndicator;
    }

    public Set<CustomDissagregationAssignationToIndicator> getCustomDissagregationAssignationToIndicators() {
        return customDissagregationAssignationToIndicators;
    }

    public void setCustomDissagregationAssignationToIndicators(Set<CustomDissagregationAssignationToIndicator> customDissagregationAssignationToIndicators) {
        this.customDissagregationAssignationToIndicators = customDissagregationAssignationToIndicators;
    }

    public TotalIndicatorCalculationType getTotalIndicatorCalculationType() {
        return totalIndicatorCalculationType;
    }

    public void setTotalIndicatorCalculationType(TotalIndicatorCalculationType totalIndicatorCalculationType) {
        this.totalIndicatorCalculationType = totalIndicatorCalculationType;
    }
}
