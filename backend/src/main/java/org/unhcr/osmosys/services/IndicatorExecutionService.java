package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.AuditAction;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.UserSecurityContext;
import com.sagatechs.generics.security.dao.UserDao;
import com.sagatechs.generics.security.model.User;
import com.sagatechs.generics.security.servicio.UserService;
import com.sagatechs.generics.service.AsyncService;
import com.sagatechs.generics.utils.DateUtils;
import com.sagatechs.generics.webservice.webModel.UserWeb;
import org.apache.commons.collections4.CollectionUtils;
import org.jboss.logging.Logger;
import org.threeten.extra.YearQuarter;
import org.unhcr.osmosys.daos.*;
import org.unhcr.osmosys.model.*;
import org.unhcr.osmosys.model.auditDTOs.LabelValue;
import org.unhcr.osmosys.model.enums.*;
import org.unhcr.osmosys.model.standardDissagregations.DissagregationAssignationToIndicatorPeriodCustomization;
import org.unhcr.osmosys.model.standardDissagregations.options.*;
import org.unhcr.osmosys.model.standardDissagregations.periodOptions.*;
import org.unhcr.osmosys.services.standardDissagregations.StandardDissagregationOptionService;
import org.unhcr.osmosys.webServices.model.*;
import org.unhcr.osmosys.webServices.model.standardDissagregations.StandardDissagregationOptionWeb;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
public class IndicatorExecutionService {

    @Inject
    private IndicatorExecutionDao indicatorExecutionDao;

    @Inject
    private GeneralIndicatorService generalIndicatorService;

    @Inject
    private QuarterService quarterService;

    @Inject
    private MonthService monthService;

    @Inject
    private IndicatorDao indicatorDao;

    @Inject
    private ProjectService projectService;

    @Inject
    private DateUtils dateUtils;

    @Inject
    private ModelWebTransformationService modelWebTransformationService;

    @Inject
    private StatementService statementService;

    @Inject
    private PeriodService periodService;

    @Inject
    private UserService userService;

    @Inject
    private OfficeService officeService;

    @Inject
    private CantonService cantonService;


    @Inject
    private UtilsService utilsService;

    @Inject
    private StandardDissagregationOptionService standardDissagregationOptionService;

    @EJB
    AsyncService asyncService;

    @Inject
    AuditService auditService;
    @Inject
    UserDao userDao;
    @Inject
    ProjectDao projectDao;
    @Inject
    ResultManagerIndicatorDao resultManagerIndicatorDao;
    @Inject
    ResultManagerIndicatorQuarterReportDao resultManagerIndicatorQuarterReportDao;
    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(IndicatorExecutionService.class);

    public IndicatorExecution getById(Long id) {
        return this.indicatorExecutionDao.find(id);
    }

    public IndicatorExecution saveOrUpdate(IndicatorExecution indicatorExecution) {
        if (indicatorExecution.getId() == null) {
            this.indicatorExecutionDao.save(indicatorExecution);
        } else {
            this.indicatorExecutionDao.update(indicatorExecution);
        }
        return indicatorExecution;
    }


    public IndicatorExecution assignPerformanceIndicatoToProject(IndicatorExecutionAssigmentWeb indicatorExecutionWeb) throws GeneralAppException {
        this.validatePerformanceIndicatorAssignationToProject(indicatorExecutionWeb);
        IndicatorExecution ie = new IndicatorExecution();
        Indicator indicator = this.indicatorDao.find(indicatorExecutionWeb.getIndicator().getId());
        Project oldProject = projectDao.find(indicatorExecutionWeb.getProject().getId()).deepCopy();
        List<LabelValue> oldprojectAudit = auditService.convertToProjectAuditDTO(oldProject).toLabelValueList();

         if (indicator == null) {
            throw new GeneralAppException("Indicador no encontrado " + indicatorExecutionWeb.getIndicator().getId(), Response.Status.BAD_REQUEST);
        }
        ie.setIndicator(indicator);
        ie.setCompassIndicator(indicator.getCompassIndicator());
        ie.setIndicatorType(indicator.getIndicatorType());
        ie.setState(indicatorExecutionWeb.getState());
        ie.setActivityDescription(indicatorExecutionWeb.getActivityDescription());
        ie.setProjectStatement(this.statementService.getById(indicatorExecutionWeb.getProjectStatement().getId()));
        ie.setKeepBudget(indicatorExecutionWeb.getKeepBudget());
        ie.setAssignedBudget(indicatorExecutionWeb.getAssignedBudget());
        Project project = this.projectService.getById(indicatorExecutionWeb.getProject().getId());
        if (project == null) {
            throw new GeneralAppException("Proyecto no encontrado " + indicatorExecutionWeb.getProject().getId(), Response.Status.BAD_REQUEST);
        }

        Period period = this.periodService.getWithAllDataById(project.getPeriod().getId());
        ie.setPeriod(period);

        // locations for ie // todo 2024 separar por un actualizador
        List<Canton> cantones = new ArrayList<>();
        if (!indicatorExecutionWeb.getLocations().isEmpty()) {
            cantones = this.cantonService.getByIds(indicatorExecutionWeb.getLocations().stream().map(BaseWebEntity::getId).collect(Collectors.toList()));
            for (Canton canton : cantones) {
                IndicatorExecutionLocationAssigment indicatorExecutionLocationAssigment = new IndicatorExecutionLocationAssigment(canton);
                ie.addIndicatorExecutionLocationAssigment(indicatorExecutionLocationAssigment);
            }
        }

        // disagregationAssigments
        List<StandardDissagregationOption> dissagregationOptions = new ArrayList<>();
        if (!indicatorExecutionWeb.getDissagregationAssigments().isEmpty()) {
            dissagregationOptions=standardDissagregationOptionService.getDissagregationOptionsByIds(indicatorExecutionWeb.getDissagregationAssigments().stream().map(BaseWebEntity::getId).collect(Collectors.toList()));
            for (StandardDissagregationOption option : dissagregationOptions) {
                IndicatorExecutionDissagregationAssigment indicatorExecutionDissagregationAssigment = new IndicatorExecutionDissagregationAssigment(option);
                ie.addIndicatorExecutionDissOptionAssigment(indicatorExecutionDissagregationAssigment);
            }
        }


        // todo 2024 separar a actualizador
        @SuppressWarnings("DuplicatedCode")
        List<DissagregationAssignationToIndicator> dissagregationAssignations = indicator.getDissagregationsAssignationToIndicator()
                .stream()
                .filter(dissagregationAssignationToIndicator -> dissagregationAssignationToIndicator.getState().equals(State.ACTIVO)
                        && dissagregationAssignationToIndicator.getPeriod().getId().equals(period.getId()))
                .collect(Collectors.toList());
        //noinspection DuplicatedCode


        List<CustomDissagregationAssignationToIndicator> customDissagregationsAssignations = indicator.getCustomDissagregationAssignationToIndicators()
                .stream()
                .filter(customDissagregationAssignationToIndicator ->
                        customDissagregationAssignationToIndicator.getState().equals(State.ACTIVO) &&
                                customDissagregationAssignationToIndicator.getPeriod().getId().equals(ie.getPeriod().getId()))
                .collect(Collectors.toList());


        ie.setProject(project);


        // manejo de desagregaciones
        Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> dissagregationsMap = this.getDissagregationTypeOptionsMap(new ArrayList<>(dissagregationAssignations), cantones, indicator, period, ie);

        List<CustomDissagregation> customDissagregations = customDissagregationsAssignations.stream().map(CustomDissagregationAssignationToIndicator::getCustomDissagregation).collect(Collectors.toList());


        this.createQuartersInIndicatorExecution(ie, project, dissagregationsMap, customDissagregations);

        // Registrar auditoría
        List<LabelValue> newprojectAudit = auditService.convertToProjectAuditDTO(project).toLabelValueList();
        auditService.logAction("Proyecto", project.getCode(),null, AuditAction.UPDATE, oldprojectAudit, newprojectAudit, null, null, State.ACTIVO);

        this.saveOrUpdate(ie);


        return ie;

    }

    private Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> getDissagregationTypeOptionsMap(
            List<DissagregationAssignationToIndicatorInterface> dissagregationAssignations,
            List<Canton> locations, Indicator indicator,
            Period period, IndicatorExecution ie
    ) throws GeneralAppException {
        List<DissagregationType> dissagregationTypes = dissagregationAssignations.stream().map(DissagregationAssignationToIndicatorInterface::getDissagregationType).collect(Collectors.toList());
        Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> dissagregationsMap = new HashMap<>();
        for (DissagregationType dissagregationType : dissagregationTypes) {
            Map<DissagregationType, List<StandardDissagregationOption>> simpleDissagregationsMap = new HashMap<>();
            for (DissagregationType simpleDissagregation : dissagregationType.getSimpleDissagregations()) {
                switch (simpleDissagregation) {
                    case LUGAR:
                        simpleDissagregationsMap.put(DissagregationType.LUGAR, new ArrayList<>(locations));
                        break;
                    case EDAD:

                        List<AgeDissagregationOption> ageOptions;
                        if (ie.getIndicatorType().equals(IndicatorType.GENERAL)) {
                            ageOptions = period.getPeriodAgeDissagregationOptions()
                                    .stream()
                                    .filter(option -> option.getState().equals(State.ACTIVO))
                                    .map(PeriodAgeDissagregationOption::getDissagregationOption)
                                    .collect(Collectors.toList());
                        } else {

                            Optional<DissagregationAssignationToIndicator> dissagregationAssignationOptional = indicator.getDissagregationsAssignationToIndicator().stream()
                                    .filter(dissagregationAssignationToIndicator -> dissagregationAssignationToIndicator.getPeriod().getId().equals(period.getId())
                                            && dissagregationAssignationToIndicator.getDissagregationType().equals(dissagregationType))
                                    .findFirst();
                            @SuppressWarnings("OptionalGetWithoutIsPresent")
                            boolean useCustomAgeDissagregation = dissagregationAssignationOptional.get().getUseCustomAgeDissagregations();
                            if (useCustomAgeDissagregation) {
                                ageOptions = dissagregationAssignationOptional.get().getDissagregationAssignationToIndicatorPeriodCustomizations()
                                        .stream()
                                        .filter(dissagregationAssignationToIndicatorPeriodCustomization -> dissagregationAssignationToIndicatorPeriodCustomization.getState().equals(State.ACTIVO))
                                        .map(DissagregationAssignationToIndicatorPeriodCustomization::getAgeDissagregationOption).collect(Collectors.toList());
                            } else {
                                ageOptions = period.getPeriodAgeDissagregationOptions()
                                        .stream()
                                        .filter(option -> option.getState().equals(State.ACTIVO))
                                        .map(PeriodAgeDissagregationOption::getDissagregationOption)
                                        .collect(Collectors.toList());
                            }
                        }
                        /*Se filtran Solo edades seleccionadas*/
                        if(!ie.getIndicatorExecutionDissagregationAssigments().isEmpty()){
                            List<Long> ageSelectedOptionsIds=ie.getIndicatorExecutionDissagregationAssigments()
                                        .stream()
                                        .filter(option ->  option.getState().equals(State.ACTIVO))
                                        .map(option -> option.getDisagregationOption().getId()).collect(Collectors.toList());

                            List<AgeDissagregationOption> filteredAges = new ArrayList<>();
                            for (AgeDissagregationOption ageOption : ageOptions) {
                                if (ageSelectedOptionsIds.contains(ageOption.getId())) {
                                    filteredAges.add(ageOption);
                                }
                            }
                            ageOptions=filteredAges;
                        }
                        simpleDissagregationsMap.put(DissagregationType.EDAD, new ArrayList<>(ageOptions));
                        break;
                    case GENERO:
                        List<GenderDissagregationOption> genderOptions = period.getPeriodGenderDissagregationOptions()
                                .stream()
                                .filter(option -> option.getState().equals(State.ACTIVO))
                                .map(PeriodGenderDissagregationOption::getDissagregationOption)
                                .collect(Collectors.toList());
                        /*Se filtran Solo géneros seleccionadgs*/
                        if(!ie.getIndicatorExecutionDissagregationAssigments().isEmpty()){
                            List<Long> genderSelectedOptionsIds=ie.getIndicatorExecutionDissagregationAssigments()
                                    .stream()
                                    .filter(option -> option.getState().equals(State.ACTIVO))
                                    .map(option -> option.getDisagregationOption().getId()).collect(Collectors.toList());

                            List<GenderDissagregationOption> filteredGenders = new ArrayList<>();
                            for (GenderDissagregationOption genderOption : genderOptions) {
                                if (genderSelectedOptionsIds.contains(genderOption.getId())) {
                                    filteredGenders.add(genderOption);
                                }
                            }
                            genderOptions=filteredGenders;
                        }
                        simpleDissagregationsMap.put(DissagregationType.GENERO, new ArrayList<>(genderOptions));
                        break;
                    case DIVERSIDAD:
                        List<DiversityDissagregationOption> diversityOptions = period.getPeriodDiversityDissagregationOptions()
                                .stream()
                                .filter(option -> option.getState().equals(State.ACTIVO))
                                .map(PeriodDiversityDissagregationOption::getDissagregationOption)
                                .collect(Collectors.toList());
                        /*Se filtran Solo diversidades seleccionadas*/
                        if(!ie.getIndicatorExecutionDissagregationAssigments().isEmpty()){
                            List<Long> diversitySelectedOptionsIds=ie.getIndicatorExecutionDissagregationAssigments()
                                    .stream()
                                    .filter(option -> option.getState().equals(State.ACTIVO))
                                    .map(option -> option.getDisagregationOption().getId()).collect(Collectors.toList());

                            List<DiversityDissagregationOption> filteredDiversities = new ArrayList<>();
                            for (DiversityDissagregationOption diversityOption : diversityOptions) {
                                if (diversitySelectedOptionsIds.contains(diversityOption.getId())) {
                                    filteredDiversities.add(diversityOption);
                                }
                            }
                            diversityOptions=filteredDiversities;
                        }
                        simpleDissagregationsMap.put(DissagregationType.DIVERSIDAD, new ArrayList<>(diversityOptions));
                        break;
                    case PAIS_ORIGEN:
                        List<CountryOfOriginDissagregationOption> countryOfOriginOptions = period.getPeriodCountryOfOriginDissagregationOptions()
                                .stream()
                                .filter(option -> option.getState().equals(State.ACTIVO))
                                .map(PeriodCountryOfOriginDissagregationOption::getDissagregationOption)
                                .collect(Collectors.toList());
                        /*Se filtran Solo países seleccionados*/
                        if(!ie.getIndicatorExecutionDissagregationAssigments().isEmpty()){
                            List<Long> diversitySelectedOptionsIds=ie.getIndicatorExecutionDissagregationAssigments()
                                    .stream()
                                    .filter(option -> option.getState().equals(State.ACTIVO))
                                    .map(option -> option.getDisagregationOption().getId()).collect(Collectors.toList());

                            List<CountryOfOriginDissagregationOption> filteredCountrys = new ArrayList<>();
                            for (CountryOfOriginDissagregationOption countryOption : countryOfOriginOptions) {
                                if (diversitySelectedOptionsIds.contains(countryOption.getId())) {
                                    filteredCountrys.add(countryOption);
                                }
                            }
                            countryOfOriginOptions=filteredCountrys;
                        }
                        simpleDissagregationsMap.put(DissagregationType.PAIS_ORIGEN, new ArrayList<>(countryOfOriginOptions));
                        break;
                    case TIPO_POBLACION:
                        List<PopulationTypeDissagregationOption> populationTypesOptions = period.getPeriodPopulationTypeDissagregationOptions()
                                .stream()
                                .filter(option -> option.getState().equals(State.ACTIVO))
                                .map(PeriodPopulationTypeDissagregationOption::getDissagregationOption)
                                .collect(Collectors.toList());
                        /*Se filtran Solo tipos de Población seleccionados*/
                        if(!ie.getIndicatorExecutionDissagregationAssigments().isEmpty()){
                            List<Long> diversitySelectedOptionsIds=ie.getIndicatorExecutionDissagregationAssigments()
                                    .stream()
                                    .filter(option -> option.getState().equals(State.ACTIVO))
                                    .map(option -> option.getDisagregationOption().getId()).collect(Collectors.toList());

                            List<PopulationTypeDissagregationOption> filteredPopulationTypes = new ArrayList<>();
                            for (PopulationTypeDissagregationOption populationOption : populationTypesOptions) {
                                if (diversitySelectedOptionsIds.contains(populationOption.getId())) {
                                    filteredPopulationTypes.add(populationOption);
                                }
                            }
                            populationTypesOptions=filteredPopulationTypes;
                        }
                        simpleDissagregationsMap.put(DissagregationType.TIPO_POBLACION, new ArrayList<>(populationTypesOptions));
                        break;

                    default:
                        throw new GeneralAppException("Error al definir opciones para el tipo de segregación simple " + simpleDissagregation, Response.Status.INTERNAL_SERVER_ERROR);
                }
            }
            dissagregationsMap.put(dissagregationType, simpleDissagregationsMap);
        }
        return dissagregationsMap;
    }

