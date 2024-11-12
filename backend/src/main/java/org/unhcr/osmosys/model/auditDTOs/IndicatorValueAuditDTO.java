package org.unhcr.osmosys.model.auditDTOs;

import java.util.ArrayList;
import java.util.List;

public class IndicatorValueAuditDTO {
    private Long id;
    private String month;
    private String state;
    private String monthEnum;
    private String monthYearOrder;
    private String dissagregationType;
    private String populationType;
    private String countryOfOrigin;
    private String genderType;
    private String ageType;
    private String diversityType;
    private String location;
    private String value;
    private String denominatorValue;
    private String numeratorValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMonthEnum() {
        return monthEnum;
    }

    public void setMonthEnum(String monthEnum) {
        this.monthEnum = monthEnum;
    }

    public String getMonthYearOrder() {
        return monthYearOrder;
    }

    public void setMonthYearOrder(String monthYearOrder) {
        this.monthYearOrder = monthYearOrder;
    }

    public String getDissagregationType() {
        return dissagregationType;
    }

    public void setDissagregationType(String dissagregationType) {
        this.dissagregationType = dissagregationType;
    }

    public String getPopulationType() {
        return populationType;
    }

    public void setPopulationType(String populationType) {
        this.populationType = populationType;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public String getGenderType() {
        return genderType;
    }

    public void setGenderType(String genderType) {
        this.genderType = genderType;
    }

    public String getAgeType() {
        return ageType;
    }

    public void setAgeType(String ageType) {
        this.ageType = ageType;
    }

    public String getDiversityType() {
        return diversityType;
    }

    public void setDiversityType(String diversityType) {
        this.diversityType = diversityType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDenominatorValue() {
        return denominatorValue;
    }

    public void setDenominatorValue(String denominatorValue) {
        this.denominatorValue = denominatorValue;
    }

    public String getNumeratorValue() {
        return numeratorValue;
    }

    public void setNumeratorValue(String numeratorValue) {
        this.numeratorValue = numeratorValue;
    }

    public List<LabelValue> toLabelValueList() {
        List<LabelValue> labelValueList = new ArrayList<>();
        labelValueList.add(new LabelValue("ID", String.valueOf(id)));
        labelValueList.add(new LabelValue("Mes id", month));
        labelValueList.add(new LabelValue("Estado", state));
        labelValueList.add(new LabelValue("Mes", monthEnum));
        labelValueList.add(new LabelValue("Orden del Mes", monthYearOrder));
        labelValueList.add(new LabelValue("Tipo de Desagregación", dissagregationType));
        labelValueList.add(new LabelValue("Tipo de Población", populationType));
        labelValueList.add(new LabelValue("País de Origen", countryOfOrigin));
        labelValueList.add(new LabelValue("Género", genderType));
        labelValueList.add(new LabelValue("Edad", ageType));
        labelValueList.add(new LabelValue("Diversidad", diversityType));
        labelValueList.add(new LabelValue("Lugar", location));
        labelValueList.add(new LabelValue("Valor", value));
        labelValueList.add(new LabelValue("Valor denominador", denominatorValue));
        labelValueList.add(new LabelValue("Valor numerador", numeratorValue));

        return labelValueList;
    }


}
