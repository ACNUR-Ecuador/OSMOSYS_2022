package org.unhcr.osmosys.model.cubeDTOs;

public class CustomDissagregationDTO {


    public CustomDissagregationDTO() {
    }

    public CustomDissagregationDTO(Long dissagregation_id, Boolean dissagregation_control, String dissagregation_description, String dissagregation_name, String dissagregation_state, Long option_id, String option_description, String option_name, String option_state) {
        this.dissagregation_id = dissagregation_id;
        this.dissagregation_control = dissagregation_control;
        this.dissagregation_description = dissagregation_description;
        this.dissagregation_name = dissagregation_name;
        this.dissagregation_state = dissagregation_state;
        this.option_id = option_id;
        this.option_description = option_description;
        this.option_name = option_name;
        this.option_state = option_state;
    }

    private Long dissagregation_id;
    private Boolean dissagregation_control;
    private String dissagregation_description;
    private String dissagregation_name;
    private String dissagregation_state;
    private Long option_id;
    private String option_description;
    private String option_name;
    private String option_state;

    public Long getDissagregation_id() {
        return dissagregation_id;
    }

    public void setDissagregation_id(Long dissagregation_id) {
        this.dissagregation_id = dissagregation_id;
    }

    public Boolean getDissagregation_control() {
        return dissagregation_control;
    }

    public void setDissagregation_control(Boolean dissagregation_control) {
        this.dissagregation_control = dissagregation_control;
    }

    public String getDissagregation_description() {
        return dissagregation_description;
    }

    public void setDissagregation_description(String dissagregation_description) {
        this.dissagregation_description = dissagregation_description;
    }

    public String getDissagregation_name() {
        return dissagregation_name;
    }

    public void setDissagregation_name(String dissagregation_name) {
        this.dissagregation_name = dissagregation_name;
    }

    public String getDissagregation_state() {
        return dissagregation_state;
    }

    public void setDissagregation_state(String dissagregation_state) {
        this.dissagregation_state = dissagregation_state;
    }

    public Long getOption_id() {
        return option_id;
    }

    public void setOption_id(Long option_id) {
        this.option_id = option_id;
    }

    public String getOption_description() {
        return option_description;
    }

    public void setOption_description(String option_description) {
        this.option_description = option_description;
    }

    public String getOption_name() {
        return option_name;
    }

    public void setOption_name(String option_name) {
        this.option_name = option_name;
    }

    public String getOption_state() {
        return option_state;
    }

    public void setOption_state(String option_state) {
        this.option_state = option_state;
    }
}
