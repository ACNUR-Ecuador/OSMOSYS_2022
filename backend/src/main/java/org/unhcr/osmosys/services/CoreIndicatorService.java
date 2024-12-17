package org.unhcr.osmosys.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.AuditAction;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.model.User;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.AuditDao;
import org.unhcr.osmosys.daos.CoreIndicatorDao;
import org.unhcr.osmosys.model.Audit;
import org.unhcr.osmosys.model.CoreIndicator;
import org.unhcr.osmosys.model.IndicatorExecutionLocationAssigment;
import org.unhcr.osmosys.model.Project;
import org.unhcr.osmosys.model.auditDTOs.FocalPointAssignationDTO;
import org.unhcr.osmosys.model.auditDTOs.IndicatorExecutionDTO;
import org.unhcr.osmosys.model.auditDTOs.ProjectAuditDTO;
import org.unhcr.osmosys.model.auditDTOs.ProjectLocationAssigmentsDTO;
import org.unhcr.osmosys.model.enums.IndicatorType;
import org.unhcr.osmosys.webServices.model.AuditWeb;
import org.unhcr.osmosys.webServices.model.CoreIndicatorWeb;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
public class CoreIndicatorService {

    @Inject
    ModelWebTransformationService modelWebTransformationService;

    @Inject
    CoreIndicatorDao coreIndicatorDao;

    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(CoreIndicatorService.class);

    public List<CoreIndicatorWeb> getAll() {
        return this.modelWebTransformationService.coreIndicatorsToCoreIndicatorsWeb(this.coreIndicatorDao.findAll());
    }

    public List<CoreIndicatorWeb> getByState(State state) {
        return this.modelWebTransformationService.coreIndicatorsToCoreIndicatorsWeb(this.coreIndicatorDao.getByState(state));
    }
}
