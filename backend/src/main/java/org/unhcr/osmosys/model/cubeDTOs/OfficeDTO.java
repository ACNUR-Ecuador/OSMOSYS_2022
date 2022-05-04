package org.unhcr.osmosys.model.cubeDTOs;

public class OfficeDTO {

    public OfficeDTO(Long office_id, String acronym, String description, Long parent_office_id, String parent_acronym, String parent_description) {
        this.office_id = office_id;
        this.acronym = acronym;
        this.description = description;
        this.parent_office_id = parent_office_id;
        this.parent_acronym = parent_acronym;
        this.parent_description = parent_description;
    }

    private Long office_id;
    private String acronym;
    private String description;
    private Long parent_office_id;
    private String parent_acronym;
    private String parent_description;

    public Long getOffice_id() {
        return office_id;
    }

    public void setOffice_id(Long office_id) {
        this.office_id = office_id;
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

    public Long getParent_office_id() {
        return parent_office_id;
    }

    public void setParent_office_id(Long parent_office_id) {
        this.parent_office_id = parent_office_id;
    }

    public String getParent_acronym() {
        return parent_acronym;
    }

    public void setParent_acronym(String parent_acronym) {
        this.parent_acronym = parent_acronym;
    }

    public String getParent_description() {
        return parent_description;
    }

    public void setParent_description(String parent_description) {
        this.parent_description = parent_description;
    }
}
