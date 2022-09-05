package org.unhcr.osmosys.model.ImportDTOs;

import org.apache.commons.lang3.StringUtils;

public class CatalogImportDTO {

    private String totalStatement;
    private String totalIndicator;
    private String codeIndicator;
    private String totalActivityInfoIndicator;
    private String todalDissagregation;
    private String indicatorDescription;


    public String getTotalStatement() {
        return totalStatement;
    }

    public void setTotalStatement(String totalStatement) {
        this.totalStatement = totalStatement;
    }

    public String getTotalIndicator() {
        return totalIndicator;
    }

    public void setTotalIndicator(String totalIndicator) {
        this.totalIndicator = totalIndicator;
    }

    public String getCodeIndicator() {
        return codeIndicator;
    }

    public void setCodeIndicator(String codeIndicator) {
        this.codeIndicator = codeIndicator;
    }

    public String getTotalActivityInfoIndicator() {
        return totalActivityInfoIndicator;
    }

    public void setTotalActivityInfoIndicator(String totalActivityInfoIndicator) {
        this.totalActivityInfoIndicator = totalActivityInfoIndicator;
    }

    public String getTodalDissagregation() {
        return todalDissagregation;
    }

    public void setTodalDissagregation(String todalDissagregation) {
        this.todalDissagregation = todalDissagregation;
    }

    public String getIndicatorDescription() {
        return indicatorDescription;
    }

    public void setIndicatorDescription(String indicatorDescription) {
        this.indicatorDescription = indicatorDescription;
    }

    public Boolean hasFullData() {

        return StringUtils.isNotBlank(this.getTotalStatement())
                && StringUtils.isNotBlank(this.getTotalIndicator())
                && StringUtils.isNotBlank(this.getCodeIndicator())
                && StringUtils.isNotBlank(this.getTotalActivityInfoIndicator())
                && StringUtils.isNotBlank(this.getTodalDissagregation())

                ;


    }

    @Override
    public String toString() {
        return "CatalogImportDTO{" +
                "totalStatement='" + totalStatement + '\'' +
                ", totalIndicator='" + totalIndicator + '\'' +
                ", codeIndicator='" + codeIndicator + '\'' +
                ", totalActivityInfoIndicator='" + totalActivityInfoIndicator + '\'' +
                ", todalDissagregation='" + todalDissagregation + '\'' +
                ", indicatorDescription='" + indicatorDescription + '\'' +
                '}';
    }
}
