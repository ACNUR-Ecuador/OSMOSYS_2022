package org.unhcr.osmosys.model.enums;

import java.io.Serializable;

public interface EnumInterface extends Serializable {

    public abstract String getStringValue();

    public abstract String getLabel();

    public abstract int getOrder();

}