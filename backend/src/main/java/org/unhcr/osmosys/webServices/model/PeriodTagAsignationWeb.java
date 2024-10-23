package org.unhcr.osmosys.webServices.model;

import java.io.Serializable;
import java.util.Objects;

public class PeriodTagAsignationWeb extends BaseWebEntity implements Serializable {

    public PeriodTagAsignationWeb() {super();}

    private PeriodWeb period;



    public PeriodWeb getPeriod() {
        return period;
    }

    public void setPeriod(PeriodWeb period) {
        this.period = period;
    }

    @Override
    public String toString() {
        return "PeriodTagAsignationWeb{" +
                "period=" + period +
                ", id=" + id +
                ", state=" + state +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PeriodTagAsignationWeb that = (PeriodTagAsignationWeb) o;
        return Objects.equals(period, that.period);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), period);
    }
}
