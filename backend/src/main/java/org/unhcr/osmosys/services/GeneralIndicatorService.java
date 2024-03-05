package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.GeneralIndicatorDao;
import org.unhcr.osmosys.model.DissagregationAssignationToGeneralIndicator;
import org.unhcr.osmosys.model.GeneralIndicator;
import org.unhcr.osmosys.model.enums.DissagregationType;
import org.unhcr.osmosys.webServices.model.DissagregationAssignationToGeneralIndicatorWeb;
import org.unhcr.osmosys.webServices.model.GeneralIndicatorWeb;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
public class GeneralIndicatorService {

    @Inject
    GeneralIndicatorDao generalIndicatorDao;

    @Inject
    IndicatorExecutionService indicatorExecutionService;

    @Inject
    ModelWebTransformationService modelWebTransformationService;

    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(GeneralIndicatorService.class);

    public GeneralIndicator getById(Long id) {
        return this.generalIndicatorDao.find(id);
    }

    public GeneralIndicator saveOrUpdate(GeneralIndicator generalIndicator) {
        if (generalIndicator.getId() == null) {
            this.generalIndicatorDao.save(generalIndicator);
        } else {
            this.generalIndicatorDao.update(generalIndicator);
        }
        return generalIndicator;
    }

    public Long save(GeneralIndicatorWeb generalIndicatorWeb) throws GeneralAppException {
        if (generalIndicatorWeb == null) {
            throw new GeneralAppException("No se puede guardar un generalIndicator null", Response.Status.BAD_REQUEST);
        }
        if (generalIndicatorWeb.getId() != null) {
            throw new GeneralAppException("No se puede crear un generalIndicator con id", Response.Status.BAD_REQUEST);
        }
        this.validate(generalIndicatorWeb);
        GeneralIndicator generalIndicator = this.saveOrUpdate(this.modelWebTransformationService.generalIndicatorWebToGeneralIndicator(generalIndicatorWeb));
        return generalIndicator.getId();
    }

    public List<GeneralIndicatorWeb> getAll() {
        return this.modelWebTransformationService.generalIndicatorsToGeneralIndicatorsWeb(this.generalIndicatorDao.findAll());
    }

    public List<GeneralIndicatorWeb> getByState(State state) {
        return this.modelWebTransformationService.generalIndicatorsToGeneralIndicatorsWeb(this.generalIndicatorDao.getByState(state));
    }


    public GeneralIndicatorWeb getWebByPeriodId(Long periodId) throws GeneralAppException {
        return this.modelWebTransformationService.generalIndicatorToGeneralIndicatorWeb(this.generalIndicatorDao.getByPeriodId(periodId));
    }

