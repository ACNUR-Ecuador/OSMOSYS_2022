package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.SituationDao;
import org.unhcr.osmosys.model.Situation;
import org.unhcr.osmosys.webServices.model.SituationWeb;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.List;

@Stateless
public class SituationService {

    @Inject
    SituationDao situationDao;

    @Inject
    ModelWebTransformationService modelWebTransformationService;

    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(SituationService.class);

    public Situation getById(Long id) {
        return this.situationDao.find(id);
    }

    public Situation saveOrUpdate(Situation situation) {
        if (situation.getId() == null) {
            this.situationDao.save(situation);
        } else {
            this.situationDao.update(situation);
        }
        return situation;
    }

    public Long save(SituationWeb situationWeb) throws GeneralAppException {
        if (situationWeb == null) {
            throw new GeneralAppException("No se puede guardar un situation null", Response.Status.BAD_REQUEST);
        }
        if (situationWeb.getId() != null) {
            throw new GeneralAppException("No se puede crear un situation con id", Response.Status.BAD_REQUEST);
        }
        this.validate(situationWeb);
        Situation situation = this.saveOrUpdate(this.modelWebTransformationService.situationWebToSituation(situationWeb));
        return situation.getId();
    }

    public List<SituationWeb> getAll() {
        return this.modelWebTransformationService.situationsToSituationsWeb(new HashSet<>(this.situationDao.findAll()));
    }

    public List<SituationWeb> getByState(State state) {
        return this.modelWebTransformationService.situationsToSituationsWeb(new HashSet<>(this.situationDao.getByState(state)));
    }

    public Long update(SituationWeb situationWeb) throws GeneralAppException {
        if (situationWeb == null) {
            throw new GeneralAppException("No se puede actualizar un situation null", Response.Status.BAD_REQUEST);
        }
        if (situationWeb.getId() == null) {
            throw new GeneralAppException("No se puede crear un situation sin id", Response.Status.BAD_REQUEST);
        }
        this.validate(situationWeb);
        Situation situation = this.saveOrUpdate(this.modelWebTransformationService.situationWebToSituation(situationWeb));
        return situation.getId();
    }


    public void validate(SituationWeb situationWeb) throws GeneralAppException {
        if (situationWeb == null) {
            throw new GeneralAppException("Situación es nulo", Response.Status.BAD_REQUEST);
        }

        if (StringUtils.isBlank(situationWeb.getCode())) {
            throw new GeneralAppException("Código no válido", Response.Status.BAD_REQUEST);
        }
        if (StringUtils.isBlank(situationWeb.getShortDescription())) {
            throw new GeneralAppException("Descripción corta no válida", Response.Status.BAD_REQUEST);
        }
        if (StringUtils.isBlank(situationWeb.getDescription())) {
            throw new GeneralAppException("Descripción no válida", Response.Status.BAD_REQUEST);
        }
        if (situationWeb.getState() == null) {
            throw new GeneralAppException("Estádo no válido", Response.Status.BAD_REQUEST);
        }

        Situation itemRecovered = this.situationDao.getByCode(situationWeb.getCode());
        if (itemRecovered != null) {
            if (situationWeb.getId() == null || !situationWeb.getId().equals(itemRecovered.getId())){
                throw new GeneralAppException("Ya existe un ítem con este código", Response.Status.BAD_REQUEST);
            }
        }

        itemRecovered = this.situationDao.getByShortDescription(situationWeb.getShortDescription());
        if (itemRecovered != null) {
            if (situationWeb.getId() == null || !situationWeb.getId().equals(itemRecovered.getId())){
                throw new GeneralAppException("Ya existe un ítem con esta descripción corta", Response.Status.BAD_REQUEST);
            }
        }
        itemRecovered = this.situationDao.getByDescription(situationWeb.getDescription());
        if (itemRecovered != null) {
            if (situationWeb.getId() == null || !situationWeb.getId().equals(itemRecovered.getId())){
                throw new GeneralAppException("Ya existe un ítem con esta descripción", Response.Status.BAD_REQUEST);
            }
        }

    }
}
