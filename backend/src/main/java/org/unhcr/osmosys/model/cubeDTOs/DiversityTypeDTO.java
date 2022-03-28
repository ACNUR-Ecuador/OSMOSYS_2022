package org.unhcr.osmosys.model.cubeDTOs;

public class DiversityTypeDTO {

    public DiversityTypeDTO(String diversity_type) {
        this.diversity_type = diversity_type;
    }

    private String diversity_type;

    public String getDiversity_type() {
        return diversity_type;
    }

    public void setDiversity_type(String diversity_type) {
        this.diversity_type = diversity_type;
    }
}
