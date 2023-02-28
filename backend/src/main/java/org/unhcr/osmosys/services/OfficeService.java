package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.model.User;
import com.sagatechs.generics.security.servicio.UserService;
import com.sagatechs.generics.webservice.webModel.UserWeb;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.OfficeDao;
import org.unhcr.osmosys.model.Office;
import org.unhcr.osmosys.model.OfficeAdministrator;
import org.unhcr.osmosys.webServices.model.OfficeWeb;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Stateless
public class OfficeService {

    @Inject
    OfficeDao officeDao;

    @Inject
    ModelWebTransformationService modelWebTransformationService;

    @Inject
    UserService userService;


    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(OfficeService.class);

    public Office getById(Long id) {
        return this.officeDao.find(id);
    }

    public Office saveOrUpdate(Office office) {
        if (office.getId() == null) {
            this.officeDao.save(office);
        } else {
            this.officeDao.update(office);
        }
        return office;
    }

    public Long save(OfficeWeb officeWeb) throws GeneralAppException {
        if (officeWeb == null) {
            throw new GeneralAppException("No se puede guardar un pilar null", Response.Status.BAD_REQUEST);
        }
        if (officeWeb.getId() != null) {
            throw new GeneralAppException("No se puede crear un pilar con id", Response.Status.BAD_REQUEST);
        }
        this.validate(officeWeb);





        Office office = this.saveOrUpdate(this.modelWebTransformationService.officeWebToOffice(officeWeb));
        List<UserWeb> userAdministratorsWebs = officeWeb.getAdministrators();

        updateAdministratorsOffice(office, userAdministratorsWebs);

        return office.getId();
    }

    private void updateAdministratorsOffice(Office office, List<UserWeb> userAdministratorsWebs) {
        for (UserWeb userAdministratorWeb : userAdministratorsWebs) {
            Optional<OfficeAdministrator> optionalAdministrator = office.getOfficeAdministrators().stream().filter(officeAdministrator -> officeAdministrator.getAdministrator().getId().equals(userAdministratorWeb.getId())).findFirst();
            if(optionalAdministrator.isPresent()){
                optionalAdministrator.get().setState(State.ACTIVO);
            }else {
                User administrator = this.userService.getById(userAdministratorWeb.getId());
                office.addAdministrator(administrator);
            }
        }

        for (OfficeAdministrator officeAdministrator : office.getOfficeAdministrators()) {
            Optional<UserWeb> userAdministratorsWebOptional = userAdministratorsWebs.stream()
                    .filter(userWeb -> userWeb.getId().equals(officeAdministrator.getAdministrator().getId()))
                    .findFirst();
            if(!userAdministratorsWebOptional.isPresent()){
                officeAdministrator.setState(State.INACTIVO);
            }
        }
        this.saveOrUpdate(office);
    }

    public List<OfficeWeb> getAll(boolean returnChilds) {
        return this.modelWebTransformationService.officesToOfficesWeb(this.officeDao.findAll(), returnChilds,true);
    }

    public List<OfficeWeb> getByState(State state, boolean returnChilds) {
        return this.modelWebTransformationService.officesToOfficesWeb(this.officeDao.getByState(state), returnChilds,false);
    }

    public Long update(OfficeWeb officeWeb) throws GeneralAppException {
        if (officeWeb == null) {
            throw new GeneralAppException("No se puede actualizar un office null", Response.Status.BAD_REQUEST);
        }
        if (officeWeb.getId() == null) {
            throw new GeneralAppException("No se puede crear un office sin id", Response.Status.BAD_REQUEST);
        }
        this.validate(officeWeb);
        Office office = this.saveOrUpdate(this.modelWebTransformationService.officeWebToOffice(officeWeb));
        List<UserWeb> userAdministratorsWebs = officeWeb.getAdministrators();

        updateAdministratorsOffice(office, userAdministratorsWebs);
        return office.getId();
    }


    public void validate(OfficeWeb officeWeb) throws GeneralAppException {
        if (officeWeb == null) {
            throw new GeneralAppException("Pilar es nulo", Response.Status.BAD_REQUEST);
        }

        if (officeWeb.getDescription() == null) {
            throw new GeneralAppException("Descripción no válido", Response.Status.BAD_REQUEST);
        }
        if (officeWeb.getAcronym() == null) {
            throw new GeneralAppException("Acrónimo no válido", Response.Status.BAD_REQUEST);
        }
        if (officeWeb.getState() == null) {
            throw new GeneralAppException("Estádo no válido", Response.Status.BAD_REQUEST);
        }
        if (officeWeb.getType() == null) {
            throw new GeneralAppException("Tipo no válido", Response.Status.BAD_REQUEST);
        }

        Office itemRecovered = this.officeDao.getByAcronym(officeWeb.getAcronym());
        if (itemRecovered != null) {
            if (officeWeb.getId() == null || !officeWeb.getId().equals(itemRecovered.getId())) {
                throw new GeneralAppException("Ya existe un ítem con este acrónimo", Response.Status.BAD_REQUEST);
            }
        }

        itemRecovered = this.officeDao.getByDescription(officeWeb.getDescription());
        if (itemRecovered != null) {
            if (officeWeb.getId() == null || !officeWeb.getId().equals(itemRecovered.getId())) {
                throw new GeneralAppException("Ya existe un ítem con esta descripción", Response.Status.BAD_REQUEST);
            }
        }
    }

    public List<OfficeWeb> getOfficeTree() {
        List<Office> offices = this.officeDao.getByNoParent();
        return this.modelWebTransformationService.officesToOfficesWeb(offices, true, true);
    }


    public List<OfficeWeb> getReportingOfficeByPeriodId(Long periodId) {
        List<Office> offices = this.officeDao.getReportingOfficeByPeriodId(periodId);
        return this.modelWebTransformationService.officesToOfficesWeb(offices, false,false);
    }
}
