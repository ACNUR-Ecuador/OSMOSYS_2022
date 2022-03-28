package org.unhcr.osmosys.model.cubeDTOs;

public class OrganizationDTO {

    public OrganizationDTO(Long organization_id, String acronym, String description) {
        this.organization_id = organization_id;
        this.acronym = acronym;
        this.description = description;
    }

    private Long organization_id;
    private String acronym;
    private String description;

    public Long getOrganization_id() {
        return organization_id;
    }

    public void setOrganization_id(Long organization_id) {
        this.organization_id = organization_id;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
