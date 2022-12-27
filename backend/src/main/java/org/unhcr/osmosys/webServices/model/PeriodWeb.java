package org.unhcr.osmosys.webServices.model;

import com.sagatechs.generics.persistence.model.State;

import java.io.Serializable;
import java.util.Objects;

public class PeriodWeb implements Serializable {
    private Long id;
    private Integer year;
    private State state;
    private GeneralIndicatorWeb generalIndicator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public GeneralIndicatorWeb getGeneralIndicator() {
        return generalIndicator;
    }

    public void setGeneralIndicator(GeneralIndicatorWeb generalIndicator) {
        this.generalIndicator = generalIndicator;
    }

    @Override
    public String toString() {
        return "PeriodWeb{" +
                "id=" + id +
                ", year=" + year +
                ", state=" + state +
                ", generalIndicator=" + generalIndicator +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PeriodWeb)) return false;
        PeriodWeb periodWeb = (PeriodWeb) o;
        return year.equals(periodWeb.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year);
    }
}
