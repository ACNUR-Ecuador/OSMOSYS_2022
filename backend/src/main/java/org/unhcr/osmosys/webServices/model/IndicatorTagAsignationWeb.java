package org.unhcr.osmosys.webServices.model;

import java.io.Serializable;
import java.util.Objects;

public class IndicatorTagAsignationWeb extends BaseWebEntity implements Serializable {

    public IndicatorTagAsignationWeb() {super();}

    private IndicatorWeb indicator;
    private TagsWeb tags;


    public IndicatorWeb getIndicator() {
        return indicator;
    }

    public void setIndicator(IndicatorWeb indicator) {
        this.indicator = indicator;
    }

    public TagsWeb getTags() {
        return tags;
    }

    public void setTags(TagsWeb tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        IndicatorTagAsignationWeb that = (IndicatorTagAsignationWeb) o;
        return Objects.equals(indicator, that.indicator) && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), indicator, tags);
    }
}
