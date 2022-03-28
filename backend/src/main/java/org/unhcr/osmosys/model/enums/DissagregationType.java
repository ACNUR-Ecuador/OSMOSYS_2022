package org.unhcr.osmosys.model.enums;

import java.util.ArrayList;
import java.util.List;

public enum DissagregationType implements EnumInterface {
    TIPO_POBLACION("Tipo de población", 1),
    EDAD("Edad", 2),
    GENERO("Género", 3),
    LUGAR("Sitio/cantón", 4),
    PAIS_ORIGEN("País de Origen", 5),
    DIVERSIDAD("Diversidad", 6),
    SIN_DESAGREGACION("Sin Desagregación", 7),
    GENERO_Y_EDAD("Género y edad", 8),
    TIPO_POBLACION_Y_GENERO("Tipo de población	y género", 9),
    TIPO_POBLACION_Y_EDAD("Tipo de población y edad", 10),
    TIPO_POBLACION_Y_DIVERSIDAD("Tipo de población y diversidad", 11),
    TIPO_POBLACION_Y_PAIS_ORIGEN("Tipo de población y país de origen", 12),
    TIPO_POBLACION_Y_LUGAR("Tipo de población y sitio/cantón", 13),
    ;

    public static List<DissagregationType> getLocationDissagregationTypes() {
        List<DissagregationType> r = new ArrayList<>();
        r.add(TIPO_POBLACION_Y_LUGAR);
        r.add(LUGAR);
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