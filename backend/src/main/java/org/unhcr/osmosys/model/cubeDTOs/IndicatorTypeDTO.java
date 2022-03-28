package org.unhcr.osmosys.model.cubeDTOs;

public class IndicatorTypeDTO {

    public IndicatorTypeDTO(String indicator_type) {
        this.indicator_type = indicator_type;
    }

    private String indicator_type;

    public String getIndicator_type() {
        return indicator_type;
    }

    public void setIndicator_type(String indicator_type) {
        this.indicator_type = indicator_type;
    }
}