    private void createQuartersInIndicatorExecution(
            IndicatorExecution ie,
            Project project,
            Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> dissagregationsMap,
            List<CustomDissagregation> customDissagregations
    ) throws GeneralAppException {
        Set<Quarter> qs = this.quarterService.createQuarters(project.getStartDate(), project.getEndDate(), dissagregationsMap, customDissagregations);

        this.validateLocationsLocationsInLocationsDissagregations(dissagregationsMap);
        List<Quarter> qsl = setOrderInQuartersAndMonths(qs);
        for (Quarter quarter : qsl) {
            ie.addQuarter(quarter);
        }
    }

    private void validateLocationsLocationsInLocationsDissagregations(Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> dissagregationsMap) throws GeneralAppException {
        for (DissagregationType dissagregationType : dissagregationsMap.keySet()) {
            if (dissagregationType.isLocationsDissagregation()) {
                if (CollectionUtils.isEmpty(dissagregationsMap.get(dissagregationType).get(DissagregationType.LUGAR))) {
                    throw new GeneralAppException("No se puede crear una desagregación de lugar sin Lugares ", Response.Status.BAD_REQUEST);
                }
            }
        }
    }

    public Long updateAssignPerformanceIndicatorToProject(IndicatorExecutionAssigmentWeb indicatorExecutionAssigmentWeb) throws GeneralAppException {
        if (indicatorExecutionAssigmentWeb.getId() == null) {
            throw new GeneralAppException("No se pudo encontrar la asignación (Id:" + indicatorExecutionAssigmentWeb.getId() + ")", Response.Status.BAD_REQUEST);
        }
        IndicatorExecution indicatorExecution = this.indicatorExecutionDao.find(indicatorExecutionAssigmentWeb.getId());
        if (indicatorExecution == null) {
            throw new GeneralAppException("No se pudo encontrar la asignación (Id:" + indicatorExecutionAssigmentWeb.getId() + ")", Response.Status.BAD_REQUEST);
        }
        if (!indicatorExecution.getProject().getId().equals(indicatorExecutionAssigmentWeb.getProject().getId())) {
            throw new GeneralAppException("El indicador no corresponde al proyecto (Id:" + indicatorExecutionAssigmentWeb.getId() + " projectId" + indicatorExecutionAssigmentWeb.getId() + ")", Response.Status.BAD_REQUEST);
        }


        Project project = projectDao.find(indicatorExecutionAssigmentWeb.getProject().getId()).deepCopy();
        List<LabelValue> oldprojectAudit = auditService.convertToProjectAuditDTO(project).toLabelValueList();


        indicatorExecution.setState(indicatorExecutionAssigmentWeb.getState());

        indicatorExecution.setProjectStatement(this.statementService.getById(indicatorExecutionAssigmentWeb.getProjectStatement().getId()));
        indicatorExecution.setActivityDescription(indicatorExecutionAssigmentWeb.getActivityDescription());
        indicatorExecution.setKeepBudget(indicatorExecutionAssigmentWeb.getKeepBudget());
        indicatorExecution.setAssignedBudget(indicatorExecutionAssigmentWeb.getAssignedBudget());

        this.updateIndicatorExecutionTotals(indicatorExecution);
        this.updatePartnerIndicatorExecutionLocationsAndDissagregationOptionsAssigment(indicatorExecutionAssigmentWeb.getId(),indicatorExecutionAssigmentWeb.getLocations(), indicatorExecutionAssigmentWeb.getDissagregationAssigments());
        // Registrar auditoría
        List<LabelValue> newprojectAudit = auditService.convertToProjectAuditDTO(project).toLabelValueList();
        auditService.logAction("Proyecto", project.getCode(), null, AuditAction.UPDATE, oldprojectAudit, newprojectAudit, null, null, State.ACTIVO);

        this.saveOrUpdate(indicatorExecution);
        return indicatorExecution.getId();
    }


    public void updateAllIndicatorExecutionsDissagregationsByPeriod(Period period) throws GeneralAppException {
        updateAllIndicatorExecutionsDissagregationsByPeriod(period, null);
    }

    public void updateAllIndicatorExecutionsDissagregationsByPeriod(Period period, String jobId) throws GeneralAppException {

        Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> periodDissagregationMapGeneralIndicator = this.getPeriodDessagregationMap(true, period, null);

        LOGGER.debug("updateAllIndicatorExecutionsDissagregationsByPeriod ");
        this.updateGeneralIndicatorExecutionsDissagregations(period, periodDissagregationMapGeneralIndicator, jobId);
        // performance indicators
        // recupero todos los indicadores afectados
        List<Indicator> indicatorsToUpdate = this.indicatorDao.getByPeriodDissagregationAssignment(period.getId());
        int totalIndicators=indicatorsToUpdate.size();
        LOGGER.debug("updateAllIndicatorExecutionsDissagregationsByPeriod : "+ totalIndicators);
        int i=0;
        for (Indicator indicator : indicatorsToUpdate) {
            i++;
            int progress = 40 + (int) (60 * ((double) i / indicatorsToUpdate.size()));
            JobStatusService.updateJob(jobId, progress, "Actualizando Indicadores de Producto " + i + "/" + indicatorsToUpdate.size());
            LOGGER.debug("updateAllIndicatorExecutionsDissagregationsByPeriod "+i+"/"+totalIndicators);
            this.updatePerformanceIndicatorExecutionsDissagregations(period, indicator);
        }

    }

    public void updateIndicatorExecutionsLocations(IndicatorExecution indicatorExecution,
                                                   Set<Canton> locationsToActivate,
                                                   Set<Canton> locationsToDesactive
    ) throws GeneralAppException {
        //seteo locatios para ie
        Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> periodDissagregationMap;
        Period period = this.periodService.getWithAllDataById(indicatorExecution.getPeriod().getId());
        if (indicatorExecution.getIndicatorType().equals(IndicatorType.GENERAL)) {
            periodDissagregationMap = this.getPeriodDessagregationMap(true, period, indicatorExecution.getIndicator());

        } else {
            periodDissagregationMap = this.getPeriodDessagregationMap(false, period, indicatorExecution.getIndicator());
        }
        if (periodDissagregationMap == null) return;

        this.updateIndicatorExecutionLocationsAssignations(indicatorExecution, locationsToActivate, locationsToDesactive);
        this.setStandardDissagregationOptionsForIndicatorExecutions(indicatorExecution, periodDissagregationMap);
        this.setStandardDissagregationSelectedOptionsForIndicatorExecutions(indicatorExecution, periodDissagregationMap);
        this.quarterService.updateQuarterDissagregations(indicatorExecution, periodDissagregationMap, null);
        this.updateIndicatorExecutionTotals(indicatorExecution);
        this.saveOrUpdate(indicatorExecution);
    }


    public void updatePerformanceIndicatorExecutionsDissagregations(Period period, Indicator indicator) throws GeneralAppException {
        // obtengo las segregaciones y las opciones standard
        List<IndicatorExecution> ies = this.indicatorExecutionDao.getByIndicatorIdAndPeriodId(period.getId(), indicator.getId());
        Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> periodDissagregationMap = this.getPeriodDessagregationMap(false, period, indicator);
        // obtengo las segregaciones opcionales

        if (periodDissagregationMap == null) return;

        for (IndicatorExecution ie : ies) {
            // debo agregar localizaciones si es el caso
            this.setStandardDissagregationOptionsForIndicatorExecutions(ie, periodDissagregationMap);
            if(!ie.getIndicatorExecutionDissagregationAssigments().isEmpty()){
                IndicatorExecutionService.DissagregationOptionsToActivateDesactivate dissOptionsToActivateDessactivate=this.getDissOptionsToActivateDessactivateByIndicatorAssignations(ie);
                this.updateIndicatorExecutionDissagregationOptionAssignations(ie, dissOptionsToActivateDessactivate.dissagregationOptionToActivate, dissOptionsToActivateDessactivate.dissagregationOptionToDissable);
            }
            this.setStandardDissagregationSelectedOptionsForIndicatorExecutions(ie, periodDissagregationMap);
            this.quarterService.updateQuarterDissagregations(ie, periodDissagregationMap, indicator.getCustomDissagregationAssignationToIndicators());
            this.updateIndicatorExecutionTotals(ie);
            this.saveOrUpdate(ie);
        }

    }

    private void setStandardDissagregationOptionsForIndicatorExecutions(IndicatorExecution ie, Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> periodDissagregationMap) {
        List<StandardDissagregationOption> locations = ie.getIndicatorExecutionLocationAssigments()
                .stream()
                .filter(indicatorExecutionLocationAssigment -> indicatorExecutionLocationAssigment.getState().equals(State.ACTIVO))
                .map(IndicatorExecutionLocationAssigment::getLocation).collect(Collectors.toList());

        periodDissagregationMap.forEach((dissagregationType, dissagregationTypeListMap) -> {
            if (dissagregationType.isLocationsDissagregation()) {
                dissagregationTypeListMap.forEach((dissagregationType1, standardDissagregationOptions) -> {
                    if (dissagregationType1.equals(DissagregationType.LUGAR)) {
                        periodDissagregationMap.get(dissagregationType).put(DissagregationType.LUGAR, locations);
                    }
                });
            }
        });
    }
    private void setStandardDissagregationSelectedOptionsForIndicatorExecutions(IndicatorExecution ie, Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> periodDissagregationMap) {

        if(ie.getIndicatorExecutionDissagregationAssigments().isEmpty()){
            return;
        }
        Set<Long> dissagregationsSelectedOptionsIds = ie.getIndicatorExecutionDissagregationAssigments().stream()
                .filter(option -> option.getState().equals(State.ACTIVO))
                .map(option -> option.getDisagregationOption().getId())
                .collect(Collectors.toSet());
        /*Obtengo las opciones de desagregación a configurar*/
        /*AgeDissagregations*/
        List<AgeDissagregationOption> ageOptions=standardDissagregationOptionService.getAgeDissagregationOptionByState(State.ACTIVO);
        List<AgeDissagregationOption> selectedAgeOptions = ageOptions.stream()
                .filter(ageOption -> dissagregationsSelectedOptionsIds.contains(ageOption.getId()))
                .collect(Collectors.toList());
        /*GenderDissagregations*/
        List<GenderDissagregationOption> genderOptions=standardDissagregationOptionService.getGenderDissagregationOptionByState(State.ACTIVO);
        List<GenderDissagregationOption> selectedGenderOptions = genderOptions.stream()
                .filter(genderOption -> dissagregationsSelectedOptionsIds.contains(genderOption.getId()))
                .collect(Collectors.toList());
        /*PopulationTypeDissagregations*/
        List<PopulationTypeDissagregationOption> populationTypeOptions=standardDissagregationOptionService.getPopulationTypeOptionsByState(State.ACTIVO);
        List<PopulationTypeDissagregationOption> selectedPopulationTypeOptions = populationTypeOptions.stream()
                .filter(populationTypeOption -> dissagregationsSelectedOptionsIds.contains(populationTypeOption.getId()))
                .collect(Collectors.toList());
        /*CountryOfOriginDissagregations*/
        List<CountryOfOriginDissagregationOption> countryOfOriginOptions=standardDissagregationOptionService.getCountryOfOriginDissagregationOptionByState(State.ACTIVO);
        List<CountryOfOriginDissagregationOption> selectedCountryOfOriginOptions = countryOfOriginOptions.stream()
                .filter(countryOfOriginOption -> dissagregationsSelectedOptionsIds.contains(countryOfOriginOption.getId()))
                .collect(Collectors.toList());
        /*DiversityDissagregations*/
        List<DiversityDissagregationOption> diversityOptions=standardDissagregationOptionService.getDiversityeOptionsByState(State.ACTIVO);
        List<DiversityDissagregationOption> selectedDiversityOptions = diversityOptions.stream()
                .filter(diversityOption -> dissagregationsSelectedOptionsIds.contains(diversityOption.getId()))
                .collect(Collectors.toList());

        /*asigno en el mapa las opciones a la desagreación correspondiente*/
        periodDissagregationMap.forEach((dissagregationType, dissagregationTypeListMap) -> {
            /*AgeDissagregation*/
            if (dissagregationType.getSimpleDissagregations().contains(DissagregationType.EDAD)) {
                dissagregationTypeListMap.forEach((dissagregationType1, standardDissagregationOptions) -> {
                    if (dissagregationType1.equals(DissagregationType.EDAD)) {
                        periodDissagregationMap.get(dissagregationType).put(DissagregationType.EDAD, new ArrayList<>(selectedAgeOptions));
                    }
                });
            }
            /*GenderDissagregation*/
            if (dissagregationType.getSimpleDissagregations().contains(DissagregationType.GENERO)) {
                dissagregationTypeListMap.forEach((dissagregationType1, standardDissagregationOptions) -> {
                    if (dissagregationType1.equals(DissagregationType.GENERO)) {
                        periodDissagregationMap.get(dissagregationType).put(DissagregationType.GENERO, new ArrayList<>(selectedGenderOptions));
                    }
                });
            }
            /*PopulationTypeDissagregation*/
            if (dissagregationType.getSimpleDissagregations().contains(DissagregationType.TIPO_POBLACION)) {
                dissagregationTypeListMap.forEach((dissagregationType1, standardDissagregationOptions) -> {
                    if (dissagregationType1.equals(DissagregationType.TIPO_POBLACION)) {
                        periodDissagregationMap.get(dissagregationType).put(DissagregationType.TIPO_POBLACION, new ArrayList<>(selectedPopulationTypeOptions));
                    }
                });
            }
            /*CountryOfOriginDissagregation*/
            if (dissagregationType.getSimpleDissagregations().contains(DissagregationType.PAIS_ORIGEN)) {
                dissagregationTypeListMap.forEach((dissagregationType1, standardDissagregationOptions) -> {
                    if (dissagregationType1.equals(DissagregationType.PAIS_ORIGEN)) {
                        periodDissagregationMap.get(dissagregationType).put(DissagregationType.PAIS_ORIGEN, new ArrayList<>(selectedCountryOfOriginOptions));
                    }
                });
            }
            /*DiversityDissagregation*/
            if (dissagregationType.getSimpleDissagregations().contains(DissagregationType.DIVERSIDAD)) {
                dissagregationTypeListMap.forEach((dissagregationType1, standardDissagregationOptions) -> {
                    if (dissagregationType1.equals(DissagregationType.DIVERSIDAD)) {
                        periodDissagregationMap.get(dissagregationType).put(DissagregationType.DIVERSIDAD, new ArrayList<>(selectedDiversityOptions));
                    }
                });
            }
        });
    }

