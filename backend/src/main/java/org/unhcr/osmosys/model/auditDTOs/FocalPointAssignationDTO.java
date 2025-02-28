package org.unhcr.osmosys.model.auditDTOs;

public class FocalPointAssignationDTO {
    private Long id;
    private String focalPoint;
    private String projectId;
    private String mainFocalPoint;
    private String state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFocalPoint() {
        return focalPoint;
    }

    public void setFocalPoint(String focalPoint) {
        this.focalPoint = focalPoint;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getMainFocalPoint() {
        return mainFocalPoint;
    }

    public void setMainFocalPoint(String mainFocalPoint) {
        this.mainFocalPoint = mainFocalPoint;
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
                ", \"Responsable del Proyecto\":\"" + focalPoint + "\"" +
                ", \"ID de Proyecto\":\"" + projectId + "\"" +
                ", \"Responsable Principal\":\"" + mainFocalPoint + "\"" +
                ", \"Estado\":\"" + state + "\"" +
                '}';
    }
}
