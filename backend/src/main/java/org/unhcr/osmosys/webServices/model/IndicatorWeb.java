package org.unhcr.osmosys.webServices.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.enums.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class IndicatorWeb implements Serializable {
    private Long id;
    private String code;
    private String description;
    private String category;
    private String instructions;
    private String qualitativeInstructions;
    private State state;
    private IndicatorType indicatorType;
    private MeasureType measureType;
    private Frecuency frecuency;
    private AreaType areaType;
    @JsonProperty("isMonitored")
    private Boolean isMonitored;
    @JsonProperty("isCalculated")
    private Boolean isCalculated;
    private Boolean compassIndicator;
    private TotalIndicatorCalculationType totalIndicatorCalculationType;

    private StatementWeb statement;
    private UnitType unit;
    private List<DissagregationAssignationToIndicatorWeb> dissagregationsAssignationToIndicator = new ArrayList<>();
    private List<CustomDissagregationAssignationToIndicatorWeb> customDissagregationAssignationToIndicators = new ArrayList<>();

    private Boolean blockAfterUpdate;

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

    public StatementWeb getStatement() {
        return statement;
    }

    public void setStatement(StatementWeb statement) {
        this.statement = statement;
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

    public UnitType getUnit() {
        return unit;
    }

    public void setUnit(UnitType unit) {
        this.unit = unit;
    }

    public String getQualitativeInstructions() {
        return qualitativeInstructions;
    }

    public void setQualitativeInstructions(String qualitativeInstructions) {
        this.qualitativeInstructions = qualitativeInstructions;
    }

    public Boolean getBlockAfterUpdate() {
        return blockAfterUpdate;
    }

    public void setBlockAfterUpdate(Boolean blockAfterUpdate) {
        this.blockAfterUpdate = blockAfterUpdate;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof IndicatorWeb)) return false;

        IndicatorWeb that = (IndicatorWeb) o;

        return new EqualsBuilder().append(id, that.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).toHashCode();
    }
}
