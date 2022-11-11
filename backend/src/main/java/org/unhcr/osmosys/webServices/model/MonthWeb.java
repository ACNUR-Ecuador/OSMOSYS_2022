package org.unhcr.osmosys.webServices.model;

import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.enums.MonthEnum;
import org.unhcr.osmosys.model.enums.SourceType;
import org.unhcr.osmosys.model.enums.TimeStateEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;


public class MonthWeb implements Serializable {
    private Long id;
    private MonthEnum month;
    private Integer year;
    private Integer order;
    private State state;
    private String commentary;
    private BigDecimal totalExecution;
    private Set<SourceType> sources;
    private String sourceOther;
    private Boolean checked;
    private BigDecimal usedBudget;
    private Boolean blockUpdate;

    private TimeStateEnum late;

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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }


    public BigDecimal getTotalExecution() {
        return totalExecution;
    }

    public void setTotalExecution(BigDecimal totalExecution) {
        this.totalExecution = totalExecution;
    }

    public Set<SourceType> getSources() {
        return sources;
    }

    public void setSources(Set<SourceType> sources) {
        this.sources = sources;
    }

    public String getSourceOther() {
        return sourceOther;
    }

    public void setSourceOther(String sourceOther) {
        this.sourceOther = sourceOther;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public BigDecimal getUsedBudget() {
        return usedBudget;
    }

    public void setUsedBudget(BigDecimal usedBudget) {
        this.usedBudget = usedBudget;
    }

    public Boolean getBlockUpdate() {
        return blockUpdate;
    }

    public void setBlockUpdate(Boolean blockUpdate) {
        this.blockUpdate = blockUpdate;
    }

    public TimeStateEnum getLate() {
        return late;
    }

    public void setLate(TimeStateEnum late) {
        this.late = late;
    }
}
