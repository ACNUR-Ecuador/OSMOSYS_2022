package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(schema = "osmosys", name = "result_manager_indicator_quarter_report")
public class ResultManagerIndicatorQuarterReport extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "indicator_id", foreignKey = @ForeignKey(name = "fk_result_man_ind_quarter_report"))
    private Indicator indicator;

    @Column(name = "quarter_year_order", nullable = false)
    private int quarterYearOrder;

    @Column(name = "report_comment")
    private String reportComment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "period_id", foreignKey = @ForeignKey(name = "fk_result_man_ind_quarter_report_period"))
    private Period period;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Indicator getIndicator() {
        return indicator;
    }

    public void setIndicator(Indicator indicator) {
        this.indicator = indicator;
    }

    public int getQuarterYearOrder() {
        return quarterYearOrder;
    }

    public void setQuarterYearOrder(int quarterYearOrder) {
        this.quarterYearOrder = quarterYearOrder;
    }

    public String getReportComment() {
        return reportComment;
    }

    public void setReportComment(String reportComment) {
        this.reportComment = reportComment;
    }


    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    @Override
    public String toString() {
        return "ResultManagerIndicatorQuarterReport{" +
                "id=" + id +
                ", indicator=" + indicator +
                ", quarterYearOrder=" + quarterYearOrder +
                ", reportComment='" + reportComment + '\'' +
                ", period=" + period +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultManagerIndicatorQuarterReport that = (ResultManagerIndicatorQuarterReport) o;
        return quarterYearOrder == that.quarterYearOrder && Objects.equals(id, that.id) && Objects.equals(indicator, that.indicator) && Objects.equals(reportComment, that.reportComment) && Objects.equals(period, that.period);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, indicator, quarterYearOrder, reportComment, period);
    }
}
