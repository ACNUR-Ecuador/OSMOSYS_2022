package org.unhcr.osmosys.webServices.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.webservice.jsonSerializers.LocalDateDeserializer;
import com.sagatechs.generics.webservice.jsonSerializers.LocalDateSerializer;

import java.io.Serializable;
import java.time.LocalDate;


@SuppressWarnings("unused")
public class ProjectResumeWeb extends BaseWebEntity implements Serializable {
    public ProjectResumeWeb() {
        super();
    }

    public ProjectResumeWeb(
            Long id,
            String code,
            String name,
            String state,
            Long organizationId,
            String organizationDescription,
            String organizationAcronym,
            Long periodId,
            Integer periodYear,
            LocalDate startDate,
            LocalDate endDate
    ) {
        super(id, State.valueOf(state));

        this.code = code;
        this.name = name;


        this.organizationId = organizationId;
        this.organizationDescription = organizationDescription;
        this.organizationAcronym = organizationAcronym;
        this.periodId = periodId;
        this.periodYear = periodYear;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    private String code;
    private String name;
    private Long organizationId;
    private String organizationDescription;
    private String organizationAcronym;
    private Long periodId;
    private Integer periodYear;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate startDate;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate endDate;


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


    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationDescription() {
        return organizationDescription;
    }

    public void setOrganizationDescription(String organizationDescription) {
        this.organizationDescription = organizationDescription;
    }

    public String getOrganizationAcronym() {
        return organizationAcronym;
    }

    public void setOrganizationAcronym(String organizationAcronym) {
        this.organizationAcronym = organizationAcronym;
    }

    public Long getPeriodId() {
        return periodId;
    }

    public void setPeriodId(Long periodId) {
        this.periodId = periodId;
    }

    public Integer getPeriodYear() {
        return periodYear;
    }

    public void setPeriodYear(Integer periodYear) {
        this.periodYear = periodYear;
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
}
