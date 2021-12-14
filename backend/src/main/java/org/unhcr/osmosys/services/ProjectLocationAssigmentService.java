package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.CantonDao;
import org.unhcr.osmosys.daos.ProjectLocationAssigmentDao;
import org.unhcr.osmosys.model.Canton;
import org.unhcr.osmosys.model.ProjectLocationAssigment;
import org.unhcr.osmosys.webServices.model.CantonWeb;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
public class ProjectLocationAssigmentService {

    @Inject
    ProjectLocationAssigmentDao projectLocationAssigmentDao;
    @Inject
    CantonDao cantonDao;

    @Inject
    ModelWebTransformationService modelWebTransformationService;

    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(ProjectLocationAssigmentService.class);

    public ProjectLocationAssigment getById(Long id) {
        return this.projectLocationAssigmentDao.find(id);
    }

    public ProjectLocationAssigment saveOrUpdate(ProjectLocationAssigment projectLocationAssigment) {
        if (projectLocationAssigment.getId() == null) {
            this.projectLocationAssigmentDao.save(projectLocationAssigment);
        } else {
            this.projectLocationAssigmentDao.update(projectLocationAssigment);
        }
        return projectLocationAssigment;
    }
}