    public GeneralIndicator update(GeneralIndicatorWeb generalIndicatorWeb) throws GeneralAppException {
        if (generalIndicatorWeb == null) {
            throw new GeneralAppException("No se puede actualizar un generalIndicator null", Response.Status.BAD_REQUEST);
        }
        if (generalIndicatorWeb.getId() == null) {
            throw new GeneralAppException("No se puede crear un generalIndicator sin id", Response.Status.BAD_REQUEST);
        }
        this.validate(generalIndicatorWeb);
        List<DissagregationAssignationToGeneralIndicator> dissagregationAssignationToGeneralIndicatorNew = new ArrayList<>();
        List<DissagregationAssignationToGeneralIndicator> dissagregationAssignationToGeneralIndicatorToActivate = new ArrayList<>();
        List<DissagregationAssignationToGeneralIndicator> dissagregationAssignationToGeneralIndicatorToDisable = new ArrayList<>();
        GeneralIndicator generalfound = this.generalIndicatorDao.getByPeriodId(generalIndicatorWeb.getPeriod().getId());
        if (generalfound.getId().equals(generalIndicatorWeb.getId())) {
            generalfound.setDescription(generalIndicatorWeb.getDescription());
            generalfound.setState(generalIndicatorWeb.getState());
            generalfound.setMeasureType(generalIndicatorWeb.getMeasureType());


            for (DissagregationAssignationToGeneralIndicatorWeb dissagregationAssignationToGeneralIndicatorWeb :
                    generalIndicatorWeb.getDissagregationAssignationsToGeneralIndicator()) {
                if (dissagregationAssignationToGeneralIndicatorWeb.getState().equals(State.ACTIVO)) {
                    Optional<DissagregationAssignationToGeneralIndicator> dissaFound =
                            generalfound.getDissagregationAssignationsToGeneralIndicator().stream().filter(dissagregationAssignationToGeneralIndicator -> dissagregationAssignationToGeneralIndicator.getDissagregationType().equals(dissagregationAssignationToGeneralIndicatorWeb.getDissagregationType())).findFirst();
                    if (dissaFound.isPresent()) {
                        if (dissaFound.get().getState().equals(State.INACTIVO)) {
                            dissagregationAssignationToGeneralIndicatorToActivate.add(dissaFound.get());
                        }
                        dissaFound.get().setState(State.ACTIVO);
                    } else {
                        DissagregationAssignationToGeneralIndicator dissanew = new DissagregationAssignationToGeneralIndicator();
                        dissanew.setState(State.ACTIVO);
                        dissanew.setDissagregationType(dissagregationAssignationToGeneralIndicatorWeb.getDissagregationType());
                        generalfound.addDissagregationAssignationsToGeneralIndicator(dissanew);
                        dissagregationAssignationToGeneralIndicatorNew.add(dissanew);
                    }
                } else {
                    Optional<DissagregationAssignationToGeneralIndicator> dissaFound =
                            generalfound.getDissagregationAssignationsToGeneralIndicator()
                                    .stream()
                                    .filter(dissagregationAssignationToGeneralIndicator ->
                                            dissagregationAssignationToGeneralIndicator.getDissagregationType().equals(dissagregationAssignationToGeneralIndicatorWeb.getDissagregationType())).findFirst();
                    if (dissaFound.isPresent()) {
                        if (dissaFound.get().getState().equals(State.ACTIVO)) {
                            dissagregationAssignationToGeneralIndicatorToDisable.add(dissaFound.get());
                        }
                        dissaFound.get().setState(State.INACTIVO);
                    }
                }
            }

            generalfound.getDissagregationAssignationsToGeneralIndicator().forEach(dissagregationAssignationToGeneralIndicator -> {
                Optional<DissagregationAssignationToGeneralIndicatorWeb> dissafound =
                        generalIndicatorWeb.getDissagregationAssignationsToGeneralIndicator()
                                .stream()
                                .filter(dissagregationAssignationToGeneralIndicatorWeb ->
                                        dissagregationAssignationToGeneralIndicator.getDissagregationType().equals(dissagregationAssignationToGeneralIndicatorWeb.getDissagregationType())).findFirst();
                dissafound.ifPresent(dissagregationAssignationToGeneralIndicatorWeb -> dissagregationAssignationToGeneralIndicatorWeb.setState(State.INACTIVO));
            });
        }
        this.saveOrUpdate(generalfound);
        /*this.indicatorExecutionService.updateIndicatorExecutionsGeneralDissagregations(generalIndicatorWeb.getPeriod().getId(),
                dissagregationAssignationToGeneralIndicatorToActivate,
                dissagregationAssignationToGeneralIndicatorToDisable,
                dissagregationAssignationToGeneralIndicatorNew);*/

        return generalfound;
    }


/*    public void updateExistingPeriodIndicators(Long periodId,
                                               List<DissagregationAssignationToGeneralIndicator> dissagregationAssignationToGeneralIndicatorNew,
                                               List<DissagregationAssignationToGeneralIndicator> dissagregationAssignationToGeneralIndicatorToActivate,
                                               List<DissagregationAssignationToGeneralIndicator> dissagregationAssignationToGeneralIndicatorToDisable) throws GeneralAppException {

        List<DissagregationType> newDissa = dissagregationAssignationToGeneralIndicatorNew.stream().map(DissagregationAssignationToGeneralIndicator::getDissagregationType).distinct().collect(Collectors.toList());
        List<DissagregationType> toActivate = dissagregationAssignationToGeneralIndicatorToActivate.stream().map(DissagregationAssignationToGeneralIndicator::getDissagregationType).distinct().collect(Collectors.toList());
        List<DissagregationType> toDisable = dissagregationAssignationToGeneralIndicatorToDisable.stream().map(DissagregationAssignationToGeneralIndicator::getDissagregationType).distinct().collect(Collectors.toList());

        toDisable.forEach(dissagregationType ->
                this.indicatorValueService.updateGeneralIndicatorStateByPeriodIdAndDissagregationType(periodId, dissagregationType, State.INACTIVO));

        toActivate.forEach(dissagregationType -> this.indicatorValueService.updateGeneralIndicatorStateByPeriodIdAndDissagregationType(periodId, dissagregationType, State.ACTIVO));

        List<Project> projects = this.projectService.getByPeriodIdWithDataToUpdateGeneralIndicator(periodId);

        for (Project project : projects) {
            List<IndicatorExecution> generalIndicators = project.getIndicatorExecutions().stream().filter(indicatorExecution -> indicatorExecution.getIndicatorType().equals(IndicatorType.GENERAL)).collect(Collectors.toList());

            for (IndicatorExecution generalIndicator : generalIndicators) {
                *//*List<Canton> cantones = generalIndicator.getIndicatorExecutionLocationAssigments().stream().map(IndicatorExecutionLocationAssigment::getLocation).collect(Collectors.toList());
                List<Month> months = generalIndicator.getQuarters().stream().flatMap(quarter -> quarter.getMonths().stream()).collect(Collectors.toList());
                for (Month month : months) {
                    for (DissagregationType dissagregationType : newDissa) {
                        List<IndicatorValue> newIndicatorValues = this.indicatorValueService.createIndicatorValueDissagregationStandardForMonth(dissagregationType, cantones);
                        for (IndicatorValue newIndicatorValue : newIndicatorValues) {
                            month.addIndicatorValue(newIndicatorValue);
                            if (newIndicatorValue.getLocation() != null) {
                                Optional<IndicatorExecutionLocationAssigment> locationAsigment = generalIndicator.getIndicatorExecutionLocationAssigments().stream().filter(indicatorExecutionLocationAssigment -> indicatorExecutionLocationAssigment.getLocation().getId().equals(newIndicatorValue.getLocation().getId())).findFirst();
                                locationAsigment.ifPresent(indicatorExecutionLocationAssigment -> newIndicatorValue.setState(locationAsigment.get().getState()));
                            }

                        }
                    }
                }*//*
                this.indicatorExecutionService.updateIndicatorExecutionsDissagregations(dissagregationAssignationToGeneralIndicatorToActivate,dissagregationAssignationToGeneralIndicatorToDisable, dissagregationAssignationToGeneralIndicatorNew);
            }
        }

    }*/

