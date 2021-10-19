package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.SituationDao;
import org.unhcr.osmosys.model.Situation;
import org.unhcr.osmosys.webServices.model.SituationWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class SituationService {

    @Inject
    SituationDao situationDao;

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
        Situation situation = this.saveOrUpdate(this.situationToSituationWeb(situationWeb));
        return situation.getId();
    }

    public List<SituationWeb> getAll() {
        List<SituationWeb> r = new ArrayList<>();
        return this.situationsToSituationsWeb(this.situationDao.findAll());
    }

    public List<SituationWeb> getByState(State state) {
        List<SituationWeb> r = new ArrayList<>();
        return this.situationsToSituationsWeb(this.situationDao.getByState(state));
    }

    public Long update(SituationWeb situationWeb) throws GeneralAppException {
        if (situationWeb == null) {
            throw new GeneralAppException("No se puede actualizar un situation null", Response.Status.BAD_REQUEST);
        }
        if (situationWeb.getId() == null) {
            throw new GeneralAppException("No se puede crear un situation sin id", Response.Status.BAD_REQUEST);
        }
        this.validate(situationWeb);
        Situation situation = this.saveOrUpdate(this.situationToSituationWeb(situationWeb));
        return situation.getId();
    }

    public List<SituationWeb> situationsToSituationsWeb(List<Situation> situations) {
        List<SituationWeb> r = new ArrayList<>();
        for (Situation situation : situations) {
            r.add(this.situationToSituationWeb(situation));
        }
        return r;
    }

    public SituationWeb situationToSituationWeb(Situation situation) {
        if (situation == null) {
            return null;
        }
        SituationWeb situationWeb = new SituationWeb();
        situationWeb.setId(situation.getId());
        situationWeb.setCode(situation.getCode());
        situationWeb.setDescription(situation.getDescription());
        situationWeb.setShortDescription(situation.getShortDescription());
        situationWeb.setState(situation.getState());

        return situationWeb;
    }

    public List<Situation> situationsWebToSituations(List<SituationWeb> situationsWebs) {
        List<Situation> r = new ArrayList<>();
        for (SituationWeb situationWeb : situationsWebs) {
            r.add(this.situationToSituationWeb(situationWeb));
        }
        return r;
    }

    public Situation situationToSituationWeb(SituationWeb situationWeb) {
        if (situationWeb == null) {
            return null;
        }
        Situation situation = new Situation();
        situation.setId(situationWeb.getId());
        situation.setState(situationWeb.getState());
        situation.setDescription(situationWeb.getDescription());
        situation.setCode(situationWeb.getCode());
        situation.setShortDescription(situationWeb.getShortDescription());
        return situation;
    }

    public void validate(SituationWeb situationWeb) throws GeneralAppException {
        if (situationWeb == null) {
            throw new GeneralAppException("Situación es nulo", Response.Status.BAD_REQUEST);
        }

        if (StringUtils.isBlank(situationWeb.getCode())) {
            throw new GeneralAppException("Código no válido", Response.Status.BAD_REQUEST);
        }
        if (StringUtils.isBlank(situationWeb.getShortDescription())) {
            throw new GeneralAppException("Descripción no válida", Response.Status.BAD_REQUEST);
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
