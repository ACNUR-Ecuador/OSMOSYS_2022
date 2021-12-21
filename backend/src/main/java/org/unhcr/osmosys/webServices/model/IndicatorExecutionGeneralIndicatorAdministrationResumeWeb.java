package org.unhcr.osmosys.webServices.model;

import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.model.User;
import org.unhcr.osmosys.model.*;
import org.unhcr.osmosys.model.enums.AreaType;
import org.unhcr.osmosys.model.enums.IndicatorType;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


public class IndicatorExecutionGeneralIndicatorAdministrationResumeWeb implements Serializable {

    private Long id;
    private String commentary;
    private BigDecimal target;

    private String indicatorDescription;
    private IndicatorType indicatorType;

    private State state;

    private BigDecimal totalExecution;

    private Set<Quarter> quarters = new HashSet<>();

    private Set<DissagregationAssignationToIndicatorExecution> dissagregationsAssignationsToIndicatorExecutions = new HashSet<>();

    private Project project;


}
