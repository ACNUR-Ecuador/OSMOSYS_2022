package org.unhcr.osmosys.model.cubeDTOs;

import org.unhcr.osmosys.model.enums.DissagregationType;

public class IndicatorMainDissagregationDTO {

    public IndicatorMainDissagregationDTO() {
    }

    public IndicatorMainDissagregationDTO(Long indicator_id, Long period_id, String dissagregation_type) {
        this.indicator_id = indicator_id;
        this.period_id = period_id;
        this.dissagregation_type = DissagregationType.valueOf(dissagregation_type);
        this.order = null;
        this.indicatorType = null;

    }

    public IndicatorMainDissagregationDTO(Long indicator_id, Long period_id, String indicatorlabel,String indicatorType) {
        this.indicator_id = indicator_id;
        this.period_id = period_id;
        this.dissagregation_type = null;
        this.order = null;
        this.indicatorlabel = indicatorlabel;
        this.indicatorType = indicatorType;

    }

    private Long indicator_id;
    private Long period_id;
    private DissagregationType dissagregation_type;
    private Boolean mainDissagregation;

    private Integer order;
    private String indicatorType;
    private String indicatorlabel;


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

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getIndicatorType() {
        return indicatorType;
    }

    public void setIndicatorType(String indicatorType) {
        this.indicatorType = indicatorType;
    }

    public String getIndicatorlabel() {
        return indicatorlabel;
    }

    public void setIndicatorlabel(String indicatorlabel) {
        this.indicatorlabel = indicatorlabel;
    }
}
