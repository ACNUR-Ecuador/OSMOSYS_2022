package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntityIdState;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.enums.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(schema = "osmosys", name = "core_indicators")
public class CoreIndicator extends BaseEntityIdState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;


    @Column(name = "area_code", nullable = true)
    private String areaCode;

    @Column(name = "description", nullable = true, columnDefinition = "text")
    private String description;

    @Column(name = "measure_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MeasureType measureType;

    @Column(name = "frecuency", nullable = false)
    @Enumerated(EnumType.STRING)
    private Frecuency frecuency;


    @Column(name = "guiadance", nullable = true)
    private String guiadance;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12)
    private State state;

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
    public Long getId() {
        return id;
    }

    @Override
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, code, areaCode, description, measureType, frecuency, guiadance, state);
    }
}
