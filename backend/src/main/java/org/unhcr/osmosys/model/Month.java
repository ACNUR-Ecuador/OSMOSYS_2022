package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.enums.MonthEnum;
import org.unhcr.osmosys.model.enums.SourceType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "osmosys", name = "months",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_month_quarter_year", columnNames = {"quarter_id", "month", "year"}),
                //@UniqueConstraint(name = "uk_month_quarter_order", columnNames = {"quarter_id", "order_"}),
        }
)
public class Month extends BaseEntity<Long> {

    public Month() {
        this.state = State.ACTIVO;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "month", nullable = false)
    @Enumerated(EnumType.STRING)
    private MonthEnum month;

    @Column(name = "month_year_order", nullable = false)
    private Integer monthYearOrder;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "order_", nullable = false)
    private Integer order;

    @Column(name = "used_budget")
    private BigDecimal usedBudget;

    @ElementCollection(targetClass = SourceType.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "month_source", schema = "osmosys",
            joinColumns = @JoinColumn(name = "month_id"))
    @Column(name = "source_type")
    @Enumerated(EnumType.STRING)
    private Set<SourceType> sources;

    @Column(name = "source_other")
    private String sourceOther;

    @Column(name = "checked")
    private Boolean checked;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12)
    private State state;

    @Column(name = "commentary", columnDefinition = "text")
    private String commentary;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quarter_id", foreignKey = @ForeignKey(name = "fk_month_quarter"))
    private Quarter quarter;

    @Column(name = "block_update")
    private Boolean blockUpdate;

    @Column(name = "total_execution")
    private BigDecimal totalExecution;


    @OneToMany(mappedBy = "month", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<IndicatorValue> indicatorValues = new HashSet<>();

    @OneToMany(mappedBy = "month", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<IndicatorValueCustomDissagregation> indicatorValuesIndicatorValueCustomDissagregations = new HashSet<>();


    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MonthEnum getMonth() {
        return month;
    }

    public void setMonth(MonthEnum monthEnum) {
        if (monthEnum != null) {
            this.monthYearOrder = monthEnum.getOrder();
        } else {
            this.monthYearOrder = null;
        }
        this.month = monthEnum;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public Quarter getQuarter() {
        return quarter;
    }

    public void setQuarter(Quarter quarter) {
        this.quarter = quarter;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Set<IndicatorValue> getIndicatorValues() {
        return indicatorValues;
    }

    public void setIndicatorValues(Set<IndicatorValue> indicatorValues) {
        this.indicatorValues = indicatorValues;
    }

    public void addIndicatorValue(IndicatorValue indicatorValue) {
        indicatorValue.setMonth(this);
        indicatorValue.setMonthEnum(this.getMonth());
        if (!this.indicatorValues.add(indicatorValue)) {
            this.indicatorValues.add(indicatorValue);
        }
    }

    public Set<IndicatorValueCustomDissagregation> getIndicatorValuesIndicatorValueCustomDissagregations() {
        return indicatorValuesIndicatorValueCustomDissagregations;
    }

    public void setIndicatorValuesIndicatorValueCustomDissagregations(Set<IndicatorValueCustomDissagregation> indicatorValuesIndicatorValueCustomDissagregations) {
        this.indicatorValuesIndicatorValueCustomDissagregations = indicatorValuesIndicatorValueCustomDissagregations;
    }

    public void addIndicatorValueCustomDissagregation(IndicatorValueCustomDissagregation indicatorValue) {
        indicatorValue.setMonth(this);
        indicatorValue.setMonthEnum(this.getMonth());
        if (!this.indicatorValuesIndicatorValueCustomDissagregations.add(indicatorValue)) {
            this.indicatorValuesIndicatorValueCustomDissagregations.add(indicatorValue);
        }
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public BigDecimal getTotalExecution() {
        return totalExecution;
    }

    public void setTotalExecution(BigDecimal totalExecution) {
        this.totalExecution = totalExecution;
    }

    public Set<SourceType> getSources() {
        return sources;
    }

    public void setSources(Set<SourceType> sources) {
        this.sources = sources;
    }

    public void addSource(SourceType sourceType) {
        this.sources.add(sourceType);
    }

    public void removeSource(SourceType sourceType) {
        this.sources.remove(sourceType);
    }

    public String getSourceOther() {
        return sourceOther;
    }

    public void setSourceOther(String sourceOther) {
        this.sourceOther = sourceOther;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Integer getMonthYearOrder() {
        return monthYearOrder;
    }

    public void setMonthYearOrder(Integer monthYearOrder) {
        this.monthYearOrder = monthYearOrder;
    }

    public BigDecimal getUsedBudget() {
        return usedBudget;
    }

    public void setUsedBudget(BigDecimal usedBudget) {
        this.usedBudget = usedBudget;
    }

    public Boolean getBlockUpdate() {
        return blockUpdate;
    }

    public void setBlockUpdate(Boolean blockUpdate) {
        this.blockUpdate = blockUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Month)) return false;

        Month month1 = (Month) o;

        return new EqualsBuilder().append(id, month1.id).append(month, month1.month).append(year, month1.year).append(order, month1.order).append(quarter, month1.quarter).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(month).append(year).append(order).append(quarter).toHashCode();
    }

    @Override
    public String toString() {
        return "Month{" +
                "month=" + month +
                ", year=" + year +
                ", order=" + order +
                '}';
    }
}
