package org.unhcr.osmosys.model.auditDTOs;

public class ProjectLocationAssigmentsDTO {
    private Long id;
    private String cantonId;
    private String projectId;
    private String state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCantonId() {
        return cantonId;
    }

    public void setCantonId(String cantonId) {
        this.cantonId = cantonId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    @Override
    public String toString() {
        return "{" +
                "\"ID\":" + id +
                ", \"Cantón\":\"" + cantonId + "\"" +
                ", \"ID de Proyecto\":\"" + projectId + "\"" +
                ", \"Estado\":\"" + state + "\"" +
                '}';
    }
}
