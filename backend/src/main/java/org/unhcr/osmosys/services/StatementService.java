package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.StatementDao;
import org.unhcr.osmosys.model.Statement;
import org.unhcr.osmosys.webServices.model.PeriodStatementAsignationWeb;
import org.unhcr.osmosys.webServices.model.StatementWeb;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;

@Stateless
public class StatementService {

    @Inject
    StatementDao statementDao;

    @Inject
    ModelWebTransformationService modelWebTransformationService;

    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(StatementService.class);

    public Statement getById(Long id) {
        return this.statementDao.find(id);
    }

    public Statement saveOrUpdate(Statement statement) {
        if (statement.getId() == null) {
            this.statementDao.save(statement);
        } else {
            this.statementDao.update(statement);
        }
        return statement;
    }

    public Long save(StatementWeb statementWeb) throws GeneralAppException {
        if (statementWeb == null) {
            throw new GeneralAppException("No se puede guardar un statement null", Response.Status.BAD_REQUEST);
        }
        if (statementWeb.getId() != null) {
            throw new GeneralAppException("No se puede crear un statement con id", Response.Status.BAD_REQUEST);
        }
        this.validate(statementWeb);
        Statement statement = this.saveOrUpdate(this.modelWebTransformationService.statementWebToStatement(statementWeb));
        return statement.getId();
    }

    public List<StatementWeb> getAll() {
        return this.modelWebTransformationService.statementsToStatementsWeb(this.statementDao.findAll(), true, true, true, true, true);
    }

    public List<StatementWeb> getByState(State state) {
        return this.modelWebTransformationService.statementsToStatementsWeb(this.statementDao.getByState(state), true, true, true, true, true);
    }

    public Long update(StatementWeb statementWeb) throws GeneralAppException {
        if (statementWeb == null) {
            throw new GeneralAppException("No se puede actualizar un statement null", Response.Status.BAD_REQUEST);
        }
        if (statementWeb.getId() == null) {
            throw new GeneralAppException("No se puede crear un statement sin id", Response.Status.BAD_REQUEST);
        }
        this.validate(statementWeb);
        Statement statement = this.saveOrUpdate(this.modelWebTransformationService.statementWebToStatement(statementWeb));
        return statement.getId();
    }


    public void validate(StatementWeb statementWeb) throws GeneralAppException {
        if (statementWeb == null) {
            throw new GeneralAppException("Situación es nulo", Response.Status.BAD_REQUEST);
        }

        if (StringUtils.isBlank(statementWeb.getCode())) {
            throw new GeneralAppException("Código no válido", Response.Status.BAD_REQUEST);
        }

        if (StringUtils.isBlank(statementWeb.getDescription())) {
            throw new GeneralAppException("Descripción no válida", Response.Status.BAD_REQUEST);
        }
        if (statementWeb.getState() == null) {
            throw new GeneralAppException("Estádo no válido", Response.Status.BAD_REQUEST);
        }

        Statement itemRecovered = this.statementDao.getByCode(statementWeb.getCode());
        if (itemRecovered != null) {
            if (statementWeb.getId() == null || !statementWeb.getId().equals(itemRecovered.getId())) {
                throw new GeneralAppException("Ya existe un ítem con este código", Response.Status.BAD_REQUEST);
            }
        }

        itemRecovered = this.statementDao.getByDescription(statementWeb.getDescription());
        if (itemRecovered != null) {
            if (statementWeb.getId() == null || !statementWeb.getId().equals(itemRecovered.getId())) {
                throw new GeneralAppException("Ya existe un ítem con esta descripción", Response.Status.BAD_REQUEST);
            }
        }

        if (CollectionUtils.isEmpty(statementWeb.getPeriodStatementAsignations())) {
            throw new GeneralAppException("La declaración debe tener al menos un periodo asignado", Response.Status.BAD_REQUEST);
        }
        for (PeriodStatementAsignationWeb periodStatementAsignation : statementWeb.getPeriodStatementAsignations()) {
            if (periodStatementAsignation.getPeriod() == null || periodStatementAsignation.getPeriod().getId() == null) {
                //noinspection ConstantConditions
                throw new GeneralAppException("El periodo no tiene un id " + periodStatementAsignation.getPeriod().toString(), Response.Status.BAD_REQUEST);
            }
        }

    }

    public Statement find(Long id) {
        return this.statementDao.find(id);
    }
}
