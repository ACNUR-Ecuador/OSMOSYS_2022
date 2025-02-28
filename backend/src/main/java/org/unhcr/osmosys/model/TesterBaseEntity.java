package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntityIdState;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.util.StringJoiner;


@Entity
@Table(schema = "tmp", name = "TesterBaseEntity")
public class TesterBaseEntity extends BaseEntityIdState {

    public TesterBaseEntity() {
    }

    public TesterBaseEntity( State state, String code) {

        this.state = state;
        this.code = code;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12)
    private State state;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Override
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TesterBaseEntity.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("state=" + state)
                .add("code='" + code + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof TesterBaseEntity)) return false;

        TesterBaseEntity that = (TesterBaseEntity) o;

        return new EqualsBuilder().append(id, that.id).append(code, that.code).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(code).toHashCode();
    }
}
