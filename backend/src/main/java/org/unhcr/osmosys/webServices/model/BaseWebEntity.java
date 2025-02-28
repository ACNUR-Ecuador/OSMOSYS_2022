package org.unhcr.osmosys.webServices.model;

import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.List;
import java.util.StringJoiner;

public abstract class BaseWebEntity implements Serializable {

    public BaseWebEntity() {
    }

    public BaseWebEntity(Long id, State state) {
        this.id = id;
        this.state = state;
    }

    protected Long id;
    protected State state;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BaseWebEntity.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("state=" + state)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof BaseWebEntity)) return false;

        BaseWebEntity that = (BaseWebEntity) o;

        return new EqualsBuilder().append(id, that.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).toHashCode();
    }
}
