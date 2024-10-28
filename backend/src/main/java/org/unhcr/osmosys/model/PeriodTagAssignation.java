package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntityIdState;
import com.sagatechs.generics.persistence.model.State;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(schema = "osmosys", name = "period_tag_asignations",
        uniqueConstraints = @UniqueConstraint(name = "period_tag_ids_unique", columnNames = {"period_id", "tag_id"})
)
public class PeriodTagAssignation extends BaseEntityIdState {


    public PeriodTagAssignation() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "period_id", nullable = false, foreignKey = @ForeignKey(name = "period_tag_asignation_period"))
    private Period period;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tag_id", nullable = false, foreignKey = @ForeignKey(name = "period_tag_asignation_tag"))
    private Tags tag;


    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public State getState() {
        return state;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Tags getTag() {
        return tag;
    }

    public void setTag(Tags tag) {
        this.tag = tag;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PeriodTagAssignation that = (PeriodTagAssignation) o;
        return Objects.equals(id, that.id) && Objects.equals(period, that.period) && Objects.equals(tag, that.tag) && state == that.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, period, tag, state);
    }

    @Override
    public String toString() {
        return "PeriodTagAssignation{" +
                "id=" + id +
                ", period=" + period +
                ", tag=" + tag +
                ", state=" + state +
                '}';
    }
}
