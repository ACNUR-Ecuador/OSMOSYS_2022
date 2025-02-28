package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.BaseEntityIdState;
import com.sagatechs.generics.persistence.model.State;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(schema = "osmosys", name = "indicator_tag_asignation",
        uniqueConstraints = @UniqueConstraint(name = "indicator_tag_ids_unique", columnNames = {"indicator_id", "tag_id"})
)
public class IndicatorTagAssignation extends BaseEntityIdState {


    public IndicatorTagAssignation() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "indicator_id", nullable = false, foreignKey = @ForeignKey(name = "indicator_tag_asignation_indicator"))
    private Indicator indicator;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tag_id", nullable = false, foreignKey = @ForeignKey(name = "indicator_tag_asignation_tag"))
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

    public Indicator getIndicator() {
        return indicator;
    }

    public void setIndicator(Indicator indicator) {
        this.indicator = indicator;
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
        IndicatorTagAssignation that = (IndicatorTagAssignation) o;
        return Objects.equals(id, that.id) && Objects.equals(indicator, that.indicator) && Objects.equals(tag, that.tag) && state == that.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, indicator, tag, state);
    }
}
