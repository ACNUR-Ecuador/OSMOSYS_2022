package org.unhcr.osmosys.model;

import java.util.Objects;

public class JobStatus {
    private int progress;
    private String state; // Por ejemplo: "En progreso", "Completado", "Error", etc.
    private Object response;

    public JobStatus(JobStatus other) {
        this.progress = other.progress;
        this.state = other.state;
        this.response = other.response;
    }

    public JobStatus(int progress, String state) {
        this.progress = progress;
        this.state = state;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        JobStatus jobStatus = (JobStatus) o;
        return progress == jobStatus.progress && Objects.equals(state, jobStatus.state) && Objects.equals(response, jobStatus.response);
    }

    @Override
    public int hashCode() {
        return Objects.hash(progress, state, response);
    }
}