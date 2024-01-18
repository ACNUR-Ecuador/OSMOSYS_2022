package org.unhcr.osmosys.model.standardDissagregations;

import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.enums.DissagregationType;

import java.util.Set;

public interface DissagregationAsignationInterface {
    Long getId();

    State getState();

    DissagregationType getDissagregationType();

    Boolean getUseCustomAgeDissagregations();

    Set<DissagregationAssignationToIndicatorPeriodCustomization> getDissagregationAssignationToIndicatorPeriodCustomizations();

}
