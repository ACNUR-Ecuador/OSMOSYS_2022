package org.unhcr.osmosys.model.cubeDTOs;

public class AgePrimaryEducationTypeDTO {
    public AgePrimaryEducationTypeDTO(String age_primary_education_type) {
        this.age_primary_education_type = age_primary_education_type;
    }

    private String age_primary_education_type;

    public String getAge_primary_education_type() {
        return age_primary_education_type;
    }

    public void setAge_primary_education_type(String age_primary_education_type) {
        this.age_primary_education_type = age_primary_education_type;
    }
}