    public void validate(GeneralIndicatorWeb generalIndicatorWeb) throws GeneralAppException {
        if (generalIndicatorWeb == null) {
            throw new GeneralAppException("Indicador es nulo", Response.Status.BAD_REQUEST);
        }


        if (StringUtils.isBlank(generalIndicatorWeb.getDescription())) {
            throw new GeneralAppException("Descripción no válida", Response.Status.BAD_REQUEST);
        }
        if (generalIndicatorWeb.getState() == null) {
            throw new GeneralAppException("Estádo no válido", Response.Status.BAD_REQUEST);
        }
        if (generalIndicatorWeb.getMeasureType() == null) {
            throw new GeneralAppException("Tipo medida no válida", Response.Status.BAD_REQUEST);
        }
        if (CollectionUtils.isEmpty(generalIndicatorWeb.getDissagregationAssignationsToGeneralIndicator())) {
            throw new GeneralAppException("El indicador debe tener al menos una desagregación", Response.Status.BAD_REQUEST);
        }

    }

    public GeneralIndicator getByPeriodIdAndState(Long periodId, State state) {
        return this.generalIndicatorDao.getByIdAndState(periodId, state);
    }

    /*****************************************************/

    public List<DissagregationType> getActiveGeneralIndicatorDissagregationTypeByPeriodId(Long periodId) throws GeneralAppException {
        GeneralIndicator generalIndicator = this.generalIndicatorDao.getByPeriodId(periodId);
        return generalIndicator.getDissagregationAssignationsToGeneralIndicator()
                .stream()
                .filter(dissagregationAssignationToGeneralIndicator -> dissagregationAssignationToGeneralIndicator.getState().equals(State.ACTIVO))
                .map(DissagregationAssignationToGeneralIndicator::getDissagregationType)
                .collect(Collectors.toList());
    }

}