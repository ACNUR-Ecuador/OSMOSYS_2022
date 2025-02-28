package org.unhcr.osmosys.model;

import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.enums.DissagregationType;


public interface DissagregationAssignationToIndicatorInterface  {



     Long getId();

     State getState();

     DissagregationType getDissagregationType();


}
