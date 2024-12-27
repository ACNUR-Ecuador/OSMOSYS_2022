package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntityIdState;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.model.User;
import org.unhcr.osmosys.model.enums.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "osmosys", name = "indicators")
public class Indicator extends BaseEntityIdState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;


    @Column(name = "regional_code", nullable = true, unique = true)
    private String regionalCode;


    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String description;

    @Column(name = "category")
    private String category;

    @Column(name = "instructions", columnDefinition = "text")
    private String instructions;

    @Column(name = "qualitative_instructions", columnDefinition = "text")
    private String qualitativeInstructions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statement_id", foreignKey = @ForeignKey(name = "fk_indicator_statement"))
    private Statement statement;

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



    @Column(name = "is_monitored", nullable = false)
    private Boolean isMonitored;

    @Column(name = "is_calculated", nullable = false)
    private Boolean isCalculated;

    @Column(name = "total_indicator_calculation_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TotalIndicatorCalculationType totalIndicatorCalculationType;

    @Column(name = "compass_indicator")
    private Boolean compassIndicator;

    @Column(name = "core_indicator")
    private Boolean coreIndicator;

    @Column(name = "unit")
    @Enumerated(EnumType.STRING)
    private UnitType unit;

    @Column(name = "block_after_update")
    private Boolean blockAfterUpdate;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "result_manager", foreignKey = @ForeignKey(name = "fk_indicator_result_manager"))
    private User resultManager;

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

    public User getResultManager() {
        return resultManager;
    }

    public void setResultManager(User resultManager) {
        this.resultManager = resultManager;
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

    public String getRegionalCode() {
        return regionalCode;
    }

    public void setRegionalCode(String regionalCode) {
        this.regionalCode = regionalCode;
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

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
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

    public Boolean getCompassIndicator() {
        return compassIndicator;
    }

    public void setCompassIndicator(Boolean compassIndicator) {
        this.compassIndicator = compassIndicator;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getQualitativeInstructions() {
        return qualitativeInstructions;
    }

    public void setQualitativeInstructions(String qualitativeInstructions) {
        this.qualitativeInstructions = qualitativeInstructions;
    }

    public UnitType getUnit() {
        return unit;
    }

    public void setUnit(UnitType unit) {
        this.unit = unit;
    }

    public Boolean getBlockAfterUpdate() {
        return blockAfterUpdate;
    }

    public void setBlockAfterUpdate(Boolean blockAfterUpdate) {
        this.blockAfterUpdate = blockAfterUpdate;
    }

    public Boolean getCoreIndicator() {
        return coreIndicator;
    }

    public void setCoreIndicator(Boolean coreIndicator) {
        this.coreIndicator = coreIndicator;
    }
}
