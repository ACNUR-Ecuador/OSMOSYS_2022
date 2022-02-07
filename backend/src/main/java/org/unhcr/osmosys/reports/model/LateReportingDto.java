package org.unhcr.osmosys.reports.model;

import org.jeasy.random.EasyRandom;

import java.util.List;
import java.util.stream.Collectors;

public class LateReportingDto {
    private String projectName;
    private String partnerCode;
    private String partnerName;
    private String statmentCode;
    private String statmentName;
    private String indicatorCode;
    private String indicatorName;
    private String notReportedMonths;

    public static List<LateReportingDto> createBeanCollection() {
        EasyRandom easyRandom = new EasyRandom();
        return easyRandom.objects(LateReportingDto.class, 10)
                .collect(Collectors.toList());
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getStatmentCode() {
        return statmentCode;
    }

    public void setStatmentCode(String statmentCode) {
        this.statmentCode = statmentCode;
    }

    public String getStatmentName() {
        return statmentName;
    }

    public void setStatmentName(String statmentName) {
        this.statmentName = statmentName;
    }

    public String getIndicatorCode() {
        return indicatorCode;
    }

    public void setIndicatorCode(String indicatorCode) {
        this.indicatorCode = indicatorCode;
    }

    public String getIndicatorName() {
        return indicatorName;
    }

    public void setIndicatorName(String indicatorName) {
        this.indicatorName = indicatorName;
    }

    public String getNotReportedMonths() {
        return notReportedMonths;
    }

    public void setNotReportedMonths(String notReportedMonths) {
        this.notReportedMonths = notReportedMonths;
    }
}
