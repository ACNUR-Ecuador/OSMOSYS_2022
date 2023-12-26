package org.unhcr.osmosys.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum DissagregationType implements EnumInterface {

    SIN_DESAGREGACION("Sin Desagregación", new String[]{}, 0),

    LUGAR("Lugar", new String[]{"LUGAR"}, 1),
    TIPO_POBLACION("Tipo de Población", new String[]{"TIPO_POBLACION"}, 2),
    PAIS_ORIGEN("País de Origen", new String[]{"PAIS_ORIGEN"}, 3),
    DIVERSIDAD("Diversidad", new String[]{"DIVERSIDAD"}, 4),
    EDAD("Edad", new String[]{"EDAD"}, 5),
    GENERO("Género", new String[]{"GENERO"}, 6),

    // 2
    LUGAR_TIPO_POBLACION(new String[]{"LUGAR", "TIPO_POBLACION"}, 7),
    LUGAR_PAIS_ORIGEN(new String[]{"LUGAR", "PAIS_ORIGEN"}, 8),
    LUGAR_DIVERSIDAD(new String[]{"LUGAR", "DIVERSIDAD"}, 9),
    LUGAR_EDAD(new String[]{"LUGAR", "EDAD"}, 10),
    LUGAR_GENERO(new String[]{"LUGAR", "GENERO"}, 11),
    TIPO_POBLACION_PAIS_ORIGEN(new String[]{"TIPO_POBLACION", "PAIS_ORIGEN"}, 12),
    TIPO_POBLACION_DIVERSIDAD(new String[]{"TIPO_POBLACION", "DIVERSIDAD"}, 13),
    TIPO_POBLACION_EDAD(new String[]{"TIPO_POBLACION", "EDAD"}, 14),
    TIPO_POBLACION_GENERO(new String[]{"TIPO_POBLACION", "GENERO"}, 15),
    PAIS_ORIGEN_DIVERSIDAD(new String[]{"PAIS_ORIGEN", "DIVERSIDAD"}, 16),
    PAIS_ORIGEN_EDAD(new String[]{"PAIS_ORIGEN", "EDAD"}, 17),
    PAIS_ORIGEN_GENERO(new String[]{"PAIS_ORIGEN", "GENERO"}, 18),
    DIVERSIDAD_EDAD(new String[]{"DIVERSIDAD", "EDAD"}, 19),
    DIVERSIDAD_GENERO(new String[]{"DIVERSIDAD", "GENERO"}, 20),
    EDAD_GENERO(new String[]{"EDAD", "GENERO"}, 21),

    //3
    LUGAR_TIPO_POBLACION_PAIS_ORIGEN(new String[]{"LUGAR", "TIPO_POBLACION", "PAIS_ORIGEN"}, 22),
    LUGAR_TIPO_POBLACION_DIVERSIDAD(new String[]{"LUGAR", "TIPO_POBLACION", "DIVERSIDAD"}, 23),
    LUGAR_TIPO_POBLACION_EDAD(new String[]{"LUGAR", "TIPO_POBLACION", "EDAD"}, 24),
    LUGAR_TIPO_POBLACION_GENERO(new String[]{"LUGAR", "TIPO_POBLACION", "GENERO"}, 25),
    LUGAR_PAIS_ORIGEN_DIVERSIDAD(new String[]{"LUGAR", "PAIS_ORIGEN", "DIVERSIDAD"}, 26),
    LUGAR_PAIS_ORIGEN_EDAD(new String[]{"LUGAR", "PAIS_ORIGEN", "EDAD"}, 27),
    LUGAR_PAIS_ORIGEN_GENERO(new String[]{"LUGAR", "PAIS_ORIGEN", "GENERO"}, 28),
    LUGAR_DIVERSIDAD_EDAD(new String[]{"LUGAR", "DIVERSIDAD", "EDAD"}, 29),
    LUGAR_DIVERSIDAD_GENERO(new String[]{"LUGAR", "DIVERSIDAD", "GENERO"}, 30),
    LUGAR_EDAD_GENERO(new String[]{"LUGAR", "EDAD", "GENERO"}, 31),
    TIPO_POBLACION_PAIS_ORIGEN_DIVERSIDAD(new String[]{"TIPO_POBLACION", "PAIS_ORIGEN", "DIVERSIDAD"}, 32),
    TIPO_POBLACION_PAIS_ORIGEN_EDAD(new String[]{"TIPO_POBLACION", "PAIS_ORIGEN", "EDAD"}, 33),
    TIPO_POBLACION_PAIS_ORIGEN_GENERO(new String[]{"TIPO_POBLACION", "PAIS_ORIGEN", "GENERO"}, 34),
    TIPO_POBLACION_DIVERSIDAD_EDAD(new String[]{"TIPO_POBLACION", "DIVERSIDAD", "EDAD"}, 35),
    TIPO_POBLACION_DIVERSIDAD_GENERO(new String[]{"TIPO_POBLACION", "DIVERSIDAD", "GENERO"}, 36),
    TIPO_POBLACION_EDAD_GENERO(new String[]{"TIPO_POBLACION", "EDAD", "GENERO"}, 37),
    PAIS_ORIGEN_DIVERSIDAD_EDAD(new String[]{"PAIS_ORIGEN", "DIVERSIDAD", "EDAD"}, 38),
    PAIS_ORIGEN_DIVERSIDAD_GENERO(new String[]{"PAIS_ORIGEN", "DIVERSIDAD", "GENERO"}, 39),
    PAIS_ORIGEN_EDAD_GENERO(new String[]{"PAIS_ORIGEN", "EDAD", "GENERO"}, 40),
    DIVERSIDAD_EDAD_GENERO(new String[]{"DIVERSIDAD", "EDAD", "GENERO"}, 41),
    // 4
    LUGAR_TIPO_POBLACION_PAIS_ORIGEN_DIVERSIDAD(new String[]{"LUGAR", "TIPO_POBLACION", "PAIS_ORIGEN", "DIVERSIDAD"}, 42),
    LUGAR_TIPO_POBLACION_PAIS_ORIGEN_EDAD(new String[]{"LUGAR", "TIPO_POBLACION", "PAIS_ORIGEN", "EDAD"}, 43),
    LUGAR_TIPO_POBLACION_PAIS_ORIGEN_GENERO(new String[]{"LUGAR", "TIPO_POBLACION", "PAIS_ORIGEN", "GENERO"}, 44),
    LUGAR_TIPO_POBLACION_DIVERSIDAD_EDAD(new String[]{"LUGAR", "TIPO_POBLACION", "DIVERSIDAD", "EDAD"}, 45),
    LUGAR_TIPO_POBLACION_DIVERSIDAD_GENERO(new String[]{"LUGAR", "TIPO_POBLACION", "DIVERSIDAD", "GENERO"}, 46),
    LUGAR_TIPO_POBLACION_EDAD_GENERO(new String[]{"LUGAR", "TIPO_POBLACION", "EDAD", "GENERO"}, 47),
    LUGAR_PAIS_ORIGEN_DIVERSIDAD_EDAD(new String[]{"LUGAR", "PAIS_ORIGEN", "DIVERSIDAD", "EDAD"}, 48),
    LUGAR_PAIS_ORIGEN_DIVERSIDAD_GENERO(new String[]{"LUGAR", "PAIS_ORIGEN", "DIVERSIDAD", "GENERO"}, 49),
    LUGAR_PAIS_ORIGEN_EDAD_GENERO(new String[]{"LUGAR", "PAIS_ORIGEN", "EDAD", "GENERO"}, 50),
    LUGAR_DIVERSIDAD_EDAD_GENERO(new String[]{"LUGAR", "DIVERSIDAD", "EDAD", "GENERO"}, 51),
    TIPO_POBLACION_PAIS_ORIGEN_DIVERSIDAD_EDAD(new String[]{"TIPO_POBLACION", "PAIS_ORIGEN", "DIVERSIDAD", "EDAD"}, 52),
    TIPO_POBLACION_PAIS_ORIGEN_DIVERSIDAD_GENERO(new String[]{"TIPO_POBLACION", "PAIS_ORIGEN", "DIVERSIDAD", "GENERO"}, 53),
    TIPO_POBLACION_PAIS_ORIGEN_EDAD_GENERO(new String[]{"TIPO_POBLACION", "PAIS_ORIGEN", "EDAD", "GENERO"}, 54),
    TIPO_POBLACION_DIVERSIDAD_EDAD_GENERO(new String[]{"TIPO_POBLACION", "DIVERSIDAD", "EDAD", "GENERO"}, 55),
    PAIS_ORIGEN_DIVERSIDAD_EDAD_GENERO(new String[]{"PAIS_ORIGEN", "DIVERSIDAD", "EDAD", "GENERO"}, 56),
    // 5
    LUGAR_TIPO_POBLACION_PAIS_ORIGEN_DIVERSIDAD_EDAD(new String[]{"LUGAR", "TIPO_POBLACION", "PAIS_ORIGEN", "DIVERSIDAD", "EDAD"}, 57),
    LUGAR_TIPO_POBLACION_PAIS_ORIGEN_DIVERSIDAD_GENERO(new String[]{"LUGAR", "TIPO_POBLACION", "PAIS_ORIGEN", "DIVERSIDAD", "GENERO"}, 58),
    LUGAR_TIPO_POBLACION_PAIS_ORIGEN_EDAD_GENERO(new String[]{"LUGAR", "TIPO_POBLACION", "PAIS_ORIGEN", "EDAD", "GENERO"}, 59),
    LUGAR_TIPO_POBLACION_DIVERSIDAD_EDAD_GENERO(new String[]{"LUGAR", "TIPO_POBLACION", "DIVERSIDAD", "EDAD", "GENERO"}, 60),
    LUGAR_PAIS_ORIGEN_DIVERSIDAD_EDAD_GENERO(new String[]{"LUGAR", "PAIS_ORIGEN", "DIVERSIDAD", "EDAD", "GENERO"}, 61),
    TIPO_POBLACION_PAIS_ORIGEN_DIVERSIDAD_EDAD_GENERO(new String[]{"TIPO_POBLACION", "PAIS_ORIGEN", "DIVERSIDAD", "EDAD", "GENERO"}, 62),

    // 6
    LUGAR_TIPO_POBLACION_PAIS_ORIGEN_DIVERSIDAD_EDAD_GENERO(new String[]{"LUGAR", "TIPO_POBLACION", "PAIS_ORIGEN", "DIVERSIDAD", "EDAD", "GENERO"}, 63),
    ;


    private final int order;

    private String label = null;

    private final String[] standardDissagregationTypes;

    DissagregationType(String[] standardDissagregationTypes, int order) {
        this.standardDissagregationTypes = standardDissagregationTypes;
        this.order = order;
    }

    DissagregationType(String label, String[] standardDissagregationTypes, int order) {
        this.standardDissagregationTypes = standardDissagregationTypes;
        this.order = order;
        this.label = label;
    }

    @Override
    public String getStringValue() {
        return this.name();
    }

    public String createLabel() {
        StringBuilder label = new StringBuilder();
        List<DissagregationType> list= this.getSimpleDissagregations();
        for (int i = 0; i < list.size() - 1; i++) {

            label.append(list.get(i).getLabel()).append(", ");
        }
        DissagregationType enumeLast = list.get(list.size()-1);
        // Add the last element with "and"
        label.append("y ").append(enumeLast.getLabel());
        System.out.println("Generando label: " + label.toString());
        return label.toString();
    }

    public List<DissagregationType> getSimpleDissagregations() {
        return Arrays.stream(this.standardDissagregationTypes).map(s -> Enum.valueOf(DissagregationType.class, s))
                .sorted((o1, o2) -> o1.getOrder() - o2.getOrder()).collect(Collectors.toList());

    }

    @Override
    public int getOrder() {
        return this.order;
    }

    public String[] getStandardDissagregationTypes() {
        return standardDissagregationTypes;
    }

    public Integer getNumberOfDissagregationTypes() {
        return this.standardDissagregationTypes.length;
    }

    @Override
    public String getLabel() {
        if (this.label == null) {
            this.label = this.createLabel();
        }
        return this.label;
    }
}