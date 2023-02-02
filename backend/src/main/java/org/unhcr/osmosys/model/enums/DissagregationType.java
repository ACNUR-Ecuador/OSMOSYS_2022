package org.unhcr.osmosys.model.enums;

import java.util.ArrayList;
import java.util.List;

public enum DissagregationType implements EnumInterface {
    TIPO_POBLACION("Tipo de población", 1),
    EDAD("Edad", 2),
    EDAD_EDUCACION_PRIMARIA("Edad-Educación Primaria", 3),
    EDAD_EDUCACION_TERCIARIA("Edad-Educación Terciaria", 4),
    GENERO("Género", 5),
    LUGAR("Lugar", 6),
    PAIS_ORIGEN("País de Origen", 7),
    DIVERSIDAD("Diversidad", 8),
    SIN_DESAGREGACION("Sin Desagregación", 9),
    GENERO_Y_EDAD("Género y edad", 10),
    GENERO_Y_DIVERSIDAD("Género y diversidad", 11),
    TIPO_POBLACION_Y_GENERO("Tipo de población	y género", 12),
    TIPO_POBLACION_Y_EDAD("Tipo de población y edad", 13),
    TIPO_POBLACION_Y_DIVERSIDAD("Tipo de población y diversidad", 14),
    TIPO_POBLACION_Y_PAIS_ORIGEN("Tipo de población y país de origen", 15),
    TIPO_POBLACION_Y_LUGAR("Tipo de población y lugar", 16),
    LUGAR_Y_GENERO("Lugar y Género", 17),
    LUGAR_Y_DIVERSIDAD("Diversidad y lugar", 18),
    LUGAR_EDAD_Y_GENERO("Lugar, género y edad", 19),
    DIVERSIDAD_EDAD_Y_GENERO("Diversidad, género y edad", 20),
    PAIS_ORIGEN_EDAD_Y_GENERO("País de Origen, género y edad", 21),
    DIVERSIDAD_EDAD_EDUCACION_PRIMARIA_Y_GENERO("Diversidad, género y edad-educación primaria", 22),
    DIVERSIDAD_EDAD_EDUCACION_TERCIARIA_Y_GENERO("Diversidad, género y edad-educación terciaria", 23),
    TIPO_POBLACION_LUGAR_EDAD_Y_GENERO("Tipo de población,lugar, género y edad", 24),
    TIPO_POBLACION_LUGAR_EDAD_EDUCACION_PRIMARIA_Y_GENERO("Tipo de población,lugar, género  y edad-educación primaria", 25),
    TIPO_POBLACION_LUGAR_EDAD_EDUCACION_TERCIARIA_Y_GENERO("Tipo de población,lugar, género y edad-educación terciaria", 26),
    LUGAR_DIVERSIDAD_EDAD_EDUCACION_PRIMARIA_Y_GENERO("Lugar, diversidad, género y edad-educación primaria", 27),
    LUGAR_PAIS_ORIGEN_EDAD_Y_GENERO("Lugar, País de Origen, género y edad", 28),
    LUGAR_PAIS_ORIGEN_EDAD_EDUCACION_PRIMARIA_Y_GENERO("Lugar, País de Origen, género y edad-educación primaria", 29),
    PAIS_ORIGEN_EDAD_EDUCACION_PRIMARIA_Y_GENERO("País de Origen, género y edad-educación primaria", 30),
    ;

    public static List<DissagregationType> getLocationDissagregationTypes() {
        List<DissagregationType> r = new ArrayList<>();
        r.add(TIPO_POBLACION_Y_LUGAR);
        r.add(LUGAR);
        r.add(LUGAR_EDAD_Y_GENERO);
        r.add(TIPO_POBLACION_LUGAR_EDAD_Y_GENERO);
        r.add(TIPO_POBLACION_LUGAR_EDAD_EDUCACION_PRIMARIA_Y_GENERO);
        r.add(TIPO_POBLACION_LUGAR_EDAD_EDUCACION_TERCIARIA_Y_GENERO);
        r.add(LUGAR_Y_DIVERSIDAD);
        r.add(LUGAR_Y_GENERO);
        r.add(LUGAR_PAIS_ORIGEN_EDAD_Y_GENERO);
        r.add(LUGAR_PAIS_ORIGEN_EDAD_EDUCACION_PRIMARIA_Y_GENERO);
        return r;
    }

    private String label;
    private final int order;

    DissagregationType(String label, int order) {
        this.label = label;
        this.order = order;
    }

    @Override
    public String getStringValue() {
        return this.name();
    }

    public String getLabel() {
        return label;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}