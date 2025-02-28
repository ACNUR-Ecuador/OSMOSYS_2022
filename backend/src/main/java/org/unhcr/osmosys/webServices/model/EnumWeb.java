package org.unhcr.osmosys.webServices.model;

import java.io.Serializable;


public class EnumWeb implements Serializable {
    private String value;
    private String label;
    private int order;

    private boolean isLocationsDissagregation;

    private boolean isAgeDissagregation;

    private String[] standardDissagregationTypes;

    private Integer numberOfDissagregations;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isLocationsDissagregation() {
        return isLocationsDissagregation;
    }

    public void setLocationsDissagregation(boolean locationsDissagregation) {
        isLocationsDissagregation = locationsDissagregation;
    }

    public boolean isAgeDissagregation() {
        return isAgeDissagregation;
    }

    public void setAgeDissagregation(boolean ageDissagregation) {
        isAgeDissagregation = ageDissagregation;
    }

    public String[] getStandardDissagregationTypes() {
        return standardDissagregationTypes;
    }

    public void setStandardDissagregationTypes(String[] standardDissagregationTypes) {
        this.standardDissagregationTypes = standardDissagregationTypes;
    }

    public Integer getNumberOfDissagregations() {
        return numberOfDissagregations;
    }

    public void setNumberOfDissagregations(Integer numberOfDissagregations) {
        this.numberOfDissagregations = numberOfDissagregations;
    }
}
