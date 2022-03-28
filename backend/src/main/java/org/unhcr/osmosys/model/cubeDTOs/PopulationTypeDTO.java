package org.unhcr.osmosys.model.cubeDTOs;

public class PopulationTypeDTO {

    public PopulationTypeDTO(String population_type) {
        this.population_type = population_type;
    }

    private String population_type;

    public String getPopulation_type() {
        return population_type;
    }

    public void setPopulation_type(String population_type) {
        this.population_type = population_type;
    }
}
