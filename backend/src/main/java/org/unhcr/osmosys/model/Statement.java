package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.BaseEntityIdState;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.enums.AreaType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(schema = "osmosys", name = "statements")
public class Statement extends BaseEntityIdState {

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


    @Column(name = "code")
    private String code;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "description", columnDefinition = "text", unique = true)
    private String description;


    // en ia
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_statement_id", foreignKey = @ForeignKey(name = "fk_statemet_statement_parent"))
    private Statement parentStatement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", foreignKey = @ForeignKey(name = "fk_statement_area"))
    private Area area;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pillar_id", foreignKey = @ForeignKey(name = "fk_statemet_pillar"))
    private Pillar pillar;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "situation_id", foreignKey = @ForeignKey(name = "fk_statemet_situation"))
    private Situation situation;

    @OneToMany(mappedBy = "statement", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<PeriodStatementAsignation> periodStatementAsignations = new HashSet<>();

    @OneToMany(mappedBy = "statement",fetch = FetchType.LAZY)
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


    public Set<PeriodStatementAsignation> getPeriodStatementAsignations() {
        return periodStatementAsignations;
    }

    public void setPeriodStatementAsignations(Set<PeriodStatementAsignation> periodStatementAsignations) {
        this.periodStatementAsignations = periodStatementAsignations;
    }

    public void addPeriodStatementAsignation(PeriodStatementAsignation periodStatementAsignation) {
        periodStatementAsignation.setStatement(this);
        if (!this.periodStatementAsignations.add(periodStatementAsignation)) {
            this.periodStatementAsignations.remove(periodStatementAsignation);
            this.periodStatementAsignations.add(periodStatementAsignation);
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Statement)) return false;

        Statement statement = (Statement) o;

        return new EqualsBuilder().append(id, statement.id).append(areaType, statement.areaType).append(code, statement.code).append(description, statement.description).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(areaType).append(code).append(description).toHashCode();
    }
}
