package org.unhcr.osmosys.webServices.model;

import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.enums.AreaType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class StatementWeb implements Serializable {

    private Long id;
    private State state;
    private AreaType areaType;
    private String code;
    private String description;
    private StatementWeb parentStatement;
    private AreaWeb area;
    private PillarWeb pillar;
    private SituationWeb situation;
    private List<PeriodStatementAsignationWeb> periodStatementAsignations = new ArrayList<>();


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
}
