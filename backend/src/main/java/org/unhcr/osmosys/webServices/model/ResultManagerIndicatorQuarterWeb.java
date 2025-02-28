package org.unhcr.osmosys.webServices.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class ResultManagerIndicatorQuarterWeb implements Serializable {
    public ResultManagerIndicatorQuarterWeb() {}
    private Long id;
    private String reportComment;
    private int quarter;
    private BigDecimal quarterExecution;
    private List<ResultManagerQuarterPopulationTypeWeb> resultManagerQuarterPopulationType;
    private List<ResultManagerQuarterImplementerWeb> resultManagerQuarterImplementer;

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }

    public BigDecimal getQuarterExecution() {
        return quarterExecution;
    }

    public void setQuarterExecution(BigDecimal quarterExecution) {
        this.quarterExecution = quarterExecution;
    }

    public List<ResultManagerQuarterPopulationTypeWeb> getResultManagerQuarterPopulationType() {
        return resultManagerQuarterPopulationType;
    }

    public void setResultManagerQuarterPopulationType(List<ResultManagerQuarterPopulationTypeWeb> resultManagerQuarterPopulationType) {
        this.resultManagerQuarterPopulationType = resultManagerQuarterPopulationType;
    }

    public List<ResultManagerQuarterImplementerWeb> getResultManagerQuarterImplementer() {
        return resultManagerQuarterImplementer;
    }

    public void setResultManagerQuarterImplementer(List<ResultManagerQuarterImplementerWeb> resultManagerQuarterImplementer) {
        this.resultManagerQuarterImplementer = resultManagerQuarterImplementer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getReportComment() {
        return reportComment;
    }

    public void setReportComment(String reportComment) {
        this.reportComment = reportComment;
    }

    @Override
    public String toString() {
        return "ResultManagerIndicatorQuarterWeb{" +
                "id=" + id +
                ", reportComment='" + reportComment + '\'' +
                ", quarter=" + quarter +
                ", quarterExecution=" + quarterExecution +
                ", resultManagerQuarterPopulationType=" + resultManagerQuarterPopulationType +
                ", resultManagerQuarterImplementer=" + resultManagerQuarterImplementer +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultManagerIndicatorQuarterWeb that = (ResultManagerIndicatorQuarterWeb) o;
        return quarter == that.quarter && Objects.equals(id, that.id) && Objects.equals(reportComment, that.reportComment) && Objects.equals(quarterExecution, that.quarterExecution) && Objects.equals(resultManagerQuarterPopulationType, that.resultManagerQuarterPopulationType) && Objects.equals(resultManagerQuarterImplementer, that.resultManagerQuarterImplementer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reportComment, quarter, quarterExecution, resultManagerQuarterPopulationType, resultManagerQuarterImplementer);
    }
}
