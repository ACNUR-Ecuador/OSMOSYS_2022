package org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class StandardDissagregationOption extends BaseEntity<Long> {

    public StandardDissagregationOption() {
    }

    public StandardDissagregationOption(String name, String groupName, Integer order, State state) {
        this.name = name;
        this.groupName = groupName;
        this.order = order;
        this.state = state;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;


    @Column(name = "name",  nullable = false)
    private String name;

    @Column(name = "group_name", nullable = false)
    private String groupName;

    @Column(name = "order_", nullable = false)
    private Integer order;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12)
    private State state;


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

        return new EqualsBuilder().append(id, that.id).append(name, that.name).append(groupName, that.groupName).append(order, that.order).append(state, that.state).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(name).append(groupName).append(order).append(state).toHashCode();
    }
}
