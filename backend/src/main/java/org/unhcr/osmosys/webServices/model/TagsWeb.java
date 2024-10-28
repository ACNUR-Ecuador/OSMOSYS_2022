package org.unhcr.osmosys.webServices.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class TagsWeb extends BaseWebEntity implements Serializable {

    public TagsWeb() {
    }

    private String name;
    private String description;
    private List<PeriodTagAsignationWeb> periodTagAsignations;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PeriodTagAsignationWeb> getPeriodTagAsignations() {
        return periodTagAsignations;
    }

    public void setPeriodTagAsignations(List<PeriodTagAsignationWeb> periodTagAsignations) {
        this.periodTagAsignations = periodTagAsignations;
    }

    @Override
    public String toString() {
        return "TagsWeb{" +
                "id=" + id +
                ", state=" + state +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", periodTagAsignationWebList=" + periodTagAsignations +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TagsWeb tagsWeb = (TagsWeb) o;
        return Objects.equals(name, tagsWeb.name) && Objects.equals(description, tagsWeb.description) && Objects.equals(periodTagAsignations, tagsWeb.periodTagAsignations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, description, periodTagAsignations);
    }
}
