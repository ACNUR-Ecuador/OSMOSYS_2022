package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.enums.AreaType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(schema = "osmosys", name = "statements")
public class Statement extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12, unique = false)
    private State state;

    @Enumerated(EnumType.STRING)
    @Column(name = "area_type", nullable = false, length = 12, unique = false)
    private AreaType areaType;

    @Column(name = "short_description", unique = true)
    private String shortDescription;


    @Column(name = "description", columnDefinition = "text", unique = true)
    private String description;


    // en ia
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_statement_id", foreignKey = @ForeignKey(name = "fk_statemet_statement_parent"))
    private Statement parentStatement;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "area_id", foreignKey = @ForeignKey(name = "fk_statement_area"))
    private Area area;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pillar_id", foreignKey = @ForeignKey(name = "fk_statemet_pillar"))
    private Pillar pillar;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "situation_id", foreignKey = @ForeignKey(name = "fk_statemet_situation"))
    private Situation situation;

    @OneToMany(mappedBy = "statement", fetch = FetchType.LAZY)
    private Set<PeriodStatementAsignation> periodStatementAsignations = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "statement_indicator_assignations",
            joinColumns = {@JoinColumn(name = "statement_id")},
            inverseJoinColumns = { @JoinColumn(name = "indicator_id")}
    )
    private Set<Indicator> indicators = new HashSet<>();

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

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Statement getParentStatement() {
        return parentStatement;
    }

    public void setParentStatement(Statement parentStatement) {
        this.parentStatement = parentStatement;
    }

    public Pillar getPillar() {
        return pillar;
    }

    public void setPillar(Pillar pillar) {
        this.pillar = pillar;
    }

    public Situation getSituation() {
        return situation;
    }

    public void setSituation(Situation situation) {
        this.situation = situation;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }


    public Set<Indicator> getIndicators() {
        return indicators;
    }

    public void setIndicators(Set<Indicator> indicators) {
        this.indicators = indicators;
    }


    public void addPeriodStatementAsignation(Period period) {
        PeriodStatementAsignation periodStatementAsignation = new PeriodStatementAsignation();
        periodStatementAsignation.setState(State.ACTIVO);
        periodStatementAsignation.setPeriod(period);
        periodStatementAsignation.setStatement(this);
        if (!this.periodStatementAsignations.add(periodStatementAsignation)) {
            this.periodStatementAsignations.remove(periodStatementAsignation);
            this.periodStatementAsignations.add(periodStatementAsignation);
        }
    }

    public Set<PeriodStatementAsignation> getPeriodStatementAsignations() {
        return periodStatementAsignations;
    }

    public void setPeriodStatementAsignations(Set<PeriodStatementAsignation> periodStatementAsignations) {
        this.periodStatementAsignations = periodStatementAsignations;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Statement statement = (Statement) o;

        return new EqualsBuilder().append(id, statement.id).append(areaType, statement.areaType).append(shortDescription, statement.shortDescription).append(description, statement.description).append(parentStatement, statement.parentStatement).append(area, statement.area).append(situation, statement.situation).isEquals();
    }


    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(areaType).append(shortDescription).append(description).append(parentStatement).append(area).append(situation).toHashCode();
    }

    @Override
    public String toString() {
        return "Statement{" +
                "id=" + id +
                ", state=" + state +
                ", areaType=" + areaType +
                ", shortDescription='" + shortDescription + '\'' +
                ", description='" + description + '\'' +
                ", parentStatement=" + parentStatement +
                ", area=" + area +
                ", pilar=" + pillar +
                ", situation=" + situation +
                '}';
    }
}
