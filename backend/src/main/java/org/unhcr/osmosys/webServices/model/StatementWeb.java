package org.unhcr.osmosys.webServices.model;

import org.unhcr.osmosys.model.enums.AreaType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class StatementWeb extends BaseWebEntity implements Serializable {

    public StatementWeb() {
        super();
    }

    private AreaType areaType;
    private String code;
    private String productCode;
    private String description;
    private StatementWeb parentStatement;
    private AreaWeb area;
    private PillarWeb pillar;
    private SituationWeb situation;
    private List<PeriodStatementAsignationWeb> periodStatementAsignations = new ArrayList<>();


    public AreaType getAreaType() {
        return areaType;
    }

    public void setAreaType(AreaType areaType) {
        this.areaType = areaType;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatementWeb getParentStatement() {
        return parentStatement;
    }

    public void setParentStatement(StatementWeb parentStatement) {
        this.parentStatement = parentStatement;
    }

    public AreaWeb getArea() {
        return area;
    }

    public void setArea(AreaWeb area) {
        this.area = area;
    }

    public PillarWeb getPillar() {
        return pillar;
    }

    public void setPillar(PillarWeb pillar) {
        this.pillar = pillar;
    }

    public SituationWeb getSituation() {
        return situation;
    }

    public void setSituation(SituationWeb situation) {
        this.situation = situation;
    }

    public List<PeriodStatementAsignationWeb> getPeriodStatementAsignations() {
        return periodStatementAsignations;
    }

    public void setPeriodStatementAsignations(List<PeriodStatementAsignationWeb> periodStatementAsignations) {
        this.periodStatementAsignations = periodStatementAsignations;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    @Override
    public String toString() {
        return "StatementWeb{" +
                "id=" + id +
                ", state=" + state +
                ", areaType=" + areaType +
                ", code='" + code + '\'' +
                ", productCode='" + productCode + '\'' +
                ", description='" + description + '\'' +
                ", parentStatement=" + parentStatement +
                ", area=" + area +
                ", pillar=" + pillar +
                ", situation=" + situation +
                ", periodStatementAsignations=" + periodStatementAsignations +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatementWeb)) return false;
        StatementWeb that = (StatementWeb) o;
        return Objects.equals(id, that.id) && areaType == that.areaType && Objects.equals(code, that.code) && Objects.equals(description, that.description) && Objects.equals(periodStatementAsignations, that.periodStatementAsignations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, areaType, code, description, periodStatementAsignations);
    }
}
