package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.TesterDao;
import org.unhcr.osmosys.model.TesterBaseEntity;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class TesterService {

    @Inject
    TesterDao testerDao;

    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(TesterService.class);

    public TesterBaseEntity getById(Long id) {
        return this.testerDao.find(id);
    }

    public TesterBaseEntity getByCode(String code) throws GeneralAppException {
        return this.testerDao.getByCode(code);
    }

    public TesterBaseEntity saveOrUpdate(TesterBaseEntity area) {
        if (area.getId() == null) {
            this.testerDao.save(area);
        } else {
            this.testerDao.update(area);
        }
        return area;
    }

    public List<TesterBaseEntity> getAll() {
        return this.testerDao.findAll();
    }

    public List<TesterBaseEntity> getByState(State state) {
        return this.testerDao.getByState(state);
    }


}
