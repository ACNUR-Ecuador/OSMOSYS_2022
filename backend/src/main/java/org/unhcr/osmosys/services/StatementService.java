package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.StatementDao;
import org.unhcr.osmosys.model.PeriodStatementAsignation;
import org.unhcr.osmosys.model.Statement;
import org.unhcr.osmosys.webServices.model.PeriodStatementAsignationWeb;
import org.unhcr.osmosys.webServices.model.StatementWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Stateless
public class StatementService {

    @Inject
    StatementDao statementDao;

    @Inject
    PeriodService periodService;

    @Inject
    AreaService areaService;

    @Inject
    PillarService pillarService;

    @Inject
    SituationService situationService;

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
        Statement statement = this.saveOrUpdate(this.statementToStatementWeb(statementWeb));
        return statement.getId();
    }

    public List<StatementWeb> getAll() {
        List<StatementWeb> r = new ArrayList<>();
        return this.statementsToStatementsWeb(this.statementDao.findAll());
    }

    public List<StatementWeb> getByState(State state) {
        List<StatementWeb> r = new ArrayList<>();
        return this.statementsToStatementsWeb(this.statementDao.getByState(state));
    }

    public Long update(StatementWeb statementWeb) throws GeneralAppException {
        if (statementWeb == null) {
            throw new GeneralAppException("No se puede actualizar un statement null", Response.Status.BAD_REQUEST);
        }
        if (statementWeb.getId() == null) {
            throw new GeneralAppException("No se puede crear un statement sin id", Response.Status.BAD_REQUEST);
        }
        this.validate(statementWeb);
        Statement statement = this.saveOrUpdate(this.statementToStatementWeb(statementWeb));
        return statement.getId();
    }

    public List<StatementWeb> statementsToStatementsWeb(List<Statement> statements) {
        List<StatementWeb> r = new ArrayList<>();
        for (Statement statement : statements) {
            r.add(this.statementToStatementWeb(statement));
        }
        return r;
    }

    public StatementWeb statementToStatementWeb(Statement statement) {
        if (statement == null) {
            return null;
        }
        StatementWeb statementWeb = new StatementWeb();
        statementWeb.setId(statement.getId());
        statementWeb.setCode(statement.getCode());
        statementWeb.setDescription(statement.getDescription());
        statementWeb.setShortDescription(statement.getShortDescription());
        statementWeb.setState(statement.getState());
        statementWeb.setParentStatement(this.statementToStatementWeb(statement.getParentStatement()));
        statementWeb.setArea(this.areaService.areaToAreaWeb(statement.getArea()));
        statementWeb.setAreaType(statement.getAreaType());
        statementWeb.setPillar(this.pillarService.pillarToPillarWeb(statement.getPillar()));
        statementWeb.setSituation(this.situationService.situationToSituationWeb(statement.getSituation()));

        statementWeb.setPeriodStatementAsignations(this.periodStatementAsignationsToPeriodStatementAsignationsWeb(statement.getPeriodStatementAsignations()));

        return statementWeb;
    }

    private List<PeriodStatementAsignationWeb> periodStatementAsignationsToPeriodStatementAsignationsWeb(Set<PeriodStatementAsignation> periodStatementAsignations) {
        List<PeriodStatementAsignationWeb> periodStatementAsignationWebs = new ArrayList<>();
        for (PeriodStatementAsignation periodStatementAsignation : periodStatementAsignations) {
            periodStatementAsignationWebs.add(this.periodStatementAsignationToPeriodStatementAsignationWeb(periodStatementAsignation));

        }
        return periodStatementAsignationWebs;
    }

    private PeriodStatementAsignationWeb periodStatementAsignationToPeriodStatementAsignationWeb(PeriodStatementAsignation periodStatementAsignation) {
        PeriodStatementAsignationWeb paw = new PeriodStatementAsignationWeb();
        paw.setId(periodStatementAsignation.getId());
        paw.setState(periodStatementAsignation.getState());
        paw.setPopulationCoverage(periodStatementAsignation.getPopulationCoverage());
        paw.setPeriod(this.periodService.periodToPeriodWeb(periodStatementAsignation.getPeriod()));
        return paw;
    }

    public List<Statement> statementsWebToStatements(List<StatementWeb> statementsWebs) {
        List<Statement> r = new ArrayList<>();
        for (StatementWeb statementWeb : statementsWebs) {
            r.add(this.statementToStatementWeb(statementWeb));
        }
        return r;
    }

    public Statement statementToStatementWeb(StatementWeb statementWeb) {
        if (statementWeb == null) {
            return null;
        }
        Statement statement;
        if (statementWeb.getId() == null) {
            statement = new Statement();
            statement.setState(statementWeb.getState());
            statement.setDescription(statementWeb.getDescription());
            statement.setCode(statementWeb.getCode());
            statement.setShortDescription(statementWeb.getShortDescription());
            statement.setAreaType(statementWeb.getArea().getAreaType());
            statement.setParentStatement(this.statementToStatementWeb(statementWeb.getParentStatement()));
            statement.setArea(this.areaService.areaToAreaWeb(statementWeb.getArea()));
            statement.getArea().addStatement(statement);
            statement.setPillar(this.pillarService.pillarWebToPillar(statementWeb.getPillar()));
            statement.setSituation(this.situationService.situationToSituationWeb(statementWeb.getSituation()));

        } else {
            statement = this.statementDao.find(statementWeb.getId());
            statement.setState(statementWeb.getState());
            statement.setDescription(statementWeb.getDescription());
            statement.setCode(statementWeb.getCode());
            statement.setShortDescription(statementWeb.getShortDescription());
            statement.setAreaType(statementWeb.getArea().getAreaType());
            statement.setParentStatement(this.statementToStatementWeb(statementWeb.getParentStatement()));
            statement.setArea(this.areaService.areaToAreaWeb(statementWeb.getArea()));
            statement.setPillar(this.pillarService.pillarWebToPillar(statementWeb.getPillar()));
            statement.setSituation(this.situationService.situationToSituationWeb(statementWeb.getSituation()));
        }

        statement.getPeriodStatementAsignations().forEach(periodStatementAsignation -> periodStatementAsignation.setState(State.INACTIVO));
        for (PeriodStatementAsignationWeb periodStatementAsignationWeb : statementWeb.getPeriodStatementAsignations()) {
            Optional<PeriodStatementAsignation> assignation = statement.getPeriodStatementAsignations().stream().filter(periodStatementAsignation -> periodStatementAsignation.getPeriod().getId().equals(periodStatementAsignationWeb.getPeriod().getId())).findFirst();
            if(assignation.isPresent()){
                assignation.get().setState(periodStatementAsignationWeb.getState());
            }else {
                PeriodStatementAsignation psa = new PeriodStatementAsignation();
                psa.setState(periodStatementAsignationWeb.getState());
                psa.setPeriod(this.periodService.periodToPeriodWeb(periodStatementAsignationWeb.getPeriod()));
                psa.setPopulationCoverage(0L);
                statement.addPeriodStatementAsignation(psa);
            }
        }
        return statement;
    }

    public void validate(StatementWeb statementWeb) throws GeneralAppException {
        if (statementWeb == null) {
            throw new GeneralAppException("Situación es nulo", Response.Status.BAD_REQUEST);
        }

        if (StringUtils.isBlank(statementWeb.getCode())) {
            throw new GeneralAppException("Código no válido", Response.Status.BAD_REQUEST);
        }
        if (StringUtils.isBlank(statementWeb.getShortDescription())) {
            throw new GeneralAppException("Descripción no válida", Response.Status.BAD_REQUEST);
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

        itemRecovered = this.statementDao.getByShortDescription(statementWeb.getShortDescription());
        if (itemRecovered != null) {
            if (statementWeb.getId() == null || !statementWeb.getId().equals(itemRecovered.getId())) {
                throw new GeneralAppException("Ya existe un ítem con esta descripción corta", Response.Status.BAD_REQUEST);
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
                throw new GeneralAppException("El periodo no tiene un id " + periodStatementAsignation.getPeriod().toString(), Response.Status.BAD_REQUEST);
            }
        }

    }
}
