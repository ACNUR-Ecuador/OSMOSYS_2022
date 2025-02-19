package org.unhcr.osmosys.webServices.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagatechs.generics.security.model.User;
import com.sagatechs.generics.webservice.webModel.UserWeb;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.enums.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class IndicatorWeb extends BaseWebEntity implements Serializable {
    public IndicatorWeb() {
        super();
    }

    private String code;
    private String regionalCode;
    private String description;
    private String category;
    private String instructions;
    private String qualitativeInstructions;
    private IndicatorType indicatorType;
    private MeasureType measureType;
    private Frecuency frecuency;
    private AreaType areaType;
    @JsonProperty("isMonitored")
    private Boolean isMonitored;
    @JsonProperty("isCalculated")
    private Boolean isCalculated;
    private Boolean compassIndicator;
    private Boolean coreIndicator;
    private TotalIndicatorCalculationType totalIndicatorCalculationType;
    private StatementWeb statement;
    private UnitType unit;
    private List<DissagregationAssignationToIndicatorWeb> dissagregationsAssignationToIndicator = new ArrayList<>();
    private List<CustomDissagregationAssignationToIndicatorWeb> customDissagregationAssignationToIndicators = new ArrayList<>();
    private String periods;
    private Boolean blockAfterUpdate;
    private UserWeb resultManager;
    private QuarterReportCalculation quarterReportCalculation;
    private String aggregationRuleComment;

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

    public UserWeb getResultManager() {
        return resultManager;
    }

    public void setResultManager(UserWeb resultManager) {
        this.resultManager = resultManager;
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

    public String getRegionalCode() {
        return regionalCode;
    }

    public void setRegionalCode(String regionalCode) {
        this.regionalCode = regionalCode;
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

    public Boolean getCoreIndicator() {
        return coreIndicator;
    }

    public void setCoreIndicator(Boolean coreIndicator) {
        this.coreIndicator = coreIndicator;
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

    public String getPeriods() {
        return periods;
    }

    public void setPeriods(String periods) {
        this.periods = periods;
    }

    public QuarterReportCalculation getQuarterReportCalculation() {
        return quarterReportCalculation;
    }

    public void setQuarterReportCalculation(QuarterReportCalculation quarterReportCalculation) {
        this.quarterReportCalculation = quarterReportCalculation;
    }

    public String getAggregationRuleComment() {
        return aggregationRuleComment;
    }

    public void setAggregationRuleComment(String aggregationRuleComment) {
        this.aggregationRuleComment = aggregationRuleComment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof IndicatorWeb)) return false;

        IndicatorWeb that = (IndicatorWeb) o;

        return new EqualsBuilder().appendSuper(super.equals(o)).append(code, that.code).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(code).toHashCode();
    }
}
