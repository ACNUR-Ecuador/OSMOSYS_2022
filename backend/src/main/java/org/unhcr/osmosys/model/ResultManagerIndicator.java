package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import org.unhcr.osmosys.model.standardDissagregations.options.StandardDissagregationOption;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(schema = "osmosys", name = "result_manager_indicators")
public class ResultManagerIndicator extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "indicator_id", foreignKey = @ForeignKey(name = "fk_result_manager_indicator"))
    private Indicator indicator;

    @Column(name = "quarter_year_order", nullable = false)
    private int quarterYearOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "population_diss_option_id", foreignKey = @ForeignKey(name = "fk_result_manager_population_diss_opt"))
    private StandardDissagregationOption populationType;

    @Column(name = "is_confirmed", nullable = false)
    private boolean confirmed;

    @Column(name = "report_value")
    private Integer reportValue;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "period_id", foreignKey = @ForeignKey(name = "fk_result_manager_ind_period"))
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

    public StandardDissagregationOption getPopulationType() {
        return populationType;
    }

    public void setPopulationType(StandardDissagregationOption populationType) {
        this.populationType = populationType;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Integer getReportValue() {
        return reportValue;
    }

    public void setReportValue(Integer reportValue) {
        this.reportValue = reportValue;
    }

    @Override
    public String toString() {
        return "ResultManagerIndicator{" +
                "id=" + id +
                ", indicator=" + indicator +
                ", quarterYearOrder=" + quarterYearOrder +
                ", populationType=" + populationType +
                ", confirmed=" + confirmed +
                ", reportValue=" + reportValue +
                ", period=" + period +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultManagerIndicator that = (ResultManagerIndicator) o;
        return quarterYearOrder == that.quarterYearOrder && confirmed == that.confirmed && Objects.equals(id, that.id) && Objects.equals(indicator, that.indicator) && Objects.equals(populationType, that.populationType) && Objects.equals(reportValue, that.reportValue) && Objects.equals(period, that.period);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, indicator, quarterYearOrder, populationType, confirmed, reportValue, period);
    }
}
