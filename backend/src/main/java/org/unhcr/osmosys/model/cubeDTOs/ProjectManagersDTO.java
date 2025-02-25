package org.unhcr.osmosys.model.cubeDTOs;

public class ProjectManagersDTO {

    private long id;
    private long project_id;
    private long user_id;
    private String user_name;
    private String user_username;

    public ProjectManagersDTO(long id, long project_id, long user_id, String user_name, String user_username) {
        this.id = id;
        this.project_id = project_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_username = user_username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProject_id() {
        return project_id;
    }

    public void setProject_id(long project_id) {
        this.project_id = project_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_username() {
        return user_username;
    }

    public void setUser_username(String user_username) {
        this.user_username = user_username;
    }
}
