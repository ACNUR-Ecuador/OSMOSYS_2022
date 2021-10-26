package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import org.unhcr.osmosys.model.enums.MonthEnum;
import org.unhcr.osmosys.model.enums.QuarterEnum;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "osmosys", name = "months")
public class Month extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "month", nullable = false)
    @Enumerated(EnumType.STRING)
    private MonthEnum month;

    @Column(name = "commentary", columnDefinition = "text")
    private String commentary;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quarter_id", foreignKey = @ForeignKey(name = "fk_month_quarter"))
    private Quarter quarter;

    @OneToMany(mappedBy = "month",fetch = FetchType.LAZY)
    private Set<IndicatorValue> indicatorValues= new HashSet<>();

    @OneToMany(mappedBy = "month",fetch = FetchType.LAZY)
    private Set<IndicatorValueCustomDissagregation> indicatorValuesIndicatorValueCustomDissagregations= new HashSet<>();


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

    public void setMonth(MonthEnum month) {
        this.month = month;
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
}
