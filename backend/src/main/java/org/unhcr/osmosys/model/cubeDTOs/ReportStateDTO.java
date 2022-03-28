package org.unhcr.osmosys.model.cubeDTOs;

public class ReportStateDTO {

    public ReportStateDTO(Long ie_id, String months_late, Boolean late) {
        this.ie_id = ie_id;
        this.months_late = months_late;
        this.late = late;
    }

    private Long ie_id;
    private String months_late;
    private Boolean late;

    public Long getIe_id() {
        return ie_id;
    }

    public void setIe_id(Long ie_id) {
        this.ie_id = ie_id;
    }

    public String getMonths_late() {
        return months_late;
    }

    public void setMonths_late(String months_late) {
        this.months_late = months_late;
    }

    public Boolean getLate() {
        return late;
    }

    public void setLate(Boolean late) {
        this.late = late;
    }
}
