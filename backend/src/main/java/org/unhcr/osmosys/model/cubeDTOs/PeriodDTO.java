package org.unhcr.osmosys.model.cubeDTOs;

public class PeriodDTO {

    public PeriodDTO(Long id, Integer year) {
        this.id = id;
        this.year = year;
    }

    private Long id;
    private Integer year;

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
}
