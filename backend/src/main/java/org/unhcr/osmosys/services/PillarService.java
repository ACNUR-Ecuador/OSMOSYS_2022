package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.PillarDao;
import org.unhcr.osmosys.model.Pillar;
import org.unhcr.osmosys.webServices.model.PillarWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class PillarService {

    @Inject
    PillarDao pillarDao;

    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(PillarService.class);

    public Pillar getById(Long id) {
        return this.pillarDao.find(id);
    }

    public Pillar saveOrUpdate(Pillar pillar) {
        if (pillar.getId() == null) {
            this.pillarDao.save(pillar);
        } else {
            this.pillarDao.update(pillar);
        }
        return pillar;
    }

    public Long save(PillarWeb pillarWeb) throws GeneralAppException {
        if (pillarWeb == null) {
            throw new GeneralAppException("No se puede guardar un pilar null", Response.Status.BAD_REQUEST);
        }
        if (pillarWeb.getId() != null) {
            throw new GeneralAppException("No se puede crear un pilar con id", Response.Status.BAD_REQUEST);
        }
        this.validate(pillarWeb);
        Pillar pillar = this.saveOrUpdate(this.pillarWebToPillar(pillarWeb));
        return pillar.getId();
    }

    public List<PillarWeb> getAll() {
        List<PillarWeb> r = new ArrayList<>();
        return this.pillarsToPillarsWeb(this.pillarDao.findAll());
    }

    public List<PillarWeb> getByState(State state) {
        List<PillarWeb> r = new ArrayList<>();
        return this.pillarsToPillarsWeb(this.pillarDao.getByState(state));
    }

    public Long update(PillarWeb pillarWeb) throws GeneralAppException {
        if (pillarWeb == null) {
            throw new GeneralAppException("No se puede actualizar un pillar null", Response.Status.BAD_REQUEST);
        }
        if (pillarWeb.getId() == null) {
            throw new GeneralAppException("No se puede crear un pillar sin id", Response.Status.BAD_REQUEST);
        }
        this.validate(pillarWeb);
        Pillar pillar = this.saveOrUpdate(this.pillarWebToPillar(pillarWeb));
        return pillar.getId();
    }

    public List<PillarWeb> pillarsToPillarsWeb(List<Pillar> pillars) {
        List<PillarWeb> r = new ArrayList<>();
        for (Pillar pillar : pillars) {
            r.add(this.pillarToPillarWeb(pillar));
        }
        return r;
    }

    public PillarWeb pillarToPillarWeb(Pillar pillar) {
        if (pillar == null) {
            return null;
        }
        PillarWeb pillarWeb = new PillarWeb();
        pillarWeb.setId(pillar.getId());
        pillarWeb.setShortDescription(pillar.getShortDescription());
        pillarWeb.setCode(pillar.getCode());
        pillarWeb.setDescription(pillar.getDescription());
        pillarWeb.setState(pillar.getState());

        return pillarWeb;
    }

    public List<Pillar> pillarsWebToPillars(List<PillarWeb> pillarsWebs) {
        List<Pillar> r = new ArrayList<>();
        for (PillarWeb pillarWeb : pillarsWebs) {
            r.add(this.pillarWebToPillar(pillarWeb));
        }
        return r;
    }

    public Pillar pillarWebToPillar(PillarWeb pillarWeb) {
        if (pillarWeb == null) {
            return null;
        }
        Pillar pillar = new Pillar();
        pillar.setId(pillarWeb.getId());
        pillar.setCode(pillarWeb.getCode());
        pillar.setShortDescription(pillarWeb.getShortDescription());
        pillar.setDescription(pillarWeb.getDescription());
        pillar.setState(pillarWeb.getState());
        return pillar;
    }

    public void validate(PillarWeb pillarWeb) throws GeneralAppException {
        if (pillarWeb == null) {
            throw new GeneralAppException("Pilar es nulo", Response.Status.BAD_REQUEST);
        }

        if (pillarWeb.getCode() == null) {
            throw new GeneralAppException("Código no válido", Response.Status.BAD_REQUEST);
        }
        if (pillarWeb.getShortDescription() == null) {
            throw new GeneralAppException("Descripción corta no válida", Response.Status.BAD_REQUEST);
        }
        if (pillarWeb.getState() == null) {
            throw new GeneralAppException("Estádo no válido", Response.Status.BAD_REQUEST);
        }

        Pillar itemRecovered = this.pillarDao.getByCode(pillarWeb.getCode());
        if (itemRecovered != null) {
            if (pillarWeb.getId() == null || !pillarWeb.getId().equals(itemRecovered.getId())){
                throw new GeneralAppException("Ya existe un ítem con este código", Response.Status.BAD_REQUEST);
            }
        }

        itemRecovered = this.pillarDao.getByShortDescription(pillarWeb.getShortDescription());
        if (itemRecovered != null) {
            if (pillarWeb.getId() == null || !pillarWeb.getId().equals(itemRecovered.getId())){
                throw new GeneralAppException("Ya existe un ítem con esta descripción corta", Response.Status.BAD_REQUEST);
            }
        }
    }
}
