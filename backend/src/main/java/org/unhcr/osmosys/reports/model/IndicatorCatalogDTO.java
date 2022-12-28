package org.unhcr.osmosys.reports.model;

public class IndicatorCatalogDTO {

    private String impactAreaAndDeclaration;
    private String outcomeArea;
    private String outcomeStatement;
    private String outputStatement;
    private String outputIndicatorCode;
    private String outputIndicator;
    private String frecuency;
    private String dissagregation;

    public String getImpactAreaAndDeclaration() {
        return impactAreaAndDeclaration;
    }

    public void setImpactAreaAndDeclaration(String impactAreaAndDeclaration) {
        this.impactAreaAndDeclaration = impactAreaAndDeclaration;
    }

    public String getOutcomeArea() {
        return outcomeArea;
    }

    public void setOutcomeArea(String outcomeArea) {
        this.outcomeArea = outcomeArea;
    }

    public String getOutcomeStatement() {
        return outcomeStatement;
    }

    public void setOutcomeStatement(String outcomeStatement) {
        this.outcomeStatement = outcomeStatement;
    }

    public String getOutputStatement() {
        return outputStatement;
    }

    public void setOutputStatement(String outputStatement) {
        this.outputStatement = outputStatement;
    }

    public String getOutputIndicatorCode() {
        return outputIndicatorCode;
    }

    public void setOutputIndicatorCode(String outputIndicatorCode) {
        this.outputIndicatorCode = outputIndicatorCode;
    }

    public String getOutputIndicator() {
        return outputIndicator;
    }

    public void setOutputIndicator(String outputIndicator) {
        this.outputIndicator = outputIndicator;
    }

    public String getFrecuency() {
        return frecuency;
    }

    public void setFrecuency(String frecuency) {
        this.frecuency = frecuency;
    }

    public String getDissagregation() {
        return dissagregation;
    }

    public void setDissagregation(String dissagregation) {
        this.dissagregation = dissagregation;
    }

    @Override
    public String toString() {
        return "IndicatorCatalog{" +
                "impactAreaAndDeclaration='" + impactAreaAndDeclaration + '\'' +
                ", outcomeArea='" + outcomeArea + '\'' +
                ", outcomeStatement='" + outcomeStatement + '\'' +
                ", outputStatement='" + outputStatement + '\'' +
                ", outputIndicatorCode='" + outputIndicatorCode + '\'' +
                ", outputIndicator='" + outputIndicator + '\'' +
                ", frecuency='" + frecuency + '\'' +
                ", dissagregation='" + dissagregation + '\'' +
                '}';
    }
}
