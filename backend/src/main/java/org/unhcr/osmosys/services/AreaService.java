package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.AreaDao;
import org.unhcr.osmosys.model.Area;
import org.unhcr.osmosys.webServices.model.AreaResumeWeb;
import org.unhcr.osmosys.webServices.model.AreaWeb;
import org.unhcr.osmosys.webServices.model.IndicatorExecutionWeb;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;

@Stateless
public class AreaService {

    @Inject
    AreaDao areaDao;

    @Inject
    IndicatorExecutionService indicatorExecutionService;

    @Inject
    ModelWebTransformationService modelWebTransformationService;


    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(AreaService.class);

    public Area getById(Long id) {
        return this.areaDao.find(id);
    }

    public Area getByCode(String code) throws GeneralAppException {
        return this.areaDao.getByCode(code);
    }

    public Area saveOrUpdate(Area area) {
        if (area.getId() == null) {
            this.areaDao.save(area);
        } else {
            this.areaDao.update(area);
        }
        return area;
    }

    public Long save(AreaWeb areaWeb) throws GeneralAppException {
        if (areaWeb == null) {
            throw new GeneralAppException("No se puede guardar un area null", Response.Status.BAD_REQUEST);
        }
        if (areaWeb.getId() != null) {
            throw new GeneralAppException("No se puede crear un area con id", Response.Status.BAD_REQUEST);
        }
        this.validate(areaWeb);
        Area area = this.saveOrUpdate(this.modelWebTransformationService.areaToAreaWeb(areaWeb));
        return area.getId();
    }

    public List<AreaWeb> getAll() {
        return this.modelWebTransformationService.areasToAreasWeb(this.areaDao.findAll());
    }

    public List<AreaWeb> getByState(State state) {
        return this.modelWebTransformationService.areasToAreasWeb(this.areaDao.getByState(state));
    }

    public Long update(AreaWeb areaWeb) throws GeneralAppException {
        if (areaWeb == null) {
            throw new GeneralAppException("No se puede actualizar un area null", Response.Status.BAD_REQUEST);
        }
        if (areaWeb.getId() == null) {
            throw new GeneralAppException("No se puede crear un area sin id", Response.Status.BAD_REQUEST);
        }
        this.validate(areaWeb);
        Area area = this.saveOrUpdate(this.modelWebTransformationService.areaToAreaWeb(areaWeb));
        return area.getId();
    }


    public void validate(AreaWeb areaWeb) throws GeneralAppException {
        if (areaWeb == null) {
            throw new GeneralAppException("Oficina es nulo", Response.Status.BAD_REQUEST);
        }

        if (StringUtils.isBlank(areaWeb.getCode())) {
            throw new GeneralAppException("Código no válido", Response.Status.BAD_REQUEST);
        }
        if (StringUtils.isBlank(areaWeb.getShortDescription())) {
            throw new GeneralAppException("Descripción no válida", Response.Status.BAD_REQUEST);
        }
        if (StringUtils.isBlank(areaWeb.getDescription())) {
            throw new GeneralAppException("Descripción no válida", Response.Status.BAD_REQUEST);
        }
        if (areaWeb.getState() == null) {
            throw new GeneralAppException("Estádo no válido", Response.Status.BAD_REQUEST);
        }
        if (areaWeb.getAreaType() == null) {
            throw new GeneralAppException("Tipo no válida", Response.Status.BAD_REQUEST);
        }

        Area itemRecovered = this.areaDao.getByCode(areaWeb.getCode());
        if (itemRecovered != null) {
            if (areaWeb.getId() == null || !areaWeb.getId().equals(itemRecovered.getId())) {
                throw new GeneralAppException("Ya existe un área con este código", Response.Status.BAD_REQUEST);
            }
        }

        itemRecovered = this.areaDao.getByShortDescription(areaWeb.getShortDescription());
        if (itemRecovered != null) {
            if (areaWeb.getId() == null || !areaWeb.getId().equals(itemRecovered.getId())) {
                throw new GeneralAppException("Ya existe un área con esta descripción corta", Response.Status.BAD_REQUEST);
            }
        }

    }

    public List<AreaResumeWeb> getDirectImplementationAreaResume(Long userId,
                                                                 Long periodId,
                                                                 Long officeId,
                                                                 Boolean supervisor,
                                                                 Boolean responsible,
                                                                 Boolean backup) throws GeneralAppException {

        List<IndicatorExecutionWeb> indicatorExecutions = this.indicatorExecutionService
                .getDirectImplementationIndicatorByPeriodIdResponsableIdSupervisorIdAndOfficeId(
                        userId, periodId, officeId, supervisor, responsible, backup
                );
        List<Area> areas = this.areaDao.findAll();


        return this.modelWebTransformationService.indicatorExecutionsToAreaWebs(indicatorExecutions, areas);
    }
}
