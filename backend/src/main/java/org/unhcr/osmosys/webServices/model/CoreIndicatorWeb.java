package org.unhcr.osmosys.webServices.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.enums.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class CoreIndicatorWeb extends BaseWebEntity implements Serializable {
    public CoreIndicatorWeb() {
        super();
    }

    private Long id;
    private String code;
    private String areaCode;
    private String description;
    private MeasureType measureType;
    private Frecuency frecuency;
    private String guiadance;
    private State state;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getGuiadance() {
        return guiadance;
    }

    public void setGuiadance(String guiadance) {
        this.guiadance = guiadance;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public void setState(State state) {
        this.state = state;
    }
}
