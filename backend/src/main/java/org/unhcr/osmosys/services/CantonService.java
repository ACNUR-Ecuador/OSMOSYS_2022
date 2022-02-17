package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.CantonDao;
import org.unhcr.osmosys.model.Canton;
import org.unhcr.osmosys.webServices.model.CantonWeb;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class CantonService {

    @Inject
    CantonDao cantonDao;

    @Inject
    ModelWebTransformationService modelWebTransformationService;

    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(CantonService.class);

    public Canton getById(Long id) {
        return this.cantonDao.find(id);
    }

    public List<CantonWeb> getAll() {
        List<CantonWeb> r = new ArrayList<>();
        return this.modelWebTransformationService.cantonsToCantonsWeb(this.cantonDao.findAll());
    }

    public List<CantonWeb> getByState(State state) {
        List<CantonWeb> r = new ArrayList<>();
        return this.modelWebTransformationService.cantonsToCantonsWeb(this.cantonDao.getByState(state));
    }

    public List<Canton> getByIds(List<Long> ids) {
        return this.cantonDao.getByIds(ids);
    }


}
