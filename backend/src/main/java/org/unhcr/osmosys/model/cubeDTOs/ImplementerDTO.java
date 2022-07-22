package org.unhcr.osmosys.model.cubeDTOs;

public class ImplementerDTO {

    public ImplementerDTO(String implementer_id, String acronym, String name, String parent_acronym, String parent_name, String implementation_type) {
        this.implementer_id = implementer_id;
        this.acronym = acronym;
        this.name = name;
        this.parent_acronym = parent_acronym;
        this.parent_name = parent_name;
        this.implementation_type = implementation_type;
    }

    private String implementer_id;
    private String acronym;
    private String name;
    private String parent_acronym;
    private String parent_name;
    private String implementation_type;

    public String getImplementer_id() {
        return implementer_id;
    }

    public void setImplementer_id(String implementer_id) {
        this.implementer_id = implementer_id;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent_acronym() {
        return parent_acronym;
    }

    public void setParent_acronym(String parent_acronym) {
        this.parent_acronym = parent_acronym;
    }

    public String getParent_name() {
        return parent_name;
    }

    public void setParent_name(String parent_name) {
        this.parent_name = parent_name;
    }

    public String getImplementation_type() {
        return implementation_type;
    }

    public void setImplementation_type(String implementation_type) {
        this.implementation_type = implementation_type;
    }
}