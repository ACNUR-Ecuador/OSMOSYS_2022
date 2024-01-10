package org.unhcr.osmosys.webServices.model;

import java.io.Serializable;
import java.util.Objects;


public class PeriodStatementAsignationWeb extends BaseWebEntity implements Serializable {


    public PeriodStatementAsignationWeb() {super();
    }

    private PeriodWeb period;



    public PeriodWeb getPeriod() {
        return period;
    }

    public void setPeriod(PeriodWeb period) {
        this.period = period;
    }

    @Override
    public String toString() {
        return "PeriodStatementAsignationWeb{" +
                "id=" + id +
                ", state=" + state +
                ", period=" + period +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PeriodStatementAsignationWeb)) return false;
        PeriodStatementAsignationWeb that = (PeriodStatementAsignationWeb) o;
        return Objects.equals(period, that.period);
    }

    @Override
    public int hashCode() {
        return Objects.hash(period);
    }
}
