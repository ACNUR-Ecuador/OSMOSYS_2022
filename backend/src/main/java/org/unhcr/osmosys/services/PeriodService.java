package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.PeriodDao;
import org.unhcr.osmosys.model.Area;
import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.webServices.model.PeriodWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class PeriodService {

    @Inject
    PeriodDao periodDao;

    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(PeriodService.class);

    public Period getById(Long id) {
        return this.periodDao.find(id);
    }

    public Period saveOrUpdate(Period period) {
        if (period.getId() == null) {
            this.periodDao.save(period);
        } else {
            this.periodDao.update(period);
        }
        return period;
    }

    public Long save(PeriodWeb periodWeb) throws GeneralAppException {
        if (periodWeb == null) {
            throw new GeneralAppException("No se puede guardar un period null", Response.Status.BAD_REQUEST);
        }
        if (periodWeb.getId() != null) {
            throw new GeneralAppException("No se puede crear un period con id", Response.Status.BAD_REQUEST);
        }
        this.validate(periodWeb);
        Period period = this.saveOrUpdate(this.periodToPeriodWeb(periodWeb));
        return period.getId();
    }

    public List<PeriodWeb> getAll() {
        List<PeriodWeb> r = new ArrayList<>();
        return this.periodsToPeriodsWeb(this.periodDao.findAll());
    }

    public List<PeriodWeb> getByState(State state) {
        List<PeriodWeb> r = new ArrayList<>();
        return this.periodsToPeriodsWeb(this.periodDao.getByState(state));
    }

    public Long update(PeriodWeb periodWeb) throws GeneralAppException {
        if (periodWeb == null) {
            throw new GeneralAppException("No se puede actualizar un period null", Response.Status.BAD_REQUEST);
        }
        if (periodWeb.getId() == null) {
            throw new GeneralAppException("No se puede crear un period sin id", Response.Status.BAD_REQUEST);
        }
        this.validate(periodWeb);
        Period period = this.saveOrUpdate(this.periodToPeriodWeb(periodWeb));
        return period.getId();
    }

    public List<PeriodWeb> periodsToPeriodsWeb(List<Period> periods) {
        List<PeriodWeb> r = new ArrayList<>();
        for (Period period : periods) {
            r.add(this.periodToPeriodWeg(period));
        }
        return r;
    }

    public PeriodWeb periodToPeriodWeg(Period period) {
        if (period == null) {
            return null;
        }
        PeriodWeb periodWeb = new PeriodWeb();
        periodWeb.setId(period.getId());
        periodWeb.setYear(period.getYear());
        periodWeb.setState(period.getState());

        return periodWeb;
    }

    public List<Period> periodsWebToPeriods(List<PeriodWeb> periodsWebs) {
        List<Period> r = new ArrayList<>();
        for (PeriodWeb periodWeb : periodsWebs) {
            r.add(this.periodToPeriodWeb(periodWeb));
        }
        return r;
    }

    public Period periodToPeriodWeb(PeriodWeb periodWeb) {
        if (periodWeb == null) {
            return null;
        }
        Period period = new Period();
        period.setId(periodWeb.getId());
        period.setYear(periodWeb.getYear());
        period.setState(periodWeb.getState());
        return period;
    }

    public void validate(PeriodWeb periodWeb) throws GeneralAppException {
        if (periodWeb == null) {
            throw new GeneralAppException("Periodo es nulo", Response.Status.BAD_REQUEST);
        }

        if (periodWeb.getYear() == null) {
            throw new GeneralAppException("Año no válido", Response.Status.BAD_REQUEST);
        }
        if (periodWeb.getState() == null) {
            throw new GeneralAppException("Estádo no válido", Response.Status.BAD_REQUEST);
        }

        Period itemRecovered = this.periodDao.getByYear(periodWeb.getYear());
        if (itemRecovered != null) {
            if (periodWeb.getId() == null || !periodWeb.getId().equals(itemRecovered)){
                throw new GeneralAppException("Ya existe un ítem con este año", Response.Status.BAD_REQUEST);
            }
        }
    }
}
