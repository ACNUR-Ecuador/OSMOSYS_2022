package org.unhcr.osmosys.model.cubeDTOs;

public class TagsDTO {

    public TagsDTO(long tag_id, String tag_name, String tag_description, String tag_operation, long period_year) {
        this.tag_id = tag_id;
        this.tag_name = tag_name;
        this.tag_description = tag_description;
        this.tag_operation = tag_operation;
        this.period_year = period_year;
    }

    private long tag_id;
    private String tag_name;
    private String tag_description;
    private String tag_operation;
    private long period_year;

    public long getTag_id() {
        return tag_id;
    }

    public void setTag_id(long tag_id) {
        this.tag_id = tag_id;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public String getTag_description() {
        return tag_description;
    }

    public void setTag_description(String tag_description) {
        this.tag_description = tag_description;
    }

    public String getTag_operation() {
        return tag_operation;
    }

    public void setTag_operation(String tag_operation) {
        this.tag_operation = tag_operation;
    }

    public long getPeriod_year() {
        return period_year;
    }

    public void setPeriod_year(long period_year) {
        this.period_year = period_year;
    }
}
