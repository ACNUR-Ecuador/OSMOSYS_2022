package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntityIdState;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.enums.AreaType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(schema = "osmosys", name = "areas")
public class Area  extends BaseEntityIdState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12)
    private State state;

    @Enumerated(EnumType.STRING)
    @Column(name = "area_type", nullable = false, length = 12)
    private AreaType areaType;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "short_description", unique = true, nullable = false)
    private String shortDescription;

    @Column(name = "description", columnDefinition = "text", unique = true)
    private String description;

    @Column(name = "definition", columnDefinition = "text")
    private String definition;

    @OneToMany(mappedBy = "area", fetch = FetchType.LAZY)
    private Set<Statement> statements = new HashSet<>();

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

    public AreaType getAreaType() {
        return areaType;
    }

    public void setAreaType(AreaType areaType) {
        this.areaType = areaType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }


    public void addStatement(Statement statement) {
        statement.setArea(this);
        statement.setAreaType(this.areaType);
        if (!this.statements.add(statement)) {
            this.statements.remove(statement);
            this.statements.add(statement);
        }
    }

    public void removeStatement(Statement statement) {
        if (statement.getId() != null) {
            statement.setState(State.INACTIVO);
        } else {
            this.statements.remove(statement);
        }
    }

    public Set<Statement> getStatements() {
        return statements;
    }

    public void setStatements(Set<Statement> statement) {
        this.statements = statement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Area area = (Area) o;

        return new EqualsBuilder().append(id, area.id).append(areaType, area.areaType).append(code, area.code).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(areaType).append(code).toHashCode();
    }

    @Override
    public String toString() {
        return "Area{" +
                "id=" + id +
                ", state=" + state +
                ", areaType=" + areaType +
                ", code='" + code + '\'' +
                '}';
    }
}
