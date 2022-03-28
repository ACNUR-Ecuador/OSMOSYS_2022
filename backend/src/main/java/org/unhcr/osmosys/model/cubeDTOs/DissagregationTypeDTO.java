package org.unhcr.osmosys.model.cubeDTOs;

public class DissagregationTypeDTO {

    public DissagregationTypeDTO(String dissagregation_type) {
        this.dissagregation_type = dissagregation_type;
    }

    private String dissagregation_type;

    public String getDissagregation_type() {
        return dissagregation_type;
    }

    public void setDissagregation_type(String dissagregation_type) {
        this.dissagregation_type = dissagregation_type;
    }
}
