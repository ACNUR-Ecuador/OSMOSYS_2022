package org.unhcr.osmosys.model.standardDissagregations.options;

import com.sagatechs.generics.persistence.model.BaseEntityIdState;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING, length = 20)
@Table(schema = "dissagregations", name = "standard_dissagregation_options")
public abstract class StandardDissagregationOption extends BaseEntityIdState implements Serializable {

    public StandardDissagregationOption() {
    }

    public StandardDissagregationOption(String name, String groupName, Integer order, State state) {
        this.name = name;
        this.groupName = groupName;
        this.order = order;
        this.state = state;
    }

    public StandardDissagregationOption(Long id, String name, String groupName, Integer order, State state) {
        this.id = id;
        this.name = name;
        this.groupName = groupName;
        this.order = order;
        this.state = state;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", unique = true, nullable = false)
    protected Long id;


    @Column(name = "name",  nullable = false)
    protected String name;

    @Column(name = "group_name", nullable = false)
    protected String groupName;

    @Column(name = "region_group_name")
    protected String regionGroupName;

    @Column(name = "parent_dissagregation_id")
    protected Long parentDissagregationId;

    @Column(name = "order_", nullable = false)
    protected Integer order;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12)
    protected State state;


    @Override
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getRegionGroupName() {
        return regionGroupName;
    }

    public void setRegionGroupName(String regionGroupName) {
        this.regionGroupName = regionGroupName;
    }

    public Long getParentDissagregationId() {
        return parentDissagregationId;
    }

    public void setParentDissagregationId(Long parentDissagregationId) {
        this.parentDissagregationId = parentDissagregationId;
    }

    @Override
    public String toString() {
        return "StandardDissagregationOption{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", groupName='" + groupName + '\'' +
                ", order=" + order +
                ", state=" + state +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof StandardDissagregationOption)) return false;

        StandardDissagregationOption that = (StandardDissagregationOption) o;

        return new EqualsBuilder().append(id, that.id).append(name, that.name).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(name).toHashCode();
    }
}
