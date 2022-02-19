package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.PeriodDao;
import org.unhcr.osmosys.model.Period;
import org.unhcr.osmosys.webServices.model.PeriodWeb;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class PeriodService {

    @Inject
    PeriodDao periodDao;

    @Inject
    ModelWebTransformationService modelWebTransformationService;

    @Inject
    GeneralIndicatorService generalIndicatorService;

    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(PeriodService.class);

    public Period find(Long id) {
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
        Period period=this.modelWebTransformationService.periodWebToPeriod(periodWeb);
        this.saveOrUpdate(period);
        if(periodWeb.getGeneralIndicator() !=null){
            this.generalIndicatorService.validate(periodWeb.getGeneralIndicator());
            period.setGeneralIndicator(this.modelWebTransformationService.generalIndicatorWebToGeneralIndicator(periodWeb.getGeneralIndicator()));
            period.getGeneralIndicator().setPeriod(period);
            this.generalIndicatorService.saveOrUpdate(period.getGeneralIndicator());
        }


        return period.getId();
    }

    public List<PeriodWeb> getAll() {
        return this.modelWebTransformationService.periodsToPeriodsWeb(this.periodDao.findAll());
    }

    public List<PeriodWeb> getByState(State state) {
        return this.modelWebTransformationService.periodsToPeriodsWeb(this.periodDao.getByState(state));
    }

    public Long update(PeriodWeb periodWeb) throws GeneralAppException {
        if (periodWeb == null) {
            throw new GeneralAppException("No se puede actualizar un period null", Response.Status.BAD_REQUEST);
        }
        if (periodWeb.getId() == null) {
            throw new GeneralAppException("No se puede crear un period sin id", Response.Status.BAD_REQUEST);
        }

        Period period=this.modelWebTransformationService.periodWebToPeriod(periodWeb);
        this.validate(periodWeb);
        this.saveOrUpdate(period);
        if(periodWeb.getGeneralIndicator() !=null){
            this.generalIndicatorService.validate(periodWeb.getGeneralIndicator());
            if(periodWeb.getGeneralIndicator().getId()!=null){
                periodWeb.getGeneralIndicator().setPeriod(new PeriodWeb());
                periodWeb.getGeneralIndicator().getPeriod().setId(period.getId());
                this.generalIndicatorService.update(periodWeb.getGeneralIndicator());
            }else {
                period.setGeneralIndicator(this.modelWebTransformationService.generalIndicatorWebToGeneralIndicator(periodWeb.getGeneralIndicator()));
                period.getGeneralIndicator().setPeriod(period);
                periodWeb.getGeneralIndicator().setPeriod(new PeriodWeb());
                periodWeb.getGeneralIndicator().getPeriod().setId(period.getId());
                this.generalIndicatorService.saveOrUpdate(this.modelWebTransformationService.generalIndicatorWebToGeneralIndicator(periodWeb.getGeneralIndicator()));
            }




        }
        return period.getId();
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
            if (periodWeb.getId() == null || !periodWeb.getId().equals(itemRecovered.getId())) {
                throw new GeneralAppException("Ya existe un ítem con este año", Response.Status.BAD_REQUEST);
            }
        }
    }

    public PeriodWeb getByid(Long id) {
        return this.modelWebTransformationService.periodToPeriodWeb(this.periodDao.find(id));
    }

    public PeriodWeb getWebWithGeneralIndicatorById(Long id) {

        Period period = this.periodDao.getWithGeneralIndicatorById(id);
        PeriodWeb periodWeb = this.modelWebTransformationService.periodToPeriodWeb(period);

        if (period.getGeneralIndicator() != null) {
            periodWeb.setGeneralIndicator(this.modelWebTransformationService.generalIndicatorToGeneralIndicatorWeb(period.getGeneralIndicator()));
        }
        return periodWeb;
    }

    public Period getWithGeneralIndicatorById(Long id) {

        return this.periodDao.getWithGeneralIndicatorById(id);

    }


    public List<PeriodWeb> getWithGeneralIndicatorAll() {
        List<Period> periods =this.periodDao.getWithGeneralIndicatorAll();

        List<PeriodWeb> r = new ArrayList<>();

        for (Period period : periods) {
            PeriodWeb periodWeb = this.modelWebTransformationService.periodToPeriodWeb(period);
            if (period.getGeneralIndicator() != null) {
                periodWeb.setGeneralIndicator(this.modelWebTransformationService.generalIndicatorToGeneralIndicatorWeb(period.getGeneralIndicator()));
            }
            r.add(periodWeb);
        }
        return r;
    }
}
