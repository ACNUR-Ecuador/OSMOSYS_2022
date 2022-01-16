package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.model.User;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.enums.IndicatorType;
import org.unhcr.osmosys.model.enums.QuarterEnum;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "osmosys", name = "quarters",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_ie_quarter_year", columnNames = {"indicator_execution_id", "quarter", "year"}),
                //@UniqueConstraint(name = "uk_ie_order", columnNames = {"indicator_execution_id", "order_"})
        }
)
public class Quarter extends BaseEntity<Long> {

    public Quarter() {
        this.state = State.ACTIVO;
        // this.totalExecution = BigDecimal.ZERO;
        this.executionPercentage = BigDecimal.ZERO;
        this.target = null;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "quarter", nullable = false)
    @Enumerated(EnumType.STRING)
    private QuarterEnum quarter;

    @Column(name = "commentary", columnDefinition = "text")
    private String commentary;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "indicator_execution_id", foreignKey = @ForeignKey(name = "fk_quarter_indicator_execution"))
    private IndicatorExecution indicatorExecution;

    @OneToMany(mappedBy = "quarter", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Month> months = new HashSet<>();

    @Column(name = "order_", nullable = false)
    private Integer order;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "target")
    private BigDecimal target;

    @Column(name = "total_execution")
    private BigDecimal totalExecution;

    @Column(name = "execution_percentage")
    private BigDecimal executionPercentage;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 12, unique = false)
    private State state;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuarterEnum getQuarter() {
        return quarter;
    }

    public void setQuarter(QuarterEnum quarter) {
        this.quarter = quarter;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public IndicatorExecution getIndicatorExecution() {
        return indicatorExecution;
    }

    public void setIndicatorExecution(IndicatorExecution indicatorExecution) {
        this.indicatorExecution = indicatorExecution;
    }

    public Set<Month> getMonths() {
        return months;
    }

    public void setMonths(Set<Month> months) {
        this.months = months;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getTarget() {
        return target;
    }

    public void setTarget(BigDecimal target) {
        this.target = target;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void addMonth(Month month) {
        month.setQuarter(this);
        if (!this.months.add(month)) {
            this.months.remove(month);
            this.months.add(month);
        }
    }

    public BigDecimal getTotalExecution() {
        return totalExecution;
    }

    public void setTotalExecution(BigDecimal totalExecution) {
        this.totalExecution = totalExecution;
    }

    public BigDecimal getExecutionPercentage() {
        return executionPercentage;
    }

    public void setExecutionPercentage(BigDecimal executionPercentage) {
        this.executionPercentage = executionPercentage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Quarter)) return false;

        Quarter quarter1 = (Quarter) o;

        return new EqualsBuilder().append(id, quarter1.id).append(quarter, quarter1.quarter).append(indicatorExecution, quarter1.indicatorExecution).append(order, quarter1.order).append(year, quarter1.year).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(quarter).append(indicatorExecution).append(order).append(year).toHashCode();
    }

    @Override
    public String toString() {
        return "Quarter{" +
                "quarter=" + quarter +
                ", order=" + order +
                ", year=" + year +
                '}';
    }
}
