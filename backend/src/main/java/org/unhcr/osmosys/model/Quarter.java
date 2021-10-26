package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.model.User;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.unhcr.osmosys.model.enums.IndicatorType;
import org.unhcr.osmosys.model.enums.QuarterEnum;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "osmosys", name = "quarters")
public class Quarter extends BaseEntity<Long> {

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

    @OneToMany(mappedBy = "quarter",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<Month> months;


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
}
