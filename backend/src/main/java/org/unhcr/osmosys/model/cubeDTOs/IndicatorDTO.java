package org.unhcr.osmosys.model.cubeDTOs;

public class IndicatorDTO {

    public IndicatorDTO(Long indicator_id, String code, String description, String category, String frecuency, String unit) {
        this.indicator_id = indicator_id;
        this.code = code;
        this.description = description;
        this.category = category;
        this.frecuency = frecuency;
        this.unit = unit;
    }

    private Long indicator_id;
    private String code;
    private String description;
    private String category;
    private String frecuency;
    private String unit;

    public Long getIndicator_id() {
        return indicator_id;
    }

    public void setIndicator_id(Long indicator_id) {
        this.indicator_id = indicator_id;
    }

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFrecuency() {
        return frecuency;
    }

    public void setFrecuency(String frecuency) {
        this.frecuency = frecuency;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