    public void updateGeneralIndicatorExecutionsDissagregations(Period period, Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> dissagregationTypeMapMap) throws GeneralAppException {
        updateGeneralIndicatorExecutionsDissagregations(period, dissagregationTypeMapMap, null);
    }

    public void updateGeneralIndicatorExecutionsDissagregations(Period period, Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> dissagregationTypeMapMap, String jobId) throws GeneralAppException {
        // busco los ies que pueden ser actualizados
        LOGGER.debug("updateGeneralIndicatorExecutionsDissagregations ");
        List<IndicatorExecution> iesToUpdate = this.indicatorExecutionDao.getGeneralIndicatorsExecutionsByPeriodId(period.getId());
        int total = iesToUpdate.size();
        LOGGER.debug("updateGeneralIndicatorExecutionsDissagregations total: " + total);
        int i=0;
        for (IndicatorExecution ie : iesToUpdate) {
            i++;
            int progress = 20 + (int) (20 * ((double) i / iesToUpdate.size()));
            JobStatusService.updateJob(jobId, progress, "Actualizando Indicadores Generales " + i + "/" + iesToUpdate.size());
            LOGGER.debug("updateGeneralIndicatorExecutionsDissagregations : " +i+"/"+ total);
            this.setStandardDissagregationOptionsForIndicatorExecutions(ie, dissagregationTypeMapMap);
            this.quarterService.updateQuarterDissagregations(ie, dissagregationTypeMapMap, null);
            this.updateIndicatorExecutionTotals(ie);
            this.saveOrUpdate(ie);
        }

    }


    /**
     * permite obtener el mapa de desagregaciones y opciones
     *
     * @param isGeneralIndicator si es para in indicador general true, para performance indicator false
     * @param period
     * @param indicator
     * @return
     * @throws GeneralAppException
     */
    private Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> getPeriodDessagregationMap(
            boolean isGeneralIndicator, Period period, Indicator indicator) throws GeneralAppException {

        final Period periodFulldata = this.periodService.getWithAllDataById(period.getId());
        Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> dissagregationMap = new HashMap<>();
        List<DissagregationAssignationToIndicatorInterface> dissagregationsForPeriod;
        if (isGeneralIndicator && periodFulldata.getGeneralIndicator() == null) {
            return null;
        } else if (isGeneralIndicator && periodFulldata.getGeneralIndicator() != null) {
            dissagregationsForPeriod = periodFulldata.getGeneralIndicator().getDissagregationAssignationsToGeneralIndicator()
                    .stream()
                    .filter(dissagregationAssignationToGeneralIndicator -> dissagregationAssignationToGeneralIndicator.getState().equals(State.ACTIVO))
                    .collect(Collectors.toList());
        } else {
            dissagregationsForPeriod = indicator.getDissagregationsAssignationToIndicator().stream()
                    .filter(dissagregationAssignationToIndicator -> dissagregationAssignationToIndicator.getState().equals(State.ACTIVO) && dissagregationAssignationToIndicator.getPeriod().getId().equals(periodFulldata.getId()))
                    .collect(Collectors.toList());
        }
        for (DissagregationAssignationToIndicatorInterface dai : dissagregationsForPeriod) {
            DissagregationType dissagregationType = dai.getDissagregationType();
            List<StandardDissagregationOption> options;
            Map<DissagregationType, List<StandardDissagregationOption>> dissagegationSimpleOptionsMap = new HashMap<>();
            for (DissagregationType simpleDissagregation : dissagregationType.getSimpleDissagregations()) {
                if (simpleDissagregation.equals(DissagregationType.EDAD)) {
                    if (isGeneralIndicator) { // con edad
                        options = periodFulldata.getPeriodAgeDissagregationOptions().stream()
                                .filter(option -> option.getState().equals(State.ACTIVO))
                                .map(PeriodAgeDissagregationOption::getDissagregationOption)
                                .map(StandardDissagregationOption.class::cast)
                                .collect(Collectors.toList());

                    } else {

                        Boolean userCustomDissagregation = ((DissagregationAssignationToIndicator) dai).getUseCustomAgeDissagregations();
                        if (userCustomDissagregation) {
                            options = ((DissagregationAssignationToIndicator) dai).getDissagregationAssignationToIndicatorPeriodCustomizations()
                                    .stream()
                                    .filter(dissagregationAssignationToIndicatorPeriodCustomization -> dissagregationAssignationToIndicatorPeriodCustomization.getState().equals(State.ACTIVO))
                                    .map(DissagregationAssignationToIndicatorPeriodCustomization::getAgeDissagregationOption)
                                    .collect(Collectors.toList());
                        } else {
                            options = periodFulldata.getPeriodAgeDissagregationOptions().stream()
                                    .filter(option -> option.getState().equals(State.ACTIVO))
                                    .map(PeriodAgeDissagregationOption::getDissagregationOption).collect(Collectors.toList());
                        }
                    }
                } else if (simpleDissagregation.equals(DissagregationType.LUGAR)) {
                    // dejo en null porq dependerá del indicator execution
                    options = null;
                } else if (simpleDissagregation.equals(DissagregationType.GENERO)) {
                    options = periodFulldata.getPeriodGenderDissagregationOptions().stream()
                            .filter(option -> option.getState().equals(State.ACTIVO))
                            .map(PeriodGenderDissagregationOption::getDissagregationOption).collect(Collectors.toList());
                } else if (simpleDissagregation.equals(DissagregationType.DIVERSIDAD)) {
                    options = periodFulldata.getPeriodDiversityDissagregationOptions().stream()
                            .filter(option -> option.getState().equals(State.ACTIVO))
                            .map(PeriodDiversityDissagregationOption::getDissagregationOption).collect(Collectors.toList());
                } else if (simpleDissagregation.equals(DissagregationType.TIPO_POBLACION)) {
                    options = periodFulldata.getPeriodPopulationTypeDissagregationOptions().stream()
                            .filter(option -> option.getState().equals(State.ACTIVO))
                            .map(PeriodPopulationTypeDissagregationOption::getDissagregationOption).collect(Collectors.toList());
                } else if (simpleDissagregation.equals(DissagregationType.PAIS_ORIGEN)) {
                    options = periodFulldata.getPeriodCountryOfOriginDissagregationOptions().stream()
                            .filter(option -> option.getState().equals(State.ACTIVO))
                            .map(PeriodCountryOfOriginDissagregationOption::getDissagregationOption).collect(Collectors.toList());
                } else {
                    throw new GeneralAppException("Error en la actualización de desagregaciones, extracción de desagregacoines del indicador", Response.Status.INTERNAL_SERVER_ERROR);
                }
                dissagegationSimpleOptionsMap.put(simpleDissagregation, options);
            }
            dissagregationMap.put(dissagregationType, dissagegationSimpleOptionsMap);
        }
        return dissagregationMap;

    }


    /***************************************************************************************************************/


    public IndicatorExecution createGeneralIndicatorForProject(Project project) throws GeneralAppException {
        IndicatorExecution ie = new IndicatorExecution();
        ie.setCompassIndicator(false);
        //target
        ie.setProject(project);
        ie.setIndicatorType(IndicatorType.GENERAL);
        ie.setTarget(null);
        Period period = this.periodService.getWithAllDataById(project.getPeriod().getId());
        ie.setPeriod(period);
        ie.setState(State.ACTIVO);
        ie.setKeepBudget(false);


        List<Canton> cantones = project.getProjectLocationAssigments().stream().filter(projectLocationAssigment -> projectLocationAssigment.getState().equals(State.ACTIVO)).map(ProjectLocationAssigment::getLocation).collect(Collectors.toList());
        GeneralIndicator generalIndicator = this.generalIndicatorService.getByPeriodIdAndState(project.getPeriod().getId(), State.ACTIVO);

        if (generalIndicator != null) {
            Set<DissagregationAssignationToGeneralIndicator> dissagregationAssignations = generalIndicator
                    .getDissagregationAssignationsToGeneralIndicator()
                    .stream().filter(dissagregationAssignationToGeneralIndicator -> dissagregationAssignationToGeneralIndicator.getState().equals(State.ACTIVO))
                    .collect(Collectors.toSet());
            List<CustomDissagregation> customDissagregations = new ArrayList<>();

            // manejo de desagregaciones
            Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> dissagregationsMap = this.getDissagregationTypeOptionsMap(new ArrayList<>(dissagregationAssignations), cantones, null, period, ie);

            createQuartersInIndicatorExecution(ie, project, dissagregationsMap, customDissagregations);

        }


        return ie;

    }


    private List<Quarter> setOrderInQuartersAndMonths(Set<Quarter> qs) {
        List<Quarter> qsl = qs.stream().sorted((o1, o2) -> {
            if (o1.getYear() < o2.getYear()) {
                return -1;
            } else if (o1.getYear() > o2.getYear()) {
                return 1;
            } else return Integer.compare(o1.getQuarter().getOrder(), o2.getQuarter().getOrder());
        }).collect(Collectors.toList());

        int orderQ = 1;
        for (Quarter quarter : qsl) {
            quarter.setOrder(orderQ);
            orderQ++;
        }
        List<Month> monthList =
                qs.stream()
                        .map(Quarter::getMonths)
                        .flatMap(Collection::stream).sorted((o1, o2) -> {
                            if (o1.getYear() < o2.getYear()) {
                                return -1;
                            } else if (o1.getYear() > o2.getYear()) {
                                return 1;
                            } else return Integer.compare(o1.getMonth().getOrder(), o2.getMonth().getOrder());
                        }).collect(Collectors.toList());
        int orderM = 1;
        for (Month month : monthList) {
            month.setOrder(orderM);
            orderM++;
        }
        return qsl;
    }


    public List<IndicatorExecutionWeb> getGeneralIndicatorExecutionsAdministrationByProjectId(Long projectId) throws GeneralAppException {
        List<IndicatorExecution> ies = this.indicatorExecutionDao.getGeneralIndicatorExecutionsByProjectId(projectId);
        return this.modelWebTransformationService.indicatorExecutionsToIndicatorExecutionsWeb(ies, true);
    }

    public List<IndicatorExecutionWeb> getGeneralIndicatorExecutionsByProjectIdAndState(Long projectId, State state) throws GeneralAppException {
        List<IndicatorExecution> ies = this.indicatorExecutionDao.getGeneralIndicatorExecutionsByProjectIdAndState(projectId, state);
        return this.modelWebTransformationService.indicatorExecutionsToIndicatorExecutionsWeb(ies, true);
    }

    public List<IndicatorExecutionWeb> getPerformanceIndicatorExecutionsByProjectId(Long projectId, State state) throws GeneralAppException {
        List<IndicatorExecution> ies = this.indicatorExecutionDao.getPerformanceIndicatorExecutionsByProjectIdAndState(projectId, state);
        return this.modelWebTransformationService.indicatorExecutionsToIndicatorExecutionsWeb(ies, true);
    }

    public void updateTargets(TargetUpdateDTOWeb targetUpdateDTOWeb) throws GeneralAppException {
        IndicatorExecution ie = this.indicatorExecutionDao.getByIdWithIndicatorValues(targetUpdateDTOWeb.getIndicatorExecutionId());
        Project project = projectDao.find(ie.getProject().getId()).deepCopy();
        List<LabelValue> oldprojectAudit = auditService.convertToProjectAuditDTO(project).toLabelValueList();

        ie.setTarget(targetUpdateDTOWeb.getTotalTarget());
        /*
        if (ie.getIndicatorType().equals(IndicatorType.GENERAL)) {
            ie.setTarget(targetUpdateDTOWeb.getTotalTarget());
        } else {
            for (QuarterWeb quarterResumeWeb : targetUpdateDTOWeb.getQuarters()) {
                Optional<Quarter> quarterOptional = ie.getQuarters().stream().filter(quarter -> quarter.getId().equals(quarterResumeWeb.getId())).findFirst();
                if (!quarterOptional.isPresent()) {
                    throw new GeneralAppException("No se pudo encontrar el trimestre con id " + quarterResumeWeb.getId(), Response.Status.NOT_FOUND);
                } else {
                    quarterOptional.get().setTarget(quarterResumeWeb.getTarget());
                }
            }
        }

*/


        this.updateIndicatorExecutionTotals(ie);
        // Registrar auditoría
        List<LabelValue> newprojectAudit = auditService.convertToProjectAuditDTO(project).toLabelValueList();
        auditService.logAction("Proyecto", project.getCode(),null, AuditAction.UPDATE, oldprojectAudit, newprojectAudit,null, null, State.ACTIVO);
        this.saveOrUpdate(ie);
    }


