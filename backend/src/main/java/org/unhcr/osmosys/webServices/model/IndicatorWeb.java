package org.unhcr.osmosys.webServices.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.Statement;
import org.unhcr.osmosys.model.enums.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class IndicatorWeb implements Serializable {
    private Long id;
    private String code;
    private String productCode;
    private String description;
    private String category;
    private State state;
    private IndicatorType indicatorType;
    private MeasureType measureType;
    private Frecuency frecuency;
    private AreaType areaType;
    private List<MarkerWeb> markers = new ArrayList<>();
    @JsonProperty("isMonitored")
    private Boolean isMonitored;
    @JsonProperty("isCalculated")
    private Boolean isCalculated;
    private Boolean compassIndicator;
    private TotalIndicatorCalculationType totalIndicatorCalculationType;

    private List<StatementWeb> statements = new ArrayList<>();
    private List<DissagregationAssignationToIndicatorWeb> dissagregationsAssignationToIndicator = new ArrayList<>();
    private List<CustomDissagregationAssignationToIndicatorWeb> customDissagregationAssignationToIndicators = new ArrayList<>();

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

    public List<StatementWeb> getStatements() {
        return statements;
    }

    public void setStatements(List<StatementWeb> statements) {
        this.statements = statements;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public List<MarkerWeb> getMarkers() {
        return markers;
    }

    public void setMarkers(List<MarkerWeb> markers) {
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

    public TotalIndicatorCalculationType getTotalIndicatorCalculationType() {
        return totalIndicatorCalculationType;
    }

    public void setTotalIndicatorCalculationType(TotalIndicatorCalculationType totalIndicatorCalculationType) {
        this.totalIndicatorCalculationType = totalIndicatorCalculationType;
    }

    public List<DissagregationAssignationToIndicatorWeb> getDissagregationsAssignationToIndicator() {
        return dissagregationsAssignationToIndicator;
    }

    public void setDissagregationsAssignationToIndicator(List<DissagregationAssignationToIndicatorWeb> dissagregationsAssignationToIndicator) {
        this.dissagregationsAssignationToIndicator = dissagregationsAssignationToIndicator;
    }

    public List<CustomDissagregationAssignationToIndicatorWeb> getCustomDissagregationAssignationToIndicators() {
        return customDissagregationAssignationToIndicators;
    }

    public void setCustomDissagregationAssignationToIndicators(List<CustomDissagregationAssignationToIndicatorWeb> customDissagregationAssignationToIndicators) {
        this.customDissagregationAssignationToIndicators = customDissagregationAssignationToIndicators;
    }

    public Boolean getCompassIndicator() {
        return compassIndicator;
    }

    public void setCompassIndicator(Boolean compassIndicator) {
        this.compassIndicator = compassIndicator;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
}
