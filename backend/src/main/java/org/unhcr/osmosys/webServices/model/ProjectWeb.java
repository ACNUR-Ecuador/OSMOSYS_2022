package org.unhcr.osmosys.webServices.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sagatechs.generics.webservice.jsonSerializers.LocalDateDeserializer;
import com.sagatechs.generics.webservice.jsonSerializers.LocalDateSerializer;
import com.sagatechs.generics.webservice.webModel.UserWeb;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


public class ProjectWeb extends BaseWebEntity implements Serializable {

    public ProjectWeb() {
        super();
    }

    private String code;
    private String name;
    private OrganizationWeb organization;
    private PeriodWeb period;
    private Set<CantonWeb> locations = new HashSet<>();
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate startDate;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate endDate;
    public Set<UserWeb> focalPoints = new HashSet<>();

    private Boolean updateAllLocationsIndicators;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public OrganizationWeb getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationWeb organization) {
        this.organization = organization;
    }

    public PeriodWeb getPeriod() {
        return period;
    }

    public void setPeriod(PeriodWeb period) {
        this.period = period;
    }

    public Set<CantonWeb> getLocations() {
        return locations;
    }

    public void setLocations(Set<CantonWeb> locations) {
        this.locations = locations;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Set<UserWeb> getFocalPoints() {
        return focalPoints;
    }

    public void setFocalPoints(Set<UserWeb> focalPoints) {
        this.focalPoints = focalPoints;
    }

    public Boolean getUpdateAllLocationsIndicators() {
        return updateAllLocationsIndicators;
    }

    public void setUpdateAllLocationsIndicators(Boolean updateAllLocationsIndicators) {
        this.updateAllLocationsIndicators = updateAllLocationsIndicators;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ProjectWeb that = (ProjectWeb) o;

        return new EqualsBuilder().append(id, that.id).append(code, that.code).append(period, that.period).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(code).append(period).toHashCode();
    }
}
