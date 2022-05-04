package org.unhcr.osmosys.model.enums;

import java.io.Serializable;

public interface EnumInterface extends Serializable {

    String getStringValue();

    String getLabel();

    int getOrder();

}