package org.unhcr.osmosys.webServices.model.standardDissagregations;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.webServices.model.BaseWebEntity;

import java.io.Serializable;


public class StandardDissagregationOptionWeb extends BaseWebEntity implements Serializable {

    public StandardDissagregationOptionWeb() {
        super();
    }

    protected String name;
    protected Integer order;
    protected String groupName;
    protected String regionGroupName;
    protected String otherGroupName;

    public String getRegionGroupName() {
        return regionGroupName;
    }

    public void setRegionGroupName(String regionGroupName) {
        this.regionGroupName = regionGroupName;
    }

    public String getOtherGroupName() {
        return otherGroupName;
    }

    public void setOtherGroupName(String otherGroupName) {
        this.otherGroupName = otherGroupName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof StandardDissagregationOptionWeb)) return false;

        StandardDissagregationOptionWeb that = (StandardDissagregationOptionWeb) o;

        return new EqualsBuilder().append(id, that.id).append(name, that.name).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(name).toHashCode();
    }
}
