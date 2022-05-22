package org.unhcr.osmosys.model.cubeDTOs;

public class UserDTO {

    public UserDTO(Long id, String name, String organization, String office) {
        this.id = id;
        this.name = name;
        this.organization = organization;
        this.office = office;
    }

    private Long id;
    private String name;
    private String organization;
    private String office;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }
}