    public void updateIndicatorExecutionTotals(IndicatorExecution indicatorExecution) throws GeneralAppException {
        TotalIndicatorCalculationType totalIndicatorCalculationType;
        if (indicatorExecution.getIndicatorType().equals(IndicatorType.GENERAL)) {
            totalIndicatorCalculationType = TotalIndicatorCalculationType.SUMA;
        } else if (indicatorExecution.getIndicator() != null && indicatorExecution.getIndicator().getTotalIndicatorCalculationType() != null) {
            totalIndicatorCalculationType = indicatorExecution.getIndicator().getTotalIndicatorCalculationType();
        } else {
            throw new GeneralAppException("Tipo de calculo inválido para el indicador ");
        }
        for (Quarter quarter : indicatorExecution.getQuarters()) {
            this.quarterService.updateQuarterTotals(quarter, totalIndicatorCalculationType);
        }
        List<Quarter> quarters = indicatorExecution.getQuarters().stream().filter(quarter -> quarter.getState().equals(State.ACTIVO)).collect(Collectors.toList());


        // total execution and total percentage
        List<BigDecimal> totalQuarterValues = quarters.stream()
                .map(Quarter::getTotalExecution).filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(totalQuarterValues)) {
            indicatorExecution.setTotalExecution(null);
            indicatorExecution.setExecutionPercentage(null);
        } else {
            BigDecimal totalExecution = this.utilsService.calculetTotalExecution(totalIndicatorCalculationType, totalQuarterValues);
            indicatorExecution.setTotalExecution(totalExecution);

            //noinspection DuplicatedCode
            if (indicatorExecution.getTotalExecution() != null && indicatorExecution.getTarget() != null) {
                if (indicatorExecution.getTarget().compareTo(BigDecimal.ZERO) == 0) {
                    indicatorExecution.setExecutionPercentage(BigDecimal.ZERO);
                } else {
                    indicatorExecution.setExecutionPercentage(indicatorExecution.getTotalExecution().divide(indicatorExecution.getTarget(), 4, RoundingMode.HALF_UP));
                }
            } else {
                indicatorExecution.setExecutionPercentage(null);
            }
        }
        this.updateIndicatorExecutionBudget(indicatorExecution);
    }

    private void updateIndicatorExecutionBudget(IndicatorExecution indicatorExecution) {
        if (indicatorExecution.getKeepBudget()) {
            Optional<BigDecimal> totalExecutedOpt = indicatorExecution
                    .getQuarters().stream().
                    flatMap(quarter -> quarter.getMonths().stream())
                    .filter(month -> month.getState().equals(State.ACTIVO) && month.getUsedBudget() != null)
                    .map(Month::getUsedBudget)
                    .reduce(BigDecimal::add);
            BigDecimal totalExecuted = totalExecutedOpt.orElse(BigDecimal.ZERO);
            BigDecimal assignedBudget = indicatorExecution.getAssignedBudget() != null ? indicatorExecution.getAssignedBudget() : BigDecimal.ZERO;
            BigDecimal result = assignedBudget.subtract(totalExecuted).setScale(2, RoundingMode.HALF_UP);
            indicatorExecution.setAvailableBudget(result);
            indicatorExecution.setTotalUsedBudget(totalExecuted);

        }
    }

    public List<IndicatorExecutionWeb> getPerformanceIndicatorExecutionsAdministrationByProjectId(Long projectId) throws GeneralAppException {
        List<IndicatorExecution> ies = this.indicatorExecutionDao.getPerformanceIndicatorExecutionsByProjectId(projectId);
        return this.modelWebTransformationService.indicatorExecutionsToIndicatorExecutionsWeb(ies, true);
    }

    public void validatePerformanceIndicatorAssignationToProject(IndicatorExecutionAssigmentWeb indicatorExecutionWeb) throws GeneralAppException {
        //noinspection DuplicatedCode
        validatePerformanceIndicatorAssignation(indicatorExecutionWeb);

        if (indicatorExecutionWeb.getProject() == null || indicatorExecutionWeb.getProject().getId() == null) {
            throw new GeneralAppException("El proyecto asignado es nulo.", Response.Status.BAD_REQUEST);
        }
    }

    public IndicatorExecutionWeb getResumeAdministrationPerformanceIndicatorById(Long id, boolean getProject) throws GeneralAppException {
        return this.modelWebTransformationService.
                indicatorExecutionToIndicatorExecutionWeb(this.indicatorExecutionDao.getPerformanceIndicatorExecutionById(id), getProject);
    }

    public Long updateMonthValues(Long indicatorExecutionId, MonthValuesWeb monthValuesWeb) throws GeneralAppException {
        // get indicator execution id
        IndicatorExecution indicatorExecution = this.indicatorExecutionDao.getByIdWithIndicatorValues(indicatorExecutionId);
        if (monthValuesWeb.getMonth() == null || monthValuesWeb.getMonth().getId() == null) {
            throw new GeneralAppException("Llamada mal estructurada (month es nulo)", Response.Status.BAD_REQUEST);
        }
        if (monthValuesWeb.getIndicatorValuesMap() == null || monthValuesWeb.getIndicatorValuesMap().isEmpty()) {
            throw new GeneralAppException("Llamada mal estructurada (no valores )", Response.Status.BAD_REQUEST);
        }
        if (indicatorExecution == null) {
            throw new GeneralAppException("No se pudo encontrar el indicador (indicatorExecutionId:" + indicatorExecutionId + ")", Response.Status.BAD_REQUEST);
        }
        // update values
        Month monthToUpdate = null;
        for (Quarter quarter : indicatorExecution.getQuarters()) {
            Optional<Month> monthToUpdateOp = quarter.getMonths().stream()
                    .filter(month -> monthValuesWeb.getMonth().getId().equals(month.getId()))
                    .findFirst();
            if (monthToUpdateOp.isPresent()) {
                monthToUpdate = monthToUpdateOp.get();
                break;
            }
        }
        if (monthToUpdate == null) {
            throw new GeneralAppException("No se pudo encontrar el mes (monthId:" + monthValuesWeb.getMonth().getId() + ")", Response.Status.BAD_REQUEST);
        }
        monthToUpdate.setCommentary(monthValuesWeb.getMonth().getCommentary());
        // para afterupdate
        if (indicatorExecution.getIndicator() != null && indicatorExecution.getIndicator().getBlockAfterUpdate()) {
            monthToUpdate.setBlockUpdate(true);
        } else if (monthValuesWeb.getMonth().getChecked() != null && monthValuesWeb.getMonth().getChecked() != monthToUpdate.getChecked()) {
            if (!monthValuesWeb.getMonth().getChecked()) {
                monthToUpdate.setBlockUpdate(false);
                // send message laertinf parner /responsable
                this.sendReviewDataAlertEmailMessage(indicatorExecution, monthToUpdate);
            } else {
                monthToUpdate.setBlockUpdate(true);
            }

        }

        monthToUpdate.setChecked(monthValuesWeb.getMonth().getChecked());
        monthToUpdate.setSourceOther(monthValuesWeb.getMonth().getSourceOther());
        monthToUpdate.setSources(new HashSet<>());
        monthToUpdate.setUsedBudget(monthValuesWeb.month.getUsedBudget());

        if (CollectionUtils.isNotEmpty(monthValuesWeb.getMonth().getSources())) {

            for (SourceType source : monthValuesWeb.getMonth().getSources()) {
                monthToUpdate.addSource(source);
            }
        }

        List<IndicatorValueWeb> totalIndicatorValueWebs = new ArrayList<>();
        List<Object> newIndicatorValues = new ArrayList<>();
        List<Object> oldIndicatorValues = new ArrayList<>();
        IndicatorType indicatorType=indicatorExecution.getIndicatorType();
        boolean auditChange=false;
        monthValuesWeb.getIndicatorValuesMap().forEach((dissagregationType, indicatorValueWebs) -> {
            if (indicatorValueWebs != null) {
                totalIndicatorValueWebs.addAll(indicatorValueWebs);
            }
        });
        for (IndicatorValueWeb indicatorValueWeb : totalIndicatorValueWebs) {
            Optional<IndicatorValue> valueToUpdateOp = monthToUpdate.getIndicatorValues().stream().filter(indicatorValue -> indicatorValue.getId().equals(indicatorValueWeb.getId())).findFirst();
            if (valueToUpdateOp.isPresent()) {
                IndicatorValue valueToUpdate = valueToUpdateOp.get();
                //agrego valores anteriores a lista de auditoria
                boolean isnewValue=false;
                Object oldValues;
                if(valueToUpdate.getValue()!=null){
                    if(valueToUpdate.getValue().compareTo(indicatorValueWeb.getValue()) !=0){
                        if(indicatorExecution.getProject() == null){
                                oldValues = indicatorExecutionDao.findIndicatorDirectImplementationValuesById(valueToUpdate.getId());
                        }else {
                                if(indicatorType==IndicatorType.GENERAL){
                                    oldValues = indicatorExecutionDao.findGeneralIndicatorDissagregationValuesById(valueToUpdate.getId());
                                }else{
                                    oldValues = indicatorExecutionDao.findIndicatorDissagregationValuesById(valueToUpdate.getId());
                                }
                        }
                            oldIndicatorValues.add(oldValues);
                            isnewValue = true;
                            auditChange = true;
                    }
                }else{
                    //solo agrego cambios si el valor a actualizar es diferente de cero
                    boolean isValueNonZero = indicatorValueWeb.getValue().compareTo(BigDecimal.ZERO) != 0;
                    if (isValueNonZero) {
                        if(indicatorExecution.getProject() == null){
                            oldValues = indicatorExecutionDao.findIndicatorDirectImplementationValuesById(valueToUpdate.getId());
                        }else {
                            if(indicatorType==IndicatorType.GENERAL){
                                oldValues = indicatorExecutionDao.findGeneralIndicatorDissagregationValuesById(valueToUpdate.getId());
                            }else{
                                oldValues = indicatorExecutionDao.findIndicatorDissagregationValuesById(valueToUpdate.getId());
                            }
                        }
                        oldIndicatorValues.add(oldValues);
                        isnewValue = true;
                        auditChange = true;
                    }

                }

                //
                valueToUpdate.setValue(indicatorValueWeb.getValue());
                valueToUpdate.setNumeratorValue(indicatorValueWeb.getNumeratorValue());
                valueToUpdate.setDenominatorValue(indicatorValueWeb.getDenominatorValue());
                //agrego valores nuevos a lista de auditoria
                if(isnewValue){
                    Object newValues;
                    if(indicatorExecution.getProject() == null){
                        newValues = indicatorExecutionDao.findIndicatorDirectImplementationValuesById(valueToUpdate.getId());
                    }else {
                        if(indicatorType==IndicatorType.GENERAL){
                            newValues = indicatorExecutionDao.findGeneralIndicatorDissagregationValuesById(valueToUpdate.getId());
                        }else{
                            newValues = indicatorExecutionDao.findIndicatorDissagregationValuesById(valueToUpdate.getId());
                        }
                    }
                    newIndicatorValues.add(newValues);
                }
                //
            } else {
                throw new GeneralAppException("No se pudo encontrar el valor (valueId:" + indicatorValueWeb.getId() + ")", Response.Status.BAD_REQUEST);
            }
        }


        if (CollectionUtils.isNotEmpty(monthValuesWeb.getCustomDissagregationValues())) {
            List<IndicatorValueCustomDissagregationWeb> totalIndicatorValueCustomDissagregationWebs = new ArrayList<>();
            monthValuesWeb.getCustomDissagregationValues().forEach(customDissagregationValuesWeb -> totalIndicatorValueCustomDissagregationWebs.addAll(customDissagregationValuesWeb.getIndicatorValuesCustomDissagregation()));
            for (IndicatorValueCustomDissagregationWeb totalIndicatorValueCustomDissagregationWeb : totalIndicatorValueCustomDissagregationWebs) {
                Optional<IndicatorValueCustomDissagregation> indicatorValueCustomDissagregationOp = monthToUpdate.getIndicatorValuesIndicatorValueCustomDissagregations().stream().filter(indicatorValueCustomDissagregation -> totalIndicatorValueCustomDissagregationWeb.getId().equals(indicatorValueCustomDissagregation.getId())).findFirst();
                if (indicatorValueCustomDissagregationOp.isPresent()) {
                    IndicatorValueCustomDissagregation valueToUpdate = indicatorValueCustomDissagregationOp.get();
                    //agrego valores anteriores y actuales a listas de auditoria
                    boolean isnewValue=false;
                    Object oldValues;
                    if(valueToUpdate.getValue()!=null){
                        if(valueToUpdate.getValue().compareTo(totalIndicatorValueCustomDissagregationWeb.getValue()) !=0){
                            // Verificar si el valor a actualizar es nulo o tiene un valor diferente
                            if(indicatorExecution.getProject() == null){
                                oldValues = indicatorExecutionDao.findIndicatorDirectImplementationCustomDissValuesById(valueToUpdate.getId());
                            }else {
                                if(indicatorType==IndicatorType.GENERAL){
                                    oldValues=indicatorExecutionDao.findGeneralIndicatorCustomDissagregationValuesById(valueToUpdate.getId());
                                }else{
                                    oldValues=indicatorExecutionDao.findIndicatorCustomDissagregationValuesById(valueToUpdate.getId());
                                }
                            }
                            oldIndicatorValues.add(oldValues);
                            isnewValue = true;
                            auditChange = true;

                        }

                    }else{
                        boolean isValueNonZero = totalIndicatorValueCustomDissagregationWeb.getValue().compareTo(BigDecimal.ZERO) != 0;
                        if (isValueNonZero) {
                            if(indicatorExecution.getProject() == null){
                                oldValues = indicatorExecutionDao.findIndicatorDirectImplementationCustomDissValuesById(valueToUpdate.getId());
                            }else {
                                if(indicatorType==IndicatorType.GENERAL){
                                    oldValues=indicatorExecutionDao.findGeneralIndicatorCustomDissagregationValuesById(valueToUpdate.getId());
                                }else{
                                    oldValues=indicatorExecutionDao.findIndicatorCustomDissagregationValuesById(valueToUpdate.getId());
                                }
                            }
                            isnewValue = true;
                            auditChange = true;
                            oldIndicatorValues.add(oldValues);

                        }

                    }

                    //
                    valueToUpdate.setValue(totalIndicatorValueCustomDissagregationWeb.getValue());
                    valueToUpdate.setNumeratorValue(totalIndicatorValueCustomDissagregationWeb.getNumeratorValue());
                    valueToUpdate.setDenominatorValue(totalIndicatorValueCustomDissagregationWeb.getDenominatorValue());
                    //agrego valores nuevos a lista de auditoria
                    if(isnewValue){
                        Object newValues;
                        if(indicatorExecution.getProject() == null){
                            newValues = indicatorExecutionDao.findIndicatorDirectImplementationCustomDissValuesById(valueToUpdate.getId());
                        }else {
                            if(indicatorType==IndicatorType.GENERAL){
                                newValues=indicatorExecutionDao.findGeneralIndicatorCustomDissagregationValuesById(valueToUpdate.getId());
                            }else{
                                newValues=indicatorExecutionDao.findIndicatorCustomDissagregationValuesById(valueToUpdate.getId());
                            }
                        }
                        newIndicatorValues.add(newValues);
                    }
                    //
                } else {
                    throw new GeneralAppException("No se pudo encontrar el valor (valueId:" + totalIndicatorValueCustomDissagregationWeb.getId() + ")", Response.Status.BAD_REQUEST);
                }
            }
        }
        this.updateIndicatorExecutionTotals(indicatorExecution);
        if(auditChange){
            String projectCode=null;
            String indicatorCode;
            if(indicatorExecution.getProject()!=null){
                projectCode = indicatorExecution.getProject().getCode();
            }
            if(indicatorExecution.getIndicator()!=null){
                indicatorCode = indicatorExecution.getIndicator().getCode();
            }else{
                indicatorCode=indicatorExecution.getProject().getPeriod().getGeneralIndicator().getId().toString();
            }
            auditService.logAction("Reporte", projectCode, indicatorCode, AuditAction.REPORT, oldIndicatorValues, newIndicatorValues,null, null, State.ACTIVO);
        }


        this.saveOrUpdate(indicatorExecution);
        return indicatorExecution.getId();
    }

    private void sendReviewDataAlertEmailMessage(IndicatorExecution indicatorExecution, Month monthToUpdate) {
        String messageText;
        String destinationAdress;
        String destinationCopy;
        if (indicatorExecution.getProject() != null) {
            // is for partner
            // al users partners
            Long organizationId = indicatorExecution.getProject().getOrganization().getId();
            List<User> partnerUsers = this.userService.getActivePartnerUsers(organizationId);
            destinationAdress = partnerUsers.stream().map(User::getEmail).collect(Collectors.joining(", "));
            destinationCopy = indicatorExecution.getProject().getFocalPoint().getEmail();
            messageText = "<p>Estimado/a colega:</p> " +
                    "<p>El punto focal de su proyecto ha solicitado la revisi&oacute;n de los siguientes datos:</p> " +
                    "<p>Socio: " + indicatorExecution.getProject().getOrganization().getDescription() + "</p> " +
                    "<p>Proyecto: " + indicatorExecution.getProject().getName() + "</p> " +
                    "<p>Mes: " + monthToUpdate.getMonth() + "</p> " +
                    "<p>Indicador: " +
                    (indicatorExecution.getIndicator() != null ? (indicatorExecution.getIndicator().getCode() + "-" + indicatorExecution.getIndicator().getDescription()) : "Indicador General") + "</p> " +
                    "<p>Para mayor detalle por favor comun&iacute;quese con el punto focal de su proyecto: <a href=\"mailto:" + indicatorExecution.getProject().getFocalPoint().getEmail() + "\">" + indicatorExecution.getProject().getFocalPoint().getName() + "</a></p> " +
                    "<p>Nota: Este correo es generado automaticamente por el Sistema OSMOSYS, por favor no contestar a este remitente.</p>";

        } else {
            destinationAdress = indicatorExecution.getAssignedUser().getEmail();
            destinationCopy = indicatorExecution.getSupervisorUser().getEmail();
            messageText = "<p>Estimado/a colega:</p> " +
                    "<p>Se ha solicitado la revisi&oacute;n de los siguientes datos:</p> " +
                    "<p>Oficina: " + indicatorExecution.getReportingOffice().getDescription() + "</p> " +
                    "<p>Mes: " + monthToUpdate.getMonth() + "</p> " +
                    "<p>Indicador: " +
                    (indicatorExecution.getIndicator() != null ? (indicatorExecution.getIndicator().getCode() + "-" + indicatorExecution.getIndicator().getDescription()) : "Indicador General") + "</p> " +
                    "<p>Para mayor detalle por favor comun&iacute;quese con el verificador de este indicador: <a href=\"mailto:" + indicatorExecution.getSupervisorUser().getEmail() + "\">" + indicatorExecution.getSupervisorUser().getName() + "</a></p> " +
                    "<p>Nota: Este correo es generado automaticamente por el Sistema OSMOSYS, por favor no contestar a este remitente.</p>";

        }
        this.asyncService.sendEmail(destinationAdress, destinationCopy, "Solicitud de Revisiòn de Datos OSMOSYS", messageText);
    }


    public void updateIndicatorExecutionProjectDates(Project project, LocalDate newStartDate, LocalDate newEndDate) throws GeneralAppException {
        List<IndicatorExecution> indicatorExecutions = this.indicatorExecutionDao.getIndicatorExecutionsByProjectId(project.getId());

        List<Canton> cantones = project.getProjectLocationAssigments().stream().filter(projectLocationAssigment -> projectLocationAssigment.getState().equals(State.ACTIVO)).map(ProjectLocationAssigment::getLocation).collect(Collectors.toList());

        for (IndicatorExecution indicatorExecution : indicatorExecutions) {
            Long periodId = indicatorExecution.getPeriod().getId();
            Period period = periodService.getWithAllDataById(periodId);
            Indicator indicator;

            // recupero las asignaciones del proyecto para indicadores
            List<DissagregationAssignationToIndicatorInterface> dissagregationAssignations;
            List<CustomDissagregation> customDissagregations;
            if (indicatorExecution.getIndicatorType().equals(IndicatorType.GENERAL)) {
                indicator = null;
                GeneralIndicator generalIndicator = this.generalIndicatorService.getByPeriodIdAndState(project.getPeriod().getId(), State.ACTIVO);
                dissagregationAssignations = generalIndicator.getDissagregationAssignationsToGeneralIndicator()
                        .stream().filter(dissagregationAssignationToGeneralIndicator -> dissagregationAssignationToGeneralIndicator.getState().equals(State.ACTIVO)).collect(Collectors.toList());
                customDissagregations = new ArrayList<>();
            } else {
                // asignacion al indicador
                indicator = indicatorExecution.getIndicator();
                dissagregationAssignations = indicator.getDissagregationsAssignationToIndicator()
                        .stream()
                        .filter(dissagregationAssignationToIndicator -> dissagregationAssignationToIndicator.getState().equals(State.ACTIVO)
                                && dissagregationAssignationToIndicator.getPeriod().getId().equals(period.getId()))
                        .collect(Collectors.toList());


                customDissagregations = indicator
                        .getCustomDissagregationAssignationToIndicators().stream()
                        .filter(customDissagregationAssignationToIndicatorExecution ->
                                customDissagregationAssignationToIndicatorExecution.getState().equals(State.ACTIVO)
                                && customDissagregationAssignationToIndicatorExecution.getPeriod().getId().equals(period.getId())
                        )
                        .map(CustomDissagregationAssignationToIndicator::getCustomDissagregation).collect(Collectors.toList());
            }

            // manejo de desagregaciones
            Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> dissagregationsMap = this.getDissagregationTypeOptionsMap(new ArrayList<>(dissagregationAssignations), cantones, indicator, period, indicatorExecution);


            Set<Quarter> quartersOrg = indicatorExecution.getQuarters();
            List<YearQuarter> newQuarters = this.dateUtils.calculateQuarter(newStartDate, newEndDate);
            // veo los quarters nuevos
            for (YearQuarter newQuarter : newQuarters) {
                Optional<Quarter> foundQuarterOp = quartersOrg.stream().filter(quarter -> {
                    try {
                        return quarter.getYear().equals(newQuarter.getYear())
                                && quarter.getQuarter().equals(QuarterEnum.getByQuarterNumber(newQuarter.getQuarterValue()));
                    } catch (GeneralAppException e) {
                        return false;
                    }
                }).findFirst();

                // si el quarter ya existe
                if (foundQuarterOp.isPresent()) {
                    Quarter foundQuarter = foundQuarterOp.get();
                    foundQuarter.setState(State.ACTIVO);
                    List<MonthEnum> monthsEnums = MonthEnum.getMonthsByQuarter(foundQuarter.getQuarter());
                    // veo los meses q deben existir
                    monthsEnums = monthsEnums.stream().filter(monthEnum -> {
                        LocalDate firstDay = LocalDate.of(foundQuarter.getYear(), monthEnum.getOrder(), 1);
                        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());
                        return
                                firstDay.isBefore(newEndDate.plusDays(1))
                                        && lastDay.isAfter(newStartDate.minusDays(1));

                    }).collect(Collectors.toList());
                    // meses presentes
                    for (Month month : foundQuarter.getMonths()) {
                        Optional<MonthEnum> presentMonth = monthsEnums.stream().filter(monthEnum -> monthEnum.equals(month.getMonth())).findFirst();
                        // busco los meses
                        if (presentMonth.isPresent()) {
                            // si existe activo
                            month.setState(State.ACTIVO);
                            for (IndicatorValue indicatorValue : month.getIndicatorValues()) {
                                indicatorValue.setState(State.ACTIVO);
                            }
                            for (IndicatorValueCustomDissagregation indicatorValuesIndicatorValueCustomDissagregation : month.getIndicatorValuesIndicatorValueCustomDissagregations()) {
                                indicatorValuesIndicatorValueCustomDissagregation.setState(State.ACTIVO);
                            }
                        } else {
                            // si no es parte de las fechas desactivo
                            month.setState(State.INACTIVO);
                            for (IndicatorValue indicatorValue : month.getIndicatorValues()) {
                                indicatorValue.setState(State.INACTIVO);
                            }
                            for (IndicatorValueCustomDissagregation indicatorValuesIndicatorValueCustomDissagregation : month.getIndicatorValuesIndicatorValueCustomDissagregations()) {
                                indicatorValuesIndicatorValueCustomDissagregation.setState(State.INACTIVO);
                            }
                        }
                    }
                    // meses ausentes
                    for (MonthEnum monthEnum : monthsEnums) {
                        Optional<Month> foundMonth = foundQuarter.getMonths().stream().filter(month -> month.getMonth().equals(monthEnum)).findFirst();
                        // estos son lo meses a crear
                        if (foundMonth.isEmpty()) {
                            // creo mes
                            Month newmonth = this.monthService
                                    .createMonth(foundQuarter.getYear(), monthEnum, dissagregationsMap, customDissagregations);
                            foundQuarter.addMonth(newmonth);
                        }
                    }


                } else {
                    // si el quarter no existe

                    this.validateLocationsLocationsInLocationsDissagregations(dissagregationsMap);
                    Quarter newCreatedQuarter = this.quarterService.createQuarter(newQuarter, newStartDate, newEndDate, dissagregationsMap, customDissagregations);
                    indicatorExecution.addQuarter(newCreatedQuarter);
                }
            }
            // veo lo que tengo que desactivar
            for (Quarter quarterOrg : quartersOrg) {
                Optional<YearQuarter> foundOrgQuarter = newQuarters.stream().filter(yearQuarter -> {
                    try {
                        return yearQuarter.getYear() == quarterOrg.getYear()
                                && QuarterEnum.getByQuarterNumber(yearQuarter.getQuarterValue()).equals(quarterOrg.getQuarter());
                    } catch (GeneralAppException e) {
                        return false;
                    }
                }).findFirst();
                if (foundOrgQuarter.isEmpty()) {
                    quarterOrg.setState(State.INACTIVO);
                    for (Month month : quarterOrg.getMonths()) {
                        month.setState(State.INACTIVO);
                        for (IndicatorValue indicatorValue : month.getIndicatorValues()) {
                            indicatorValue.setState(State.INACTIVO);
                        }
                        for (IndicatorValueCustomDissagregation indicatorValuesIndicatorValueCustomDissagregation : month.getIndicatorValuesIndicatorValueCustomDissagregations()) {
                            indicatorValuesIndicatorValueCustomDissagregation.setState(State.INACTIVO);
                        }
                    }
                }
            }
            //order recalculate
            this.setOrderInQuartersAndMonths(indicatorExecution.getQuarters());
            this.updateIndicatorExecutionTotals(indicatorExecution);
            this.saveOrUpdate(indicatorExecution);
        }


    }


    public ProjectService.LocationToActivateDesativate getLocationToActivateDessactivate(IndicatorExecution indicatorExecution, List<CantonWeb> cantonesWeb) {

        Set<Canton> cantonesToActivate = new HashSet<>(this.cantonService.getByIds(cantonesWeb.stream().map(CantonWeb::getId).collect(Collectors.toList())));
        //desactivo los q ya no existen
        Set<Canton> cantonesToDissable = new HashSet<>();
        // cantones que ya existent
        List<Canton> existingCantons = indicatorExecution.getIndicatorExecutionLocationAssigments()
                .stream()
                .map(IndicatorExecutionLocationAssigment::getLocation).collect(Collectors.toList());
        for (Canton existingCanton : existingCantons) {
            Optional<CantonWeb> cantonWebFound = cantonesWeb.stream().filter(cantonWeb -> cantonWeb.getId().equals(existingCanton.getId()))
                    .findFirst();
            if (cantonWebFound.isEmpty()) {
                cantonesToDissable.add(existingCanton);
            }
        }
        return new ProjectService.LocationToActivateDesativate(cantonesToActivate, cantonesToDissable);
    }

    public IndicatorExecutionService.DissagregationOptionsToActivateDesactivate getDissOptionsToActivateDessactivate(IndicatorExecution indicatorExecution, List<StandardDissagregationOptionWeb> dissagregationsOptionsWeb) {

        Set<StandardDissagregationOption> dissOptionsToActivate = new HashSet<>(this.standardDissagregationOptionService.getDissagregationOptionsByIds(dissagregationsOptionsWeb.stream().map(StandardDissagregationOptionWeb::getId).collect(Collectors.toList())));
        //desactivo los q ya no existen
        Set<StandardDissagregationOption> dissOptionsToDissable = new HashSet<>();
        // cantones que ya existent
        List<StandardDissagregationOption> existingDissOptions = indicatorExecution.getIndicatorExecutionDissagregationAssigments()
                .stream()
                .map(IndicatorExecutionDissagregationAssigment::getDisagregationOption).collect(Collectors.toList());
        for (StandardDissagregationOption existingOption : existingDissOptions) {
            Optional<StandardDissagregationOptionWeb> dissOptionWebFound = dissagregationsOptionsWeb.stream().filter(option -> option.getId().equals(existingOption.getId()))
                    .findFirst();
            if (dissOptionWebFound.isEmpty()) {
                dissOptionsToDissable.add(existingOption);
            }
        }
        return new IndicatorExecutionService.DissagregationOptionsToActivateDesactivate(dissOptionsToActivate, dissOptionsToDissable);
    }

    public IndicatorExecutionService.DissagregationOptionsToActivateDesactivate getDissOptionsToActivateDessactivateByIndicatorAssignations(IndicatorExecution indicatorExecution) {
        Set<Long> dissagregationsSelectedOptionsIds = indicatorExecution.getIndicatorExecutionDissagregationAssigments().stream()
                .filter(option -> option.getState().equals(State.ACTIVO))
                .map(option -> option.getDisagregationOption().getId())
                .collect(Collectors.toSet());

        Set<StandardDissagregationOption> dissOptionsToActivate = new HashSet<>();
        Set<StandardDissagregationOption> dissOptionsToDissable = new HashSet<>();
        /*Actualizando por cambios en el Indicador*/
        for (DissagregationAssignationToIndicator dissagregationAssignationToIndicator : indicatorExecution.getIndicator().getDissagregationsAssignationToIndicator()) {
            if(dissagregationAssignationToIndicator.getDissagregationType().equals(DissagregationType.SIN_DESAGREGACION) && Objects.equals(dissagregationAssignationToIndicator.getPeriod().getId(), indicatorExecution.getPeriod().getId()) && dissagregationAssignationToIndicator.getState().equals(State.ACTIVO)){
                dissOptionsToDissable=indicatorExecution.getIndicatorExecutionDissagregationAssigments().stream().map(IndicatorExecutionDissagregationAssigment::getDisagregationOption).collect(Collectors.toSet());
                break;
            }
            for (DissagregationType dissagregationType: dissagregationAssignationToIndicator.getDissagregationType().getSimpleDissagregations()){
                /*AgeDissagregations*/
                if(dissagregationType.equals(DissagregationType.EDAD) && Objects.equals(dissagregationAssignationToIndicator.getPeriod().getId(), indicatorExecution.getPeriod().getId())){
                    List<AgeDissagregationOption> ageOptions=standardDissagregationOptionService.getAgeDissagregationOptionByState(State.ACTIVO);
                    List<AgeDissagregationOption> selectedAgeOptions = ageOptions.stream()
                            .filter(ageOption -> dissagregationsSelectedOptionsIds.contains(ageOption.getId()))
                            .collect(Collectors.toList());
                    if(selectedAgeOptions.isEmpty() && dissagregationAssignationToIndicator.getState().equals(State.ACTIVO)){
                        dissOptionsToActivate.addAll(ageOptions);
                    }
                    if(!selectedAgeOptions.isEmpty()&& dissagregationAssignationToIndicator.getState().equals(State.INACTIVO)){
                        dissOptionsToDissable.addAll(selectedAgeOptions);
                    }
                }
                /*GenderDissagregations*/
                if(dissagregationType.equals(DissagregationType.GENERO) && Objects.equals(dissagregationAssignationToIndicator.getPeriod().getId(), indicatorExecution.getPeriod().getId())){
                    List<GenderDissagregationOption> genderOptions=standardDissagregationOptionService.getGenderDissagregationOptionByState(State.ACTIVO);
                    List<GenderDissagregationOption> selectedGenderOptions = genderOptions.stream()
                            .filter(genderOption -> dissagregationsSelectedOptionsIds.contains(genderOption.getId()))
                            .collect(Collectors.toList());
                    if(selectedGenderOptions.isEmpty() && dissagregationAssignationToIndicator.getState().equals(State.ACTIVO)){
                        dissOptionsToActivate.addAll(genderOptions);
                    }
                    if(!selectedGenderOptions.isEmpty()&& dissagregationAssignationToIndicator.getState().equals(State.INACTIVO)){
                        dissOptionsToDissable.addAll(selectedGenderOptions);
                    }
                }
                /*PopulationTypeDissagregations*/
                if(dissagregationType.equals(DissagregationType.TIPO_POBLACION) && Objects.equals(dissagregationAssignationToIndicator.getPeriod().getId(), indicatorExecution.getPeriod().getId())){
                    List<PopulationTypeDissagregationOption> populationTypeOptions=standardDissagregationOptionService.getPopulationTypeOptionsByState(State.ACTIVO);
                    List<PopulationTypeDissagregationOption> selectedPopulationTypeOptions = populationTypeOptions.stream()
                            .filter(populationTypeOption -> dissagregationsSelectedOptionsIds.contains(populationTypeOption.getId()))
                            .collect(Collectors.toList());
                    if(selectedPopulationTypeOptions.isEmpty() && dissagregationAssignationToIndicator.getState().equals(State.ACTIVO)){
                        dissOptionsToActivate.addAll(populationTypeOptions);
                    }
                    if(!selectedPopulationTypeOptions.isEmpty()&& dissagregationAssignationToIndicator.getState().equals(State.INACTIVO)){
                        dissOptionsToDissable.addAll(selectedPopulationTypeOptions);
                    }
                }
                /*CountryOfOriginDissagregations*/
                if(dissagregationType.equals(DissagregationType.PAIS_ORIGEN) && Objects.equals(dissagregationAssignationToIndicator.getPeriod().getId(), indicatorExecution.getPeriod().getId())){
                    List<CountryOfOriginDissagregationOption> countryOfOriginOptions=standardDissagregationOptionService.getCountryOfOriginDissagregationOptionByState(State.ACTIVO);
                    List<CountryOfOriginDissagregationOption> selectedCountryOfOriginOptions = countryOfOriginOptions.stream()
                            .filter(countryOfOriginOption -> dissagregationsSelectedOptionsIds.contains(countryOfOriginOption.getId()))
                            .collect(Collectors.toList());
                    if(selectedCountryOfOriginOptions.isEmpty() && dissagregationAssignationToIndicator.getState().equals(State.ACTIVO)){
                        dissOptionsToActivate.addAll(countryOfOriginOptions);
                    }
                    if(!selectedCountryOfOriginOptions.isEmpty()&& dissagregationAssignationToIndicator.getState().equals(State.INACTIVO)){
                        dissOptionsToDissable.addAll(selectedCountryOfOriginOptions);
                    }
                }
                /*DiversityDissagregations*/
                if(dissagregationType.equals(DissagregationType.DIVERSIDAD) && Objects.equals(dissagregationAssignationToIndicator.getPeriod().getId(), indicatorExecution.getPeriod().getId())){
                    List<DiversityDissagregationOption> diversityOptions=standardDissagregationOptionService.getDiversityeOptionsByState(State.ACTIVO);
                    List<DiversityDissagregationOption> selectedDiversityOptions = diversityOptions.stream()
                            .filter(diversityOption -> dissagregationsSelectedOptionsIds.contains(diversityOption.getId()))
                            .collect(Collectors.toList());
                    if(selectedDiversityOptions.isEmpty() && dissagregationAssignationToIndicator.getState().equals(State.ACTIVO)){
                        dissOptionsToActivate.addAll(diversityOptions);
                    }
                    if(!selectedDiversityOptions.isEmpty()&& dissagregationAssignationToIndicator.getState().equals(State.INACTIVO)){
                        dissOptionsToDissable.addAll(selectedDiversityOptions);
                    }
                }
            }
        }

        if(!dissOptionsToActivate.isEmpty() || !dissOptionsToDissable.isEmpty()){
            return new IndicatorExecutionService.DissagregationOptionsToActivateDesactivate(dissOptionsToActivate, dissOptionsToDissable);
        }

        /*Actualizando por cambios en el Periodo*/
        Set<Long> allDissagregationsSelectedOptionsIds = indicatorExecution.getIndicatorExecutionDissagregationAssigments().stream()
                .map(option -> option.getDisagregationOption().getId())
                .collect(Collectors.toSet());
        for (DissagregationAssignationToIndicator dissagregationAssignationToIndicator : indicatorExecution.getIndicator().getDissagregationsAssignationToIndicator()) {
            if (dissagregationAssignationToIndicator.getDissagregationType().equals(DissagregationType.SIN_DESAGREGACION) && Objects.equals(dissagregationAssignationToIndicator.getPeriod().getId(), indicatorExecution.getPeriod().getId()) && dissagregationAssignationToIndicator.getState().equals(State.ACTIVO)) {
                dissOptionsToDissable = indicatorExecution.getIndicatorExecutionDissagregationAssigments().stream().map(IndicatorExecutionDissagregationAssigment::getDisagregationOption).collect(Collectors.toSet());
                break;
            }
            for (DissagregationType dissagregationType : dissagregationAssignationToIndicator.getDissagregationType().getSimpleDissagregations()) {
                /*AgeDissagregationOptions*/
                if (dissagregationType.equals(DissagregationType.EDAD) && Objects.equals(dissagregationAssignationToIndicator.getPeriod().getId(), indicatorExecution.getPeriod().getId()) && dissagregationAssignationToIndicator.getState().equals(State.ACTIVO)) {
                    Set<PeriodAgeDissagregationOption> periodAgeDissagregationOptions = indicatorExecution.getPeriod().getPeriodAgeDissagregationOptions();
                    for (PeriodAgeDissagregationOption option : periodAgeDissagregationOptions) {
                        if (!allDissagregationsSelectedOptionsIds.contains(option.getDissagregationOption().getId()) && option.getState().equals(State.ACTIVO)) {
                            dissOptionsToActivate.add(option.getAgeDissagregationOption());
                        }
                        if (dissagregationsSelectedOptionsIds.contains(option.getAgeDissagregationOption().getId()) && option.getState().equals(State.INACTIVO)) {
                            dissOptionsToDissable.add(option.getAgeDissagregationOption());
                        }

                    }
                }
                /*GenderDissagregationOptions*/
                if (dissagregationType.equals(DissagregationType.GENERO) && Objects.equals(dissagregationAssignationToIndicator.getPeriod().getId(), indicatorExecution.getPeriod().getId()) && dissagregationAssignationToIndicator.getState().equals(State.ACTIVO)) {
                    Set<PeriodGenderDissagregationOption> periodGenderDissagregationOptions =indicatorExecution.getPeriod().getPeriodGenderDissagregationOptions();
                    for(PeriodGenderDissagregationOption option: periodGenderDissagregationOptions){
                        if(!allDissagregationsSelectedOptionsIds.contains(option.getDissagregationOption().getId()) && option.getState().equals(State.ACTIVO)){
                            dissOptionsToActivate.add(option.getDissagregationOption());
                        }
                        if(dissagregationsSelectedOptionsIds.contains(option.getDissagregationOption().getId()) && option.getState().equals(State.INACTIVO)){
                            dissOptionsToDissable.add(option.getDissagregationOption());
                        }

                    }
                }
                /*PopulationTypeDissagregationOptions*/
                if (dissagregationType.equals(DissagregationType.TIPO_POBLACION) && Objects.equals(dissagregationAssignationToIndicator.getPeriod().getId(), indicatorExecution.getPeriod().getId()) && dissagregationAssignationToIndicator.getState().equals(State.ACTIVO)) {
                    Set<PeriodPopulationTypeDissagregationOption> periodPopulationTypeDissagregationOptions =indicatorExecution.getPeriod().getPeriodPopulationTypeDissagregationOptions();
                    for(PeriodPopulationTypeDissagregationOption option: periodPopulationTypeDissagregationOptions){
                        if(!allDissagregationsSelectedOptionsIds.contains(option.getDissagregationOption().getId()) && option.getState().equals(State.ACTIVO)){
                            dissOptionsToActivate.add(option.getDissagregationOption());
                        }
                        if(dissagregationsSelectedOptionsIds.contains(option.getDissagregationOption().getId()) && option.getState().equals(State.INACTIVO)){
                            dissOptionsToDissable.add(option.getDissagregationOption());
                        }

                    }
                }
                /*CountryOfOriginDissagregationOptions*/
                if (dissagregationType.equals(DissagregationType.PAIS_ORIGEN) && Objects.equals(dissagregationAssignationToIndicator.getPeriod().getId(), indicatorExecution.getPeriod().getId()) && dissagregationAssignationToIndicator.getState().equals(State.ACTIVO)) {
                    Set<PeriodCountryOfOriginDissagregationOption> periodCountryOfOriginDissagregationOptions =indicatorExecution.getPeriod().getPeriodCountryOfOriginDissagregationOptions();
                    for(PeriodCountryOfOriginDissagregationOption option: periodCountryOfOriginDissagregationOptions){
                        if(!allDissagregationsSelectedOptionsIds.contains(option.getDissagregationOption().getId()) && option.getState().equals(State.ACTIVO)){
                            dissOptionsToActivate.add(option.getDissagregationOption());
                        }
                        if(dissagregationsSelectedOptionsIds.contains(option.getDissagregationOption().getId()) && option.getState().equals(State.INACTIVO)){
                            dissOptionsToDissable.add(option.getDissagregationOption());
                        }

                    }
                }
                /*DiversityDissagregationOptions*/
                if (dissagregationType.equals(DissagregationType.DIVERSIDAD) && Objects.equals(dissagregationAssignationToIndicator.getPeriod().getId(), indicatorExecution.getPeriod().getId()) && dissagregationAssignationToIndicator.getState().equals(State.ACTIVO)) {
                    Set<PeriodDiversityDissagregationOption> periodDiversityDissagregationOptions =indicatorExecution.getPeriod().getPeriodDiversityDissagregationOptions();
                    for(PeriodDiversityDissagregationOption option: periodDiversityDissagregationOptions){
                        if(!allDissagregationsSelectedOptionsIds.contains(option.getDissagregationOption().getId()) && option.getState().equals(State.ACTIVO)){
                            dissOptionsToActivate.add(option.getDissagregationOption());
                        }
                        if(dissagregationsSelectedOptionsIds.contains(option.getDissagregationOption().getId()) && option.getState().equals(State.INACTIVO)){
                            dissOptionsToDissable.add(option.getDissagregationOption());
                        }

                    }
                }

            }
        }

        return new IndicatorExecutionService.DissagregationOptionsToActivateDesactivate(dissOptionsToActivate, dissOptionsToDissable);
    }


    public List<IndicatorExecutionWeb> getAllDirectImplementationIndicatorByPeriodId(Long periodId) throws
            GeneralAppException {
        List<IndicatorExecution> indicatorExecutions = this.indicatorExecutionDao.getDirectImplementationIndicatorByPeriodId(periodId);
        return this.modelWebTransformationService.indicatorExecutionsToIndicatorExecutionsWeb(indicatorExecutions, false);
    }

    @SuppressWarnings("DuplicatedCode")
    public Long assignPerformanceIndicatoDirectImplementation(IndicatorExecutionAssigmentWeb
                                                                      indicatorExecutionAssigmentWeb) throws GeneralAppException {
        this.validatePerformanceIndicatorAssignationDirectImplementation(indicatorExecutionAssigmentWeb);

        IndicatorExecution indicatorExecution = new IndicatorExecution();
        Indicator indicator = this.indicatorDao.find(indicatorExecutionAssigmentWeb.getIndicator().getId());
        if (indicator == null) {
            throw new GeneralAppException("Indicador no encontrado " + indicatorExecutionAssigmentWeb.getIndicator().getId(), Response.Status.BAD_REQUEST);
        }
        indicatorExecution.setIndicator(indicator);
        Period period = this.periodService.getWithAllDataById(indicatorExecutionAssigmentWeb.getPeriod().getId());
        if (period == null) {
            throw new GeneralAppException("Periodo no encontrado " + indicatorExecutionAssigmentWeb.getPeriod().getId(), Response.Status.BAD_REQUEST);
        }
        indicatorExecution.setCompassIndicator(indicator.getCompassIndicator());
        indicatorExecution.setIndicatorType(indicator.getIndicatorType());
        indicatorExecution.setState(indicatorExecutionAssigmentWeb.getState());
        indicatorExecution.setPeriod(period);
        indicatorExecution.setKeepBudget(indicatorExecutionAssigmentWeb.getKeepBudget());
        indicatorExecution.setAssignedBudget(indicatorExecutionAssigmentWeb.getAssignedBudget());
        Office office = this.officeService.getById(indicatorExecutionAssigmentWeb.getReportingOffice().getId());
        indicatorExecution.setTarget(indicatorExecutionAssigmentWeb.getTarget() != null ? new BigDecimal(indicatorExecutionAssigmentWeb.getTarget()) : null);
        indicatorExecution.setReportingOffice(office);
        if (office == null) {
            throw new GeneralAppException("Oficina no encontrada " + indicatorExecutionAssigmentWeb.getReportingOffice().getId(), Response.Status.BAD_REQUEST);
        }
        this.assigUsersToIndicatorExecution(indicatorExecution, indicatorExecutionAssigmentWeb);
        List<DissagregationAssignationToIndicator> dissagregationAssignations = indicator.getDissagregationsAssignationToIndicator()
                .stream()
                .filter(dissagregationAssignationToIndicator ->
                        dissagregationAssignationToIndicator.getState().equals(State.ACTIVO)
                                && dissagregationAssignationToIndicator.getPeriod().getId().equals(indicatorExecution.getPeriod().getId())
                ).collect(Collectors.toList());
        // manejo de desagregaciones
        List<Canton> cantones = indicatorExecution.getIndicatorExecutionLocationAssigments().stream().filter(indicatorExecutionLocationAssigment -> indicatorExecutionLocationAssigment.getState().equals(State.ACTIVO))
                .map(IndicatorExecutionLocationAssigment::getLocation).collect(Collectors.toList());
        Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> dissagregationsMap = this.getDissagregationTypeOptionsMap(new ArrayList<>(dissagregationAssignations), cantones, indicator, period, indicatorExecution);


        List<CustomDissagregationAssignationToIndicator> customDissagregationsAssignations =
                indicator.getCustomDissagregationAssignationToIndicators()
                        .stream()
                        .filter(customDissagregationAssignationToIndicator ->
                                customDissagregationAssignationToIndicator.getState().equals(State.ACTIVO)
                                        && customDissagregationAssignationToIndicator.getPeriod().getId().equals(indicatorExecution.getPeriod().getId())
                        ).collect(Collectors.toList());


        List<CustomDissagregation> customDissagregations =
                customDissagregationsAssignations
                        .stream()
                        .map(CustomDissagregationAssignationToIndicator::getCustomDissagregation)
                        .collect(Collectors.toList());
        LocalDate startDate = LocalDate.of(period.getYear(), 1, 1);
        LocalDate endDate = LocalDate.of(period.getYear(), 12, 31);
        Set<Quarter> qs = this.quarterService.createQuarters(startDate, endDate, dissagregationsMap, customDissagregations);
        List<Quarter> qsl = setOrderInQuartersAndMonths(qs);
        for (Quarter quarter : qsl) {
            indicatorExecution.addQuarter(quarter);
        }
        this.saveOrUpdate(indicatorExecution);
        return indicatorExecution.getId();
    }


    public Long updateAssignPerformanceIndicatorDirectImplementation(IndicatorExecutionAssigmentWeb
                                                                             indicatorExecutionAssigmentWeb) throws GeneralAppException {
        if (indicatorExecutionAssigmentWeb.getId() == null) {
            throw new GeneralAppException("No se pudo encontrar la asignación (Id:" + indicatorExecutionAssigmentWeb.getId() + ")", Response.Status.BAD_REQUEST);
        }
        this.validatePerformanceIndicatorAssignationDirectImplementation(indicatorExecutionAssigmentWeb);
        IndicatorExecution indicatorExecution = this.indicatorExecutionDao.find(indicatorExecutionAssigmentWeb.getId());
        if (indicatorExecution == null) {
            throw new GeneralAppException("No se pudo encontrar la asignación (Id:" + indicatorExecutionAssigmentWeb.getId() + ")", Response.Status.BAD_REQUEST);
        }
        Office reportingOffice = this.officeService.getById(indicatorExecutionAssigmentWeb.getReportingOffice().getId());
        if (reportingOffice == null) {
            throw new GeneralAppException("No se pudo encontrar la oficina (Id:" + reportingOffice.getId() + ")", Response.Status.BAD_REQUEST);
        }
        indicatorExecution.setReportingOffice(reportingOffice);

        indicatorExecution.setState(indicatorExecutionAssigmentWeb.getState());
        indicatorExecution.setTarget(indicatorExecutionAssigmentWeb.getTarget() != null ? new BigDecimal(indicatorExecutionAssigmentWeb.getTarget()) : null);

        this.assigUsersToIndicatorExecution(indicatorExecution, indicatorExecutionAssigmentWeb);
        /*  *************budget**********/
        indicatorExecution.setKeepBudget(indicatorExecutionAssigmentWeb.getKeepBudget());
        indicatorExecution.setAssignedBudget(indicatorExecutionAssigmentWeb.getAssignedBudget());

        this.saveOrUpdate(indicatorExecution);
        this.updateIndicatorExecutionTotals(indicatorExecution);
        return indicatorExecution.getId();
    }

    public void assigUsersToIndicatorExecution(IndicatorExecution indicatorExecution, IndicatorExecutionAssigmentWeb indicatorExecutionAssigmentWeb) throws GeneralAppException {
        User assignedUser = this.userService.getById(indicatorExecutionAssigmentWeb.getAssignedUser().getId());
        if (assignedUser == null) {
            throw new GeneralAppException("Usuario responsable no encontrado " + indicatorExecutionAssigmentWeb.getAssignedUser().getId(), Response.Status.BAD_REQUEST);
        }
        indicatorExecution.setAssignedUser(assignedUser);
        if (indicatorExecutionAssigmentWeb.getAssignedUserBackup() != null) {
            User assignedUserBackup = this.userService.getById(indicatorExecutionAssigmentWeb.getAssignedUserBackup().getId());
            if (assignedUserBackup == null) {
                throw new GeneralAppException("Usuario responsable alterno no encontrado " + indicatorExecutionAssigmentWeb.getAssignedUserBackup().getId(), Response.Status.BAD_REQUEST);
            }
            indicatorExecution.setAssignedUserBackup(assignedUserBackup);
        } else {
            indicatorExecution.setAssignedUserBackup(null);
        }
        if (indicatorExecutionAssigmentWeb.getSupervisorUser() != null) {
            User supervisorUser = this.userService.getById(indicatorExecutionAssigmentWeb.getSupervisorUser().getId());
            if (supervisorUser == null) {
                throw new GeneralAppException("Usuario supervisor no encontrado " + indicatorExecutionAssigmentWeb.getSupervisorUser().getId(), Response.Status.BAD_REQUEST);
            }
            indicatorExecution.setSupervisorUser(supervisorUser);
        }
    }

    public void validatePerformanceIndicatorAssignationDirectImplementation(IndicatorExecutionAssigmentWeb
                                                                                    indicatorExecutionWeb) throws GeneralAppException {
        validatePerformanceIndicatorAssignation(indicatorExecutionWeb);

        if (indicatorExecutionWeb.getReportingOffice() == null || indicatorExecutionWeb.getReportingOffice().getId() == null) {
            throw new GeneralAppException("La oficina asignada es obligatorio.", Response.Status.BAD_REQUEST);
        }

        if (indicatorExecutionWeb.getPeriod() == null || indicatorExecutionWeb.getPeriod().getId() == null) {
            throw new GeneralAppException("El periodo asignado es obligatorio.", Response.Status.BAD_REQUEST);
        }
        if (indicatorExecutionWeb.getAssignedUser() == null || indicatorExecutionWeb.getAssignedUser().getId() == null) {
            throw new GeneralAppException("El usuario responsable es obligatorio.", Response.Status.BAD_REQUEST);
        }
        // existe indicador para esta officina para
        IndicatorExecution assimentFound = this.indicatorExecutionDao.getByIndicatorIdAndOfficeIdAndPeriodId(indicatorExecutionWeb.getIndicator().getId(), indicatorExecutionWeb.getReportingOffice().getId(), indicatorExecutionWeb.getPeriod().getId());
        if (assimentFound != null && !assimentFound.getId().equals(indicatorExecutionWeb.getId())) {
            throw new GeneralAppException("Este indicador ya se encuentra asignado para esta oficina. " + indicatorExecutionWeb.getIndicator().getCode(), Response.Status.BAD_REQUEST);
        }

    }

    private void validatePerformanceIndicatorAssignation(IndicatorExecutionAssigmentWeb indicatorExecutionWeb) throws GeneralAppException {
        if (indicatorExecutionWeb == null) {
            throw new GeneralAppException("La asignación es obligatorio", Response.Status.BAD_REQUEST);
        }

        if (indicatorExecutionWeb.getIndicator() == null || indicatorExecutionWeb.getIndicator().getId() == null) {
            throw new GeneralAppException("Indicador asignado es obligatorio.", Response.Status.BAD_REQUEST);
        }

        if (indicatorExecutionWeb.getState() == null) {
            throw new GeneralAppException("El estado es obligatorio.", Response.Status.BAD_REQUEST);
        }
    }

    public List<IndicatorExecutionWeb> getDirectImplementationIndicatorByPeriodIdResponsableIdSupervisorIdAndOfficeId
            (
                    Long userId,
                    Long periodId,
                    Long officeId,
                    Boolean supervisor,
                    Boolean responsible,
                    Boolean backup) throws GeneralAppException {
        List<IndicatorExecution> indicatorExecutions = this.indicatorExecutionDao.getDirectImplementationIndicatorByPeriodIdResponsableIdSupervisorIdAndOfficeId(
                userId, periodId, officeId, supervisor, responsible, backup
        );
        return this.modelWebTransformationService.indicatorExecutionsToIndicatorExecutionsWeb(indicatorExecutions, false);
    }

    public List<IndicatorExecutionWeb> getActivePartnersIndicatorExecutionsByProjectId(Long projectId) throws GeneralAppException {
        List<IndicatorExecution> indicatorExecutions = this.indicatorExecutionDao.getActivePartnersIndicatorExecutionsByProjectId(projectId);
        return this.modelWebTransformationService
                .indicatorExecutionsToIndicatorExecutionsWeb(indicatorExecutions, true);
    }


    public List<IndicatorExecutionWeb> getActiveProjectIndicatorExecutionsByPeriodId(Long periodId) throws GeneralAppException {
        List<IndicatorExecution> indicatorExecutions = this.indicatorExecutionDao.getActivePartnersIndicatorExecutionsByPeriodId(periodId);
        return this.modelWebTransformationService
                .indicatorExecutionsToIndicatorExecutionsWeb(indicatorExecutions, true);
    }

    public List<IndicatorExecutionWeb> getDirectImplementationIndicatorExecutionsByIds(List<Long> indicatorExecutionIds, State state) throws GeneralAppException {
        return this.modelWebTransformationService
                .indicatorExecutionsToIndicatorExecutionsWeb(
                        this.indicatorExecutionDao.getDirectImplementationIndicatorExecutionsByIdsAndState(indicatorExecutionIds, state), false);
    }

    public Long updatePartnerIndicatorExecutionLocationAssigment(Long indicatorExecutionId, List<CantonWeb> cantonesWeb) throws GeneralAppException {
        if (indicatorExecutionId == null) {
            throw new GeneralAppException("indicador execution id es dato obligatorio", Response.Status.BAD_REQUEST);
        }
        if (CollectionUtils.isEmpty(cantonesWeb)) {
            throw new GeneralAppException("Al menos debe haber un cantón", Response.Status.BAD_REQUEST);
        }
        // recupero el ie
        IndicatorExecution ie = this.indicatorExecutionDao.getPartnerIndicatorExecutionById(indicatorExecutionId);
        if (ie == null) {
            throw new GeneralAppException("No se pudo encontrar el indicador (indicatorExecutionId =" + indicatorExecutionId + ")", Response.Status.BAD_REQUEST);
        }
        if (ie.getProject() == null) {
            throw new GeneralAppException("Este indicador no es ejecutado por un Socio (indicatorExecutionId =" + indicatorExecutionId + ")", Response.Status.BAD_REQUEST);
        }

        if (ie.getIndicatorType().equals(IndicatorType.GENERAL)) {
            ProjectService.LocationToActivateDesativate locatoinActiveNoActive = this.projectService.setLocationsInProject(ie.getProject(), cantonesWeb);
            Project project = ie.getProject();
            for (IndicatorExecution indicatorExecution : project.getIndicatorExecutions()) {
                this.updateIndicatorExecutionsLocations(indicatorExecution, locatoinActiveNoActive.locationsToActivate, locatoinActiveNoActive.locationsToDissable);
            }
        } else {
            ProjectService.LocationToActivateDesativate locationToActivateDessactivate = this.getLocationToActivateDessactivate(ie, cantonesWeb);
            this.updateIndicatorExecutionsLocations(ie, locationToActivateDessactivate.locationsToActivate, locationToActivateDessactivate.locationsToDissable);
        }

        return ie.getId();
    }


    /*Actualizar lugares y opciones de desagregación en conjunto*/
    public Long updatePartnerIndicatorExecutionLocationsAndDissagregationOptionsAssigment(Long indicatorExecutionId, List<CantonWeb> cantonesWeb, List<StandardDissagregationOptionWeb> indicatorExecutionDissagregationOptionsWeb) throws GeneralAppException {
        if (indicatorExecutionId == null) {
            throw new GeneralAppException("indicador execution id es dato obligatorio", Response.Status.BAD_REQUEST);
        }
        if (CollectionUtils.isEmpty(cantonesWeb)) {
            throw new GeneralAppException("Al menos debe haber un cantón", Response.Status.BAD_REQUEST);
        }
        // recupero el ie
        IndicatorExecution ie = this.indicatorExecutionDao.getPartnerIndicatorExecutionById(indicatorExecutionId);
        if (ie == null) {
            throw new GeneralAppException("No se pudo encontrar el indicador (indicatorExecutionId =" + indicatorExecutionId + ")", Response.Status.BAD_REQUEST);
        }
        if (ie.getProject() == null) {
            throw new GeneralAppException("Este indicador no es ejecutado por un Socio (indicatorExecutionId =" + indicatorExecutionId + ")", Response.Status.BAD_REQUEST);
        }

        //seteo locatios para ie
        Map<DissagregationType, Map<DissagregationType, List<StandardDissagregationOption>>> periodDissagregationMap;
        Period period = this.periodService.getWithAllDataById(ie.getPeriod().getId());

        if (ie.getIndicatorType().equals(IndicatorType.GENERAL)) {
            ProjectService.LocationToActivateDesativate locatoinActiveNoActive = this.projectService.setLocationsInProject(ie.getProject(), cantonesWeb);
            Project project = ie.getProject();
            for (IndicatorExecution indicatorExecution : project.getIndicatorExecutions()) {
                this.updateIndicatorExecutionsLocations(indicatorExecution, locatoinActiveNoActive.locationsToActivate, locatoinActiveNoActive.locationsToDissable);
            }
        } else {
            periodDissagregationMap = this.getPeriodDessagregationMap(false, period, ie.getIndicator());
            if (periodDissagregationMap == null) return ie.getId();
            ProjectService.LocationToActivateDesativate locationToActivateDessactivate = this.getLocationToActivateDessactivate(ie, cantonesWeb);
            this.updateIndicatorExecutionLocationsAssignations(ie, locationToActivateDessactivate.locationsToActivate, locationToActivateDessactivate.locationsToDissable);
            if(!indicatorExecutionDissagregationOptionsWeb.isEmpty()){
                IndicatorExecutionService.DissagregationOptionsToActivateDesactivate dissOptionsToActivateDessactivate = this.getDissOptionsToActivateDessactivate(ie, indicatorExecutionDissagregationOptionsWeb);
                this.updateIndicatorExecutionDissagregationOptionAssignations(ie, dissOptionsToActivateDessactivate.dissagregationOptionToActivate, dissOptionsToActivateDessactivate.dissagregationOptionToDissable);
            }
            this.setStandardDissagregationOptionsForIndicatorExecutions(ie, periodDissagregationMap);
            this.setStandardDissagregationSelectedOptionsForIndicatorExecutions(ie, periodDissagregationMap);
            this.quarterService.updateQuarterDissagregations(ie, periodDissagregationMap, null);
            this.updateIndicatorExecutionTotals(ie);
            this.saveOrUpdate(ie);
        }

        return ie.getId();
    }
    /****************/

    static class DissagregationOptionsToActivateDesactivate {
        public final Set<StandardDissagregationOption> dissagregationOptionToActivate;
        public final Set<StandardDissagregationOption> dissagregationOptionToDissable;

        public DissagregationOptionsToActivateDesactivate(Set<StandardDissagregationOption> dissagregationOptionToActivate, Set<StandardDissagregationOption> dissagregationOptionToDissable) {
            this.dissagregationOptionToActivate = dissagregationOptionToActivate;
            this.dissagregationOptionToDissable = dissagregationOptionToDissable;
        }
    }


    public Long updateDirectImplementationIndicatorExecutionLocationAssigment(Long indicatorExecutionId, List<CantonWeb> cantonesWeb) throws GeneralAppException {
        if (indicatorExecutionId == null) {
            throw new GeneralAppException("indicador execution id es dato obligatorio", Response.Status.BAD_REQUEST);
        }
        if (CollectionUtils.isEmpty(cantonesWeb)) {
            throw new GeneralAppException("Al menos debe haber un cantón", Response.Status.BAD_REQUEST);
        }
        // recupero el ie
        IndicatorExecution ie = this.indicatorExecutionDao.getDirectImplementationIndicatorExecutionsById(indicatorExecutionId);
        if (ie == null) {
            throw new GeneralAppException("No se pudo encontrar el indicador (indicatorExecutionId =" + indicatorExecutionId + ")", Response.Status.BAD_REQUEST);
        }
        ProjectService.LocationToActivateDesativate locationToActivateDessactivate = this.getLocationToActivateDessactivate(ie, cantonesWeb);

        this.updateIndicatorExecutionsLocations(ie, locationToActivateDessactivate.locationsToActivate, locationToActivateDessactivate.locationsToDissable);


        this.saveOrUpdate(ie);
        return ie.getId();
    }


    public List<IndicatorExecutionWeb> getActiveProjectIndicatorExecutionsByPeriodYear(Integer year) throws GeneralAppException {
        Period period = this.periodService.getByYear(year);
        if (period == null) {
            throw new GeneralAppException("Periodo no encontrado", Response.Status.NOT_FOUND);
        }
        return this.getActiveProjectIndicatorExecutionsByPeriodId(period.getId());
    }

    public List<IndicatorExecutionWeb> getActiveDirectImplementationIndicatorExecutionsByPeriodYear(Integer year) throws GeneralAppException {

        Period period = this.periodService.getByYear(year);
        if (period == null) {
            throw new GeneralAppException("Periodo no encontrado", Response.Status.NOT_FOUND);
        }
        return this.modelWebTransformationService.indicatorExecutionsToIndicatorExecutionsWeb(
                this.indicatorExecutionDao.getDirectImplementationIndicatorByPeriodIdAndState(period.getId(), State.ACTIVO), false
        );

    }




    public void updateIndicatorExecutionLocationsAssignations(
            IndicatorExecution indicatorExecution,
            Set<Canton> locationsToActivate,
            Set<Canton> locationsToDissable
    ) {
        locationsToActivate.forEach(cantonToActivate -> {
            Optional<IndicatorExecutionLocationAssigment> indicatorExecutionLocationAssigmentToActivateOpt =
                    indicatorExecution.getIndicatorExecutionLocationAssigments()
                            .stream()
                            .filter(indicatorExecutionLocationAssigment ->
                                    cantonToActivate.getId().equals(indicatorExecutionLocationAssigment.getLocation().getId()))
                            .findFirst();
            if (indicatorExecutionLocationAssigmentToActivateOpt.isPresent()) {
                indicatorExecutionLocationAssigmentToActivateOpt.get().setState(State.ACTIVO);
            } else {
                IndicatorExecutionLocationAssigment newIndicatorExecutionLocationAssigment = new IndicatorExecutionLocationAssigment();
                newIndicatorExecutionLocationAssigment.setLocation(cantonToActivate);
                newIndicatorExecutionLocationAssigment.setState(State.ACTIVO);
                indicatorExecution.addIndicatorExecutionLocationAssigment(newIndicatorExecutionLocationAssigment);

            }
        });

        locationsToDissable.forEach(cantonToDissable -> {
            Optional<IndicatorExecutionLocationAssigment> indicatorExecutionLocationAssigmentToDissableOpt =
                    indicatorExecution.getIndicatorExecutionLocationAssigments()
                            .stream()
                            .filter(indicatorExecutionLocationAssigment -> cantonToDissable.getId().equals(indicatorExecutionLocationAssigment.getLocation().getId()))
                            .findFirst();
            indicatorExecutionLocationAssigmentToDissableOpt.ifPresent(indicatorExecutionLocationAssigment -> indicatorExecutionLocationAssigment.setState(State.INACTIVO));
        });
    }

    /*Actualizar Opciones de Desagregaciones Indicator Executions*/

    public void updateIndicatorExecutionDissagregationOptionAssignations(
            IndicatorExecution indicatorExecution,
            Set<StandardDissagregationOption> dissOptionsToActivate,
            Set<StandardDissagregationOption> dissOptionsToDissable
    ) {
        dissOptionsToActivate.forEach(dissOptionToActivate -> {
            Optional<IndicatorExecutionDissagregationAssigment> indicatorExecutionDissOptionAssigmentToActivateOpt =
                    indicatorExecution.getIndicatorExecutionDissagregationAssigments()
                            .stream()
                            .filter(indicatorExecutionDissOptionAssigment ->
                                    dissOptionToActivate.getId().equals(indicatorExecutionDissOptionAssigment.getDisagregationOption().getId()))
                            .findFirst();
            if (indicatorExecutionDissOptionAssigmentToActivateOpt.isPresent()) {
                indicatorExecutionDissOptionAssigmentToActivateOpt.get().setState(State.ACTIVO);
            } else {
                IndicatorExecutionDissagregationAssigment newIndicatorExecutionDissOptionAssigment = new IndicatorExecutionDissagregationAssigment();
                newIndicatorExecutionDissOptionAssigment.setDisagregationOption(dissOptionToActivate);
                newIndicatorExecutionDissOptionAssigment.setState(State.ACTIVO);
                indicatorExecution.addIndicatorExecutionDissOptionAssigment(newIndicatorExecutionDissOptionAssigment);

            }
        });

        dissOptionsToDissable.forEach(dissOptionToDissable -> {
            Optional<IndicatorExecutionDissagregationAssigment> indicatorExecutionDissOptionAssigmentToDissableOpt =
                    indicatorExecution.getIndicatorExecutionDissagregationAssigments()
                            .stream()
                            .filter(indicatorExecutionDissOptionAssigment -> dissOptionToDissable.getId().equals(indicatorExecutionDissOptionAssigment.getDisagregationOption().getId()))
                            .findFirst();
            indicatorExecutionDissOptionAssigmentToDissableOpt.ifPresent(indicatorExecutionDissOptionAssigment -> indicatorExecutionDissOptionAssigment.setState(State.INACTIVO));
        });
    }


    public void updateAllPartnersTotals(Long periodId) throws GeneralAppException {
        List<IndicatorExecution> indicatorExecutions = this.indicatorExecutionDao.getActivePartnersIndicatorExecutionsByPeriodId(periodId);
        int conteo = 0;
        int total = indicatorExecutions.size();
        for (IndicatorExecution indicatorExecution : indicatorExecutions) {
            conteo = conteo + 1;
            LOGGER.info(conteo + ":" + total);
            this.updateIndicatorExecutionTotals(indicatorExecution);
            this.saveOrUpdate(indicatorExecution);
        }
    }


    public void updateAllDirectImplementationTotals(Long periodId) throws GeneralAppException {
        List<IndicatorExecution> indicatorExecutions = this.indicatorExecutionDao.getDirectImplementationActiveByPeriodId(periodId);
        int conteo = 0;
        int total = indicatorExecutions.size();
        for (IndicatorExecution indicatorExecution : indicatorExecutions) {
            conteo = conteo + 1;
            LOGGER.info(conteo + ":" + total);
            this.updateIndicatorExecutionTotals(indicatorExecution);
            this.saveOrUpdate(indicatorExecution);
        }
    }

    public void quartersTargetUpdate(List<QuarterWeb> quarterWebs) throws GeneralAppException {
        List<Long> quartersIds = quarterWebs.stream().map(QuarterWeb::getId).collect(Collectors.toList());
        List<Long> indicatorsExecutionsIdsToUpdate = this.indicatorExecutionDao.getByQuartersIds(quartersIds);
        List<IndicatorExecution> indicatorsExecutionsToUpdate = this.indicatorExecutionDao.getByIdsWithIndicatorValues(indicatorsExecutionsIdsToUpdate);
        List<Quarter> quartersList = indicatorsExecutionsToUpdate.stream()
                .map(IndicatorExecution::getQuarters)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        for (QuarterWeb quarterWeb : quarterWebs) {
            Optional<Quarter> quarterOpt = quartersList.stream().filter(quarter -> quarterWeb.getId().equals(quarter.getId())).findFirst();

            if (quarterOpt.isPresent()) {
                quarterOpt.get().setTarget(quarterWeb.getTarget());
            } else {
                throw new GeneralAppException("No se pudo encontrar el trimestre con id " + quarterWeb.getId(), Response.Status.NOT_FOUND);
            }
        }

        for (IndicatorExecution indicatorExecution : indicatorsExecutionsToUpdate) {
            this.updateIndicatorExecutionTotals(indicatorExecution);
            this.saveOrUpdate(indicatorExecution);
        }
    }

    public List<User> getSupervisorsByPeriodId(Long periodId) {
        return this.indicatorExecutionDao.getSupervisorstByPeriodId(periodId);
    }

    public List<IndicatorExecutionWeb> getDirectImplementationsIndicatorExecutionsBySupervisorId(Long periodId, Long supervisorId) throws GeneralAppException {
        List<IndicatorExecution> r = this.indicatorExecutionDao.getDirectImplementationsIndicatorExecutionsBySupervisorId(periodId, supervisorId);
        return this.modelWebTransformationService.indicatorExecutionsToIndicatorExecutionsWeb(r, false);
    }

    public List<ResultManagerIndicatorWeb> getResultManagerIndicators(Long userId, Long periodId) throws GeneralAppException {
        List<Indicator> resultManagerIndicators = this.userDao.findIndicatorsByResultManager(userId);
        List<ResultManagerIndicatorWeb> r = new ArrayList<>();
        for(Indicator resultManagerIndicator : resultManagerIndicators){
            boolean hasExecutions=false;
            List<IndicatorExecution> ies =this.indicatorExecutionDao.getByIndicatorIdAndPeriodId(periodId,resultManagerIndicator.getId());
            if(!ies.isEmpty()){
                hasExecutions = true;
            }
            ResultManagerIndicatorWeb rmi=this.indicatorExecutionDao.getIndicatorAnualTargetandExecutions(resultManagerIndicator.getId(), periodId);
            rmi.setIndicator(this.modelWebTransformationService.indicatorToIndicatorWeb(resultManagerIndicator,true,true));
            rmi.setHasExecutions(hasExecutions);
            r.add(rmi);
            //obtener ResultManagerIndicatorQuarter
            if(hasExecutions){
                List<ResultManagerIndicatorQuarterWeb> rmiq = getIndicatorQuarterExecutions(resultManagerIndicator.getId(), periodId);
                rmi.setResultManagerIndicatorQuarter(rmiq);
            }
        }
        return r;
    }

    public List<ResultManagerIndicatorQuarterWeb>  getIndicatorQuarterExecutions(Long indicatorId, Long periodId) throws GeneralAppException{
        int year=periodService.getById(periodId).getYear();
        List<ResultManagerIndicatorQuarterWeb> rmiqs = indicatorExecutionDao.getIndicatorQuarterExecutions(indicatorId, year);
        for(ResultManagerIndicatorQuarterWeb rmiq : rmiqs){
            List<ResultManagerQuarterImplementerWeb> rmi= indicatorExecutionDao.getIndicatorQuarterImplementers(indicatorId,periodId,rmiq.getQuarter());
            rmiq.setResultManagerQuarterImplementer(rmi);
            List<ResultManagerQuarterPopulationTypeWeb> rmpts=indicatorExecutionDao.getResultManagerQuarterPopulationType(indicatorId,rmiq.getQuarter(),year);
            for(ResultManagerQuarterPopulationTypeWeb rmpt : rmpts){
                ResultManagerIndicator rm =this.resultManagerIndicatorDao.getResultManagerIndicatorByIdParameters(indicatorId,rmiq.getQuarter(),rmpt.getPopulationType().getId(),periodId);
                if(rm == null){
                    rmpt.setConfirmation(false);
                }else{
                    rmpt.setId(rm.getId());
                    rmpt.setConfirmation(rm.isConfirmed());
                    rmpt.setReportValue(rm.getReportValue());
                }
            }
            rmiq.setResultManagerQuarterPopulationType(rmpts);
            ResultManagerIndicatorQuarterReport rmiqr=this.resultManagerIndicatorQuarterReportDao.getResultManIndQuarterReportByIdParameters(indicatorId,rmiq.getQuarter(),periodId);
            if(rmiqr!=null){
                rmiq.setId(rmiqr.getId());
                rmiq.setReportComment(rmiqr.getReportComment());
            }

        }
        return rmiqs;
    }

    public List<IndicatorExecution> getIndicatorExecutionsByResultManagerAndPeriodId(Long userId, Long periodId){
        return indicatorExecutionDao.getIndicatorExecutionsByResultManagerAndPeriodId(userId, periodId);
    }

    public List<IndicatorExecutionWeb> getDirectImplementationsIndicatorExecutionsByReportUserId(Long periodId, Long userId) throws GeneralAppException {
        List<IndicatorExecution> r = this.indicatorExecutionDao.getDirectImplementationsIndicatorExecutionsByReportUserId(periodId, userId);
        return this.modelWebTransformationService.indicatorExecutionsToIndicatorExecutionsWeb(r, false);
    }




}






