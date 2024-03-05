package org.unhcr.osmosys.model.cubeDTOs;

public class StandardDissagregationOptionDTO {

    public StandardDissagregationOptionDTO() {
    }

    public StandardDissagregationOptionDTO(Long id, String group_name, String name, Long order_, String state, String age_range) {
        this.id = id;
        this.group_name = group_name;
        this.name = name;
        this.order_ = order_;
        this.state = state;
        this.age_range = age_range;
    }

    public Long id;
    public String group_name;
    public String name;
    public Long order_;
    public String state;
    public String age_range;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOrder_() {
        return order_;
    }

    public void setOrder_(Long order_) {
        this.order_ = order_;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAge_range() {
        return age_range;
    }

    public void setAge_range(String age_range) {
        this.age_range = age_range;
    }
}
