package org.unhcr.osmosys.model.cubeDTOs;

public class GenderTypeDTO {

    public GenderTypeDTO(String gender_type) {
        this.gender_type = gender_type;
    }

    private String gender_type;

    public String getGender_type() {
        return gender_type;
    }

    public void setGender_type(String gender_type) {
        this.gender_type = gender_type;
    }
}
