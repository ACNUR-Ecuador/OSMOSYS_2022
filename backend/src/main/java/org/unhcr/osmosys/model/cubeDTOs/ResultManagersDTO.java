package org.unhcr.osmosys.model.cubeDTOs;

public class ResultManagersDTO {

    private Long id;
    private Long indicator_id;
    private Long user_id;
    private Long population_type_id;
    private Long period_year;
    private Long period_id;
    private Long quarter;
    private Boolean  confirmed;
    private Long value;

    public ResultManagersDTO(Long id, Long indicator_id, Long user_id, Long population_type_id, Long period_year, Long period_id, Long quarter, Boolean confirmed, Long value) {
        this.id = id;
        this.indicator_id = indicator_id;
        this.user_id = user_id;
        this.population_type_id = population_type_id;
        this.period_year = period_year;
        this.period_id = period_id;
        this.quarter = quarter;
        this.confirmed = confirmed;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIndicator_id() {
        return indicator_id;
    }

    public void setIndicator_id(Long indicator_id) {
        this.indicator_id = indicator_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getPopulation_type_id() {
        return population_type_id;
    }

    public void setPopulation_type_id(Long population_type_id) {
        this.population_type_id = population_type_id;
    }

    public Long getPeriod_year() {
        return period_year;
    }

    public void setPeriod_year(Long period_year) {
        this.period_year = period_year;
    }

    public Long getPeriod_id() {
        return period_id;
    }

    public void setPeriod_id(Long period_id) {
        this.period_id = period_id;
    }

    public Long getQuarter() {
        return quarter;
    }

    public void setQuarter(Long quarter) {
        this.quarter = quarter;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
