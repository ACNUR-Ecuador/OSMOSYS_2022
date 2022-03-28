package org.unhcr.osmosys.model.cubeDTOs;

public class CountryOfOriginTypeDTO {

    public CountryOfOriginTypeDTO(String country_of_origin) {
        this.country_of_origin = country_of_origin;
    }

    private String country_of_origin;

    public String getCountry_of_origin() {
        return country_of_origin;
    }

    public void setCountry_of_origin(String country_of_origin) {
        this.country_of_origin = country_of_origin;
    }
}
