package org.unhcr.osmosys.model.cubeDTOs;

import org.unhcr.osmosys.model.enums.DissagregationType;

public class IndicatorMainDissagregationDTO {

    public IndicatorMainDissagregationDTO() {
    }

    public IndicatorMainDissagregationDTO(Long indicator_id, Long period_id, String dissagregation_type) {
        this.indicator_id = indicator_id;
        this.period_id = period_id;
        this.dissagregation_type = DissagregationType.valueOf(dissagregation_type);
    }

    private Long indicator_id;
    private Long period_id;
    private DissagregationType dissagregation_type;
    private Boolean mainDissagregation;


    public Long getIndicator_id() {
        return indicator_id;
    }

    public void setIndicator_id(Long indicator_id) {
        this.indicator_id = indicator_id;
    }

    public Long getPeriod_id() {
        return period_id;
    }

    public void setPeriod_id(Long period_id) {
        this.period_id = period_id;
    }

    public DissagregationType getDissagregation_type() {
        return dissagregation_type;
    }

    public void setDissagregation_type(DissagregationType dissagregation_type) {
        this.dissagregation_type = dissagregation_type;
    }

    public Boolean getMainDissagregation() {
        return mainDissagregation;
    }

    public void setMainDissagregation(Boolean mainDissagregation) {
        this.mainDissagregation = mainDissagregation;
    }
}
