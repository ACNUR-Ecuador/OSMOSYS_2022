package org.unhcr.osmosys.webServices.model;

import com.sagatechs.generics.persistence.model.State;

import java.io.Serializable;


public class PeriodStatementAsignationWeb implements Serializable {

    private Long id;
    private State state;
    private PeriodWeb period;
    private Long populationCoverage;


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

    public PeriodWeb getPeriod() {
        return period;
    }

    public void setPeriod(PeriodWeb period) {
        this.period = period;
    }

    public Long getPopulationCoverage() {
        return populationCoverage;
    }

    public void setPopulationCoverage(Long populationCoverage) {
        this.populationCoverage = populationCoverage;
    }
}
