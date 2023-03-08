package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.CantonDao;
import org.unhcr.osmosys.model.Canton;
import org.unhcr.osmosys.webServices.model.CantonWeb;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
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
        return this.modelWebTransformationService.cantonsToCantonsWeb(this.cantonDao.findAll());
    }

    public List<CantonWeb> getByState(State state) {
        return this.modelWebTransformationService.cantonsToCantonsWeb(this.cantonDao.getByState(state));
    }

    public List<Canton> getByIds(List<Long> ids) {
        return this.cantonDao.getByIds(ids);
    }

    public Canton getByCantonDescriptionAndProvinceDescription(String cantonDescription, String provinceDescription) throws GeneralAppException {
        return this.cantonDao.getByCantonDescriptionAndProvinceDescription(cantonDescription, provinceDescription);
    }

    public List<CantonWeb> discoverCantones(List<CantonWeb> cantonWebs) throws GeneralAppException {
        List<CantonWeb> r = new ArrayList<>();
        for (CantonWeb cantonWeb : cantonWebs) {
            Canton can = this.cantonDao.discoverCanton(cantonWeb.getCode(), cantonWeb.getDescription(), cantonWeb.getProvincia() != null ? cantonWeb.getProvincia().getCode() : null, cantonWeb.getProvincia() != null ? cantonWeb.getProvincia().getDescription() : null);
            if (can != null) {
                r.add(this.modelWebTransformationService.cantonToCantonWeb(can));
            }
        }
        return r;
    }
}
