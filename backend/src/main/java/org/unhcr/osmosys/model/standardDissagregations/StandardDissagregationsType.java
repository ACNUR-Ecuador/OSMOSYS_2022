package org.unhcr.osmosys.model.standardDissagregations;

import org.unhcr.osmosys.model.enums.EnumInterface;

public enum StandardDissagregationsType implements EnumInterface {
    LUGAR("Lugar", 1),
    TIPO_POBLACION("Tipo de población", 2),
    DIVERSIDAD("Diversidad", 3),
    PAIS_ORIGEN("País de Origen", 4),
    EDAD("Edad", 5),
    GENERO("Género", 6);


    private String label;
    private final int order;

    StandardDissagregationsType(String label, int order) {
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