package org.unhcr.osmosys.model.cubeDTOs;

public class AgeTypeDTO {

    public AgeTypeDTO(String age_type) {
        this.age_type = age_type;
    }

    private String age_type;

    public String getAge_type() {
        return age_type;
    }

    public void setAge_type(String age_type) {
        this.age_type = age_type;
    }
}
