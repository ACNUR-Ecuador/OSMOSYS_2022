package org.unhcr.osmosys.model.cubeDTOs;

public class ProjectDTO {

    public ProjectDTO(Long project_id, String name) {
        this.project_id = project_id;
        this.name = name;
    }

    private Long project_id;
    private String name;


    public Long getProject_id() {
        return project_id;
    }

    public void setProject_id(Long project_id) {
        this.project_id = project_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
