package org.unhcr.osmosys.webServices.services;

import com.sagatechs.generics.appConfiguration.AppConfigurationService;
import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.model.User;
import com.sagatechs.generics.webservice.webModel.UserWeb;
import org.apache.commons.collections4.CollectionUtils;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.AreaDao;
import org.unhcr.osmosys.daos.StatementDao;
import org.unhcr.osmosys.model.*;
import org.unhcr.osmosys.model.enums.*;
import org.unhcr.osmosys.model.standardDissagregations.options.AgeDissagregationOption;
import org.unhcr.osmosys.model.standardDissagregations.options.StandardDissagregationOption;
import org.unhcr.osmosys.model.standardDissagregations.periodOptions.*;
import org.unhcr.osmosys.services.UtilsService;
import org.unhcr.osmosys.webServices.model.*;
import org.unhcr.osmosys.webServices.model.standardDissagregations.StandardDissagregationOptionWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
public class ModelWebTransformationService {
    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(ModelWebTransformationService.class);

    @Inject
    StatementDao statementDao;

    @Inject
    AreaDao areaDao;

    @Inject
    AppConfigurationService appConfigurationService;

    @Inject
    UtilsService utilsService;

    //<editor-fold desc="FocalPointAssignation">
    public FocalPointAssignationWeb focalPointerAssignationToFocalPointerAssignationWeb(FocalPointAssignation focalPointAssignation) {
        if (focalPointAssignation == null) {
            return null;
        }
        FocalPointAssignationWeb focalPointAssignationWeb = new FocalPointAssignationWeb();
        focalPointAssignationWeb.setId(focalPointAssignation.getId());
        focalPointAssignationWeb.setFocalPointer(focalPointAssignation.getFocalPointer());
        focalPointAssignationWeb.setProject(focalPointAssignation.getProject());
        focalPointAssignationWeb.setMainFocalPointer(focalPointAssignation.getMainFocalPointer());
        focalPointAssignationWeb.setState(focalPointAssignation.getState());

        return focalPointAssignationWeb;
    }

    public FocalPointAssignation focalPointerAssignationWebToFocalPointerAssignation(FocalPointAssignationWeb focalPointAssignationWeb) {
        if (focalPointAssignationWeb == null) {
            return null;
        }
        FocalPointAssignation focalPointAssignation = new FocalPointAssignation();
        focalPointAssignation.setId(focalPointAssignationWeb.getId());
        focalPointAssignation.setState(focalPointAssignationWeb.getState());
        focalPointAssignation.setFocalPointer(focalPointAssignationWeb.getFocalPointer());
        focalPointAssignation.setMainFocalPointer(focalPointAssignationWeb.getMainFocalPointer());
        focalPointAssignation.setProject(focalPointAssignationWeb.getProject());
        return focalPointAssignation;
    }

    public List<FocalPointAssignationWeb> focalPointerAssignationsToFocalPointerAssignationWeb(List<FocalPointAssignation> focalPointAssignations) {
        List<FocalPointAssignationWeb> r = new ArrayList<>();
        for (FocalPointAssignation focalPointAssignation : focalPointAssignations) {
            r.add(this.focalPointerAssignationToFocalPointerAssignationWeb(focalPointAssignation));
        }
        return r;
    }

    public List<FocalPointAssignation> focalPointerAssignationWebsToFocalPointerAssignations(List<FocalPointAssignationWeb> focalPointAssignationWebs) {
        List<FocalPointAssignation> r = new ArrayList<>();
        for (FocalPointAssignationWeb focalPointAssignationWeb : focalPointAssignationWebs) {
            r.add(this.focalPointerAssignationWebToFocalPointerAssignation(focalPointAssignationWeb));
        }
        return r;
    }

    //</editor-fold>

    //<editor-fold desc="Areas">
    public List<AreaWeb> areasToAreasWeb(List<Area> areas) {
        List<AreaWeb> r = new ArrayList<>();
        for (Area area : areas) {
            r.add(this.areaToAreaWeb(area));
        }
        return r;
    }

    public AreaWeb areaToAreaWeb(Area area) {
        if (area == null) {
            return null;
        }
        AreaWeb areaWeb = new AreaWeb();
        areaWeb.setId(area.getId());
        areaWeb.setAreaType(area.getAreaType());
        areaWeb.setCode(area.getCode());
        areaWeb.setDefinition(area.getDefinition());
        areaWeb.setDescription(area.getDescription());
        areaWeb.setShortDescription(area.getShortDescription());
        areaWeb.setState(area.getState());

        return areaWeb;
    }

    @SuppressWarnings("unused")
    public List<Area> areasWebToAreas(List<AreaWeb> areasWebs) {
        List<Area> r = new ArrayList<>();
        for (AreaWeb areaWeb : areasWebs) {
            r.add(this.areaToAreaWeb(areaWeb));
        }
        return r;
    }


    public Area areaToAreaWeb(AreaWeb areaWeb) {
        if (areaWeb == null) {
            return null;
        }
        Area area = new Area();
        area.setId(areaWeb.getId());
        area.setAreaType(areaWeb.getAreaType());
        area.setState(areaWeb.getState());
        area.setDescription(areaWeb.getDescription());
        area.setCode(areaWeb.getCode());
        area.setDefinition(areaWeb.getDefinition());
        area.setShortDescription(areaWeb.getShortDescription());
        return area;
    }


    public List<AreaResumeWeb> indicatorExecutionsToAreaWebs(List<IndicatorExecutionWeb> indicatorExecutions, List<Area> areas) throws GeneralAppException {
        // grouped by areas
        List<AreaResumeWeb> r = new ArrayList<>();

        List<AreaWeb> areasWeb = this.areasToAreasWeb(areas);
        Map<AreaWeb, List<IndicatorExecutionWeb>> mapAreas = new HashMap<>();
        for (IndicatorExecutionWeb indicatorExecution : indicatorExecutions) {
            AreaWeb area = indicatorExecution.getIndicator().getStatement().getArea();
            Optional<AreaWeb> areaT = areasWeb.stream().filter(area1 -> area1.getId().equals(area.getId())).findFirst();
            if (areaT.isEmpty()) {
                throw new GeneralAppException("Area no encontrada, error interno", Response.Status.INTERNAL_SERVER_ERROR);
            }

            mapAreas.computeIfAbsent(areaT.get(), k -> new ArrayList<>());
            mapAreas.get(area).add(indicatorExecution);
        }

        for (AreaWeb areaWeb : mapAreas.keySet()) {
            List<IndicatorExecutionWeb> indicatorExecutionsArea = mapAreas.get(areaWeb);
            AreaResumeWeb arw = new AreaResumeWeb();
            arw.setArea(areaWeb);
            arw.setNumberOfLateIndicators(indicatorExecutionsArea.size());
            arw.setIndicatorExecutionIds(new ArrayList<>());
            int lateCount = 0;
            int soonReportCount = 0;
            List<IndicatorWeb> indicators = new ArrayList<>();
            for (IndicatorExecutionWeb indicatorExecutionWeb : indicatorExecutionsArea) {
                indicators.add(indicatorExecutionWeb.getIndicator());
                arw.getIndicatorExecutionIds().add(indicatorExecutionWeb.getId());
                if (indicatorExecutionWeb.getLate().equals(TimeStateEnum.LATE)) {
                    lateCount++;
                }
                if (indicatorExecutionWeb.getLate().equals(TimeStateEnum.SOON_REPORT)) {
                    soonReportCount++;
                }
            }
            indicators.sort((o1, o2) -> {
                int staComp = o1.getStatement().getCode().compareTo(o2.getStatement().getCode());
                if (staComp != 0) {
                    return staComp;
                } else {
                    return o1.getDescription().compareTo(o2.getDescription());
                }
            });

            arw.setNumberOfLateIndicators(lateCount);
            arw.setNumberOfSoonReportIndicators(soonReportCount);
            arw.setNumberOfIndicators(indicators.size());
            arw.setIndicators(indicators);
            r.sort(Comparator.comparing(o -> o.getArea().getId()));
            r.add(arw);
        }

        return r;
    }
    //</editor-fold>

    //<editor-fold desc="CustomDissagregation">
    public CustomDissagregation customDissagregationWebToCustomDissagregation(CustomDissagregationWeb customDissagregationWeb) {
        if (customDissagregationWeb == null) {
            return null;
        }
        CustomDissagregation customDissagregation = new CustomDissagregation();
        customDissagregation.setId(customDissagregationWeb.getId());
        customDissagregation.setName(customDissagregationWeb.getName());
        customDissagregation.setState(customDissagregationWeb.getState());
        customDissagregation.setDescription(customDissagregationWeb.getDescription());
        customDissagregation.setControlTotalValue(customDissagregationWeb.getControlTotalValue());

        Set<CustomDissagregationOption> options = this.customDissagregationOptionsWebToCustomDissagregationOptions(new ArrayList<>(customDissagregationWeb.getCustomDissagregationOptions()));

        for (CustomDissagregationOption customDissagregationOption : options) {
            customDissagregation.addCustomDissagregationOption(customDissagregationOption);
        }

        return customDissagregation;
    }

    public CustomDissagregationWeb customDissagregationWebToCustomDissagregation(CustomDissagregation customDissagregation) {
        if (customDissagregation == null) {
            return null;
        }
        CustomDissagregationWeb customDissagregationWeb = new CustomDissagregationWeb();
        customDissagregationWeb.setId(customDissagregation.getId());
        customDissagregationWeb.setControlTotalValue(customDissagregation.getControlTotalValue());
        customDissagregationWeb.setDescription(customDissagregation.getDescription());
        customDissagregationWeb.setName(customDissagregation.getName());
        customDissagregationWeb.setState(customDissagregation.getState());
        customDissagregationWeb.setCustomDissagregationOptions(this.customDissagregationOptionsToCustomDissagregationOptionsWeb(customDissagregation.getCustomDissagregationOptions()));

        return customDissagregationWeb;
    }


    public List<CustomDissagregationWeb> customDissagregationsToCustomDissagregationsWeb(List<CustomDissagregation> customDissagregations) {
        List<CustomDissagregationWeb> r = new ArrayList<>();
        for (CustomDissagregation customDissagregation : customDissagregations) {
            r.add(this.customDissagregationWebToCustomDissagregation(customDissagregation));
        }
        return r;
    }

    @SuppressWarnings("unused")
    public List<CustomDissagregation> customDissagregationsWebToCustomDissagregations(List<CustomDissagregationWeb> customDissagregationsWebs) {
        List<CustomDissagregation> r = new ArrayList<>();
        for (CustomDissagregationWeb customDissagregationWeb : customDissagregationsWebs) {
            r.add(this.customDissagregationWebToCustomDissagregation(customDissagregationWeb));
        }
        return r;
    }

    //</editor-fold>

    //<editor-fold desc="CustomDissagregationOption">

    public CustomDissagregationOption customDissagregationOptionWebToCustomDissagregationOption(CustomDissagregationOptionWeb customDissagregationOptionWeb) {
        if (customDissagregationOptionWeb == null) {
            return null;
        }
        CustomDissagregationOption customDissagregationOption = new CustomDissagregationOption();
        customDissagregationOption.setId(customDissagregationOptionWeb.getId());
        customDissagregationOption.setName(customDissagregationOptionWeb.getName());
        customDissagregationOption.setState(customDissagregationOptionWeb.getState());
        customDissagregationOption.setDescription(customDissagregationOptionWeb.getDescription());
        //customDissagregationOption.setMarkers(new HashSet<>(this.markerService.markersWebToMarkers(new ArrayList<>(customDissagregationOptionWeb.getMarkers()))));
        return customDissagregationOption;
    }

    public CustomDissagregationOptionWeb customDissagregationOptionToCustomDissagregationOptionWeb(CustomDissagregationOption customDissagregationOption) {
        if (customDissagregationOption == null) {
            return null;
        }
        CustomDissagregationOptionWeb customDissagregationOptionWeb = new CustomDissagregationOptionWeb();
        customDissagregationOptionWeb.setId(customDissagregationOption.getId());
        customDissagregationOptionWeb.setName(customDissagregationOption.getName());
        customDissagregationOptionWeb.setState(customDissagregationOption.getState());
        customDissagregationOptionWeb.setDescription(customDissagregationOption.getDescription());
        return customDissagregationOptionWeb;
    }


    public List<CustomDissagregationOptionWeb> customDissagregationOptionsToCustomDissagregationOptionsWeb(Set<CustomDissagregationOption> customDissagregationOptions) {
        List<CustomDissagregationOptionWeb> r = new ArrayList<>();
        for (CustomDissagregationOption customDissagregationOption : customDissagregationOptions) {
            r.add(this.customDissagregationOptionToCustomDissagregationOptionWeb(customDissagregationOption));
        }
        return r;
    }

    public Set<CustomDissagregationOption> customDissagregationOptionsWebToCustomDissagregationOptions(List<CustomDissagregationOptionWeb> customDissagregationOptionsWeb) {
        Set<CustomDissagregationOption> r = new HashSet<>();
        for (CustomDissagregationOptionWeb customDissagregationOptionWeb : customDissagregationOptionsWeb) {
            r.add(this.customDissagregationOptionWebToCustomDissagregationOption(customDissagregationOptionWeb));
        }
        return r;
    }
    //</editor-fold>


    //<editor-fold desc="CustomDissagregationAssignationToIndicator">
    public CustomDissagregationAssignationToIndicatorWeb customDissagregationAssignationToIndicatorToCustomDissagregationAssignationToIndicatorWeb(CustomDissagregationAssignationToIndicator c) {
        CustomDissagregationAssignationToIndicatorWeb w = new CustomDissagregationAssignationToIndicatorWeb();
        w.setId(c.getId());
        w.setState(c.getState());
        w.setPeriod(this.periodToPeriodWeb(c.getPeriod(), true));
        w.setCustomDissagregation(this.customDissagregationWebToCustomDissagregation(c.getCustomDissagregation()));
        return w;
    }

    public CustomDissagregationAssignationToIndicator customDissagregationAssignationToIndicatorWebToCustomDissagregationAssignationToIndicator(CustomDissagregationAssignationToIndicatorWeb w) {
        CustomDissagregationAssignationToIndicator c = new CustomDissagregationAssignationToIndicator();
        c.setId(w.getId());
        c.setState(w.getState());
        c.setPeriod(this.periodWebToPeriod(w.getPeriod()));
        c.setCustomDissagregation(this.customDissagregationWebToCustomDissagregation(w.getCustomDissagregation()));

        return c;
    }

    public List<CustomDissagregationAssignationToIndicatorWeb> customDissagregationAssignationToIndicatorsToCustomDissagregationAssignationToIndicatorsWeb(Set<CustomDissagregationAssignationToIndicator> c) {
        List<CustomDissagregationAssignationToIndicatorWeb> r = new ArrayList<>();
        for (CustomDissagregationAssignationToIndicator customDissagregationAssignationToIndicator : c) {
            r.add(this.customDissagregationAssignationToIndicatorToCustomDissagregationAssignationToIndicatorWeb(customDissagregationAssignationToIndicator));
        }
        return r;
    }

    public Set<CustomDissagregationAssignationToIndicator> customDissagregationAssignationToIndicatorsWebToCustomDissagregationAssignationToIndicators(List<CustomDissagregationAssignationToIndicatorWeb> w) {
        Set<CustomDissagregationAssignationToIndicator> r = new HashSet<>();
        for (CustomDissagregationAssignationToIndicatorWeb customDissagregationAssignationToIndicatorWeb : w) {
            r.add(this.customDissagregationAssignationToIndicatorWebToCustomDissagregationAssignationToIndicator(customDissagregationAssignationToIndicatorWeb));
        }
        return r;
    }

    //</editor-fold>

    //<editor-fold desc="Indicator">
    public IndicatorWeb indicatorToIndicatorWeb(Indicator indicator, boolean getStatement, boolean getDissagregations) {
        if (indicator == null) {
            return null;
        }
        IndicatorWeb indicatorWeb = new IndicatorWeb();
        indicatorWeb.setId(indicator.getId());
        indicatorWeb.setCode(indicator.getCode());
        indicatorWeb.setDescription(indicator.getDescription());
        indicatorWeb.setCategory(indicator.getCategory());
        indicatorWeb.setQualitativeInstructions(indicator.getQualitativeInstructions());
        indicatorWeb.setInstructions(indicator.getInstructions());
        indicatorWeb.setState(indicator.getState());
        indicatorWeb.setIndicatorType(indicator.getIndicatorType());
        indicatorWeb.setMeasureType(indicator.getMeasureType());
        indicatorWeb.setFrecuency(indicator.getFrecuency());
        indicatorWeb.setAreaType(indicator.getAreaType());
        indicatorWeb.setMonitored(indicator.getMonitored());
        indicatorWeb.setCalculated(indicator.getCalculated());
        indicatorWeb.setTotalIndicatorCalculationType(indicator.getTotalIndicatorCalculationType());
        indicatorWeb.setCompassIndicator(indicator.getCompassIndicator());
        indicatorWeb.setUnit(indicator.getUnit());
        indicatorWeb.setBlockAfterUpdate(indicator.getBlockAfterUpdate());
        if (getStatement) {
            indicatorWeb.setStatement(this.statementToStatementWeb(indicator.getStatement(), true, true, false, false, false));
        }
        if (getDissagregations) {
            indicatorWeb.setDissagregationsAssignationToIndicator(this.dissagregationAssignationToIndicatorsToDissagregationAssignationToIndicatorsWeb(indicator.getDissagregationsAssignationToIndicator()));
            indicatorWeb.setCustomDissagregationAssignationToIndicators(this.customDissagregationAssignationToIndicatorsToCustomDissagregationAssignationToIndicatorsWeb(indicator.getCustomDissagregationAssignationToIndicators()));
        }
        return indicatorWeb;
    }


    public List<IndicatorWeb> indicatorsToIndicatorsWeb(List<Indicator> indicators, boolean getStatement, boolean getDissagregations) {
        List<IndicatorWeb> r = new ArrayList<>();
        for (Indicator indicator : indicators) {
            r.add(this.indicatorToIndicatorWeb(indicator, getStatement, getDissagregations));
        }
        return r;
    }
    //</editor-fold>

    //<editor-fold desc="DissagregationAssignationToIndicator">
    public DissagregationAssignationToIndicatorWeb dissagregationAssignationToIndicatorToDissagregationAssignationToIndicatorWeb(DissagregationAssignationToIndicator d) {
        DissagregationAssignationToIndicatorWeb w = new DissagregationAssignationToIndicatorWeb();
        w.setId(d.getId());
        w.setState(d.getState());
        w.setPeriod(this.periodToPeriodWeb(d.getPeriod(), true));
        w.setDissagregationType(d.getDissagregationType());
        w.setUseCustomAgeDissagregations(d.getUseCustomAgeDissagregations());
        w.setCustomIndicatorOptions(new HashSet<>());

        Set<StandardDissagregationOptionWeb> customIndicatorOptions = d.getDissagregationAssignationToIndicatorPeriodCustomizations()
                .stream()
                .filter(dissagregationAssignationToIndicatorPeriodCustomization -> dissagregationAssignationToIndicatorPeriodCustomization.getState().equals(State.ACTIVO))
                .map(dissagregationAssignationToIndicatorPeriodCustomization -> this.standardDissagregationOptionToStandardDissagregationOptionWeb(dissagregationAssignationToIndicatorPeriodCustomization.getAgeDissagregationOption())).collect(Collectors.toSet());
        w.setCustomIndicatorOptions(customIndicatorOptions);
        return w;
    }

    public DissagregationAssignationToIndicator dissagregationAssignationToIndicatorWebToDissagregationAssignationToIndicator(DissagregationAssignationToIndicatorWeb w) {
        DissagregationAssignationToIndicator d = new DissagregationAssignationToIndicator();
        d.setId(w.getId());
        d.setState(w.getState());
        d.setDissagregationType(w.getDissagregationType());
        d.setPeriod(this.periodWebToPeriod(w.getPeriod()));

        return d;
    }


    public List<DissagregationAssignationToIndicatorWeb> dissagregationAssignationToIndicatorsToDissagregationAssignationToIndicatorsWeb(Set<DissagregationAssignationToIndicator> d) {
        List<DissagregationAssignationToIndicatorWeb> r = new ArrayList<>();
        for (DissagregationAssignationToIndicator dissagregationAssignationToIndicator : d) {
            r.add(this.dissagregationAssignationToIndicatorToDissagregationAssignationToIndicatorWeb(dissagregationAssignationToIndicator));
        }

        return r;
    }

    public Set<DissagregationAssignationToIndicator> dissagregationAssignationToIndicatorsWebToDissagregationAssignationToIndicators(List<DissagregationAssignationToIndicatorWeb> d) {
        Set<DissagregationAssignationToIndicator> r = new HashSet<>();
        for (DissagregationAssignationToIndicatorWeb dissagregationAssignationToIndicator : d) {
            r.add(this.dissagregationAssignationToIndicatorWebToDissagregationAssignationToIndicator(dissagregationAssignationToIndicator));
        }

        return r;
    }
    //</editor-fold>


    //<editor-fold desc="Office">
    public OfficeWeb officeToOfficeWeb(Office office, boolean returnChilds, boolean returnAdministrators) {
        if (office == null) {
            return null;
        }
        OfficeWeb officeWeb = new OfficeWeb();
        officeWeb.setId(office.getId());
        officeWeb.setDescription(office.getDescription());
        officeWeb.setAcronym(office.getAcronym());
        officeWeb.setType(office.getType());
        officeWeb.setState(office.getState());
        officeWeb.setParentOffice(this.officeToOfficeWeb(office.getParentOffice(), false, false));
        if (returnChilds) {
            officeWeb.setChildOffices(this.officesToOfficesWeb(new ArrayList<>(office.getChildOffices()), returnChilds, false));
        }
        if (returnAdministrators) {
            List<User> administrators = office.getOfficeAdministrators().stream()
                    .filter(officeAdministrator -> officeAdministrator.getState().equals(State.ACTIVO))
                    .map(OfficeAdministrator::getAdministrator).collect(Collectors.toList());

            officeWeb.setAdministrators(this.usersToUsersWebSimple(administrators, true, true));
        }
        return officeWeb;
    }


    public Office officeWebToOffice(OfficeWeb officeWeb) {
        if (officeWeb == null) {
            return null;
        }
        Office office = new Office();
        office.setId(officeWeb.getId());
        office.setDescription(officeWeb.getDescription());
        office.setAcronym(officeWeb.getAcronym());
        office.setType(officeWeb.getType());
        office.setState(officeWeb.getState());
        office.setParentOffice(this.officeWebToOffice(officeWeb.getParentOffice()));
        // office.setChildOffices(this.officesToOfficesWeb(office.getChildOffices()));
        return office;
    }

    public List<OfficeWeb> officesToOfficesWeb(List<Office> offices, boolean returnChilds, boolean returnAdministrators) {
        List<OfficeWeb> r = new ArrayList<>();
        for (Office office : offices) {
            r.add(this.officeToOfficeWeb(office, returnChilds, returnAdministrators));
        }
        return r;
    }

    @SuppressWarnings("unused")
    public List<Office> officesWebToOffices(List<OfficeWeb> officesWebs) {
        List<Office> r = new ArrayList<>();
        for (OfficeWeb officeWeb : officesWebs) {
            r.add(this.officeWebToOffice(officeWeb));
        }
        return r;
    }
    //</editor-fold>

    //<editor-fold desc="Organization">
    public OrganizationWeb organizationToOrganizationWeb(Organization organization) {
        if (organization == null) {
            return null;
        }
        OrganizationWeb o = new OrganizationWeb();
        o.setId(organization.getId());
        o.setAcronym(organization.getAcronym());
        o.setCode(organization.getCode());
        o.setState(organization.getState());
        o.setDescription(organization.getDescription());
        return o;
    }

    public Organization organizationWebToOrganization(OrganizationWeb organizationWeb) {
        if (organizationWeb == null) {
            return null;
        }
        Organization o = new Organization();
        o.setId(organizationWeb.getId());
        o.setAcronym(organizationWeb.getAcronym());
        o.setCode(organizationWeb.getCode());
        o.setState(organizationWeb.getState());
        o.setDescription(organizationWeb.getDescription());
        return o;
    }


    @SuppressWarnings("unused")
    public List<Organization> organizationsWebToOrganizations(List<OrganizationWeb> organizationsWebs) {
        List<Organization> r = new ArrayList<>();
        for (OrganizationWeb organizationWeb : organizationsWebs) {
            r.add(this.organizationWebToOrganization(organizationWeb));
        }
        return r;
    }

    public List<OrganizationWeb> organizationsToOrganizationsWeb(List<Organization> organizations) {
        List<OrganizationWeb> r = new ArrayList<>();
        for (Organization organization : organizations) {
            r.add(this.organizationToOrganizationWeb(organization));
        }
        return r;
    }
    //</editor-fold>

    //<editor-fold desc="Period">
    public PeriodWeb periodToPeriodWeb(Period period, boolean simple) {
        if (period == null) {
            return null;
        }
        PeriodWeb periodWeb = new PeriodWeb();
        periodWeb.setId(period.getId());
        periodWeb.setYear(period.getYear());
        periodWeb.setState(period.getState());

        if (simple)
            return periodWeb;

        for (PeriodAgeDissagregationOption option : period.getPeriodAgeDissagregationOptions()) {
            if (option.getState().equals(State.ACTIVO)) {
                periodWeb.getPeriodAgeDissagregationOptions().add(this.standardDissagregationOptionToStandardDissagregationOptionWeb(option.getDissagregationOption()));
            }
        }

        for (PeriodGenderDissagregationOption option : period.getPeriodGenderDissagregationOptions()) {
            if (option.getState().equals(State.ACTIVO)) {
                periodWeb.getPeriodGenderDissagregationOptions().add(this.standardDissagregationOptionToStandardDissagregationOptionWeb(option.getDissagregationOption()));
            }
        }

        for (PeriodPopulationTypeDissagregationOption option : period.getPeriodPopulationTypeDissagregationOptions()) {
            if (option.getState().equals(State.ACTIVO)) {
                periodWeb.getPeriodPopulationTypeDissagregationOptions().add(this.standardDissagregationOptionToStandardDissagregationOptionWeb(option.getDissagregationOption()));
            }
        }

        for (PeriodDiversityDissagregationOption option : period.getPeriodDiversityDissagregationOptions()) {
            if (option.getState().equals(State.ACTIVO)) {
                periodWeb.getPeriodDiversityDissagregationOptions().add(this.standardDissagregationOptionToStandardDissagregationOptionWeb(option.getDissagregationOption()));
            }
        }

        for (PeriodCountryOfOriginDissagregationOption option : period.getPeriodCountryOfOriginDissagregationOptions()) {
            if (option.getState().equals(State.ACTIVO)) {
                periodWeb.getPeriodCountryOfOriginDissagregationOptions().add(this.standardDissagregationOptionToStandardDissagregationOptionWeb(option.getDissagregationOption()));
            }
        }
        if (period.getGeneralIndicator() != null) {
            periodWeb.setGeneralIndicator(this.generalIndicatorToGeneralIndicatorWeb(period.getGeneralIndicator()));
        }

        return periodWeb;
    }


    public Period periodWebToPeriod(PeriodWeb periodWeb) {
        if (periodWeb == null) {
            return null;
        }
        Period period = new Period();
        period.setId(periodWeb.getId());
        period.setYear(periodWeb.getYear());
        period.setState(periodWeb.getState());
        return period;
    }

    public List<PeriodWeb> periodsToPeriodsWeb(List<Period> periods, boolean simple) {
        List<PeriodWeb> r = new ArrayList<>();
        for (Period period : periods) {
            r.add(this.periodToPeriodWeb(period, simple));
        }
        return r;
    }


    @SuppressWarnings("unused")
    public List<Period> periodsWebToPeriods(List<PeriodWeb> periodsWebs) {
        List<Period> r = new ArrayList<>();
        for (PeriodWeb periodWeb : periodsWebs) {
            r.add(this.periodWebToPeriod(periodWeb));
        }
        return r;
    }
    //</editor-fold>

    //<editor-fold desc="Pillar">
    public PillarWeb pillarToPillarWeb(Pillar pillar) {
        if (pillar == null) {
            return null;
        }
        PillarWeb pillarWeb = new PillarWeb();
        pillarWeb.setId(pillar.getId());
        pillarWeb.setShortDescription(pillar.getShortDescription());
        pillarWeb.setCode(pillar.getCode());
        pillarWeb.setDescription(pillar.getDescription());
        pillarWeb.setState(pillar.getState());

        return pillarWeb;
    }


    public Pillar pillarWebToPillar(PillarWeb pillarWeb) {
        if (pillarWeb == null) {
            return null;
        }
        Pillar pillar = new Pillar();
        pillar.setId(pillarWeb.getId());
        pillar.setCode(pillarWeb.getCode());
        pillar.setShortDescription(pillarWeb.getShortDescription());
        pillar.setDescription(pillarWeb.getDescription());
        pillar.setState(pillarWeb.getState());
        return pillar;
    }

    public List<PillarWeb> pillarsToPillarsWeb(List<Pillar> pillars) {
        List<PillarWeb> r = new ArrayList<>();
        for (Pillar pillar : pillars) {
            r.add(this.pillarToPillarWeb(pillar));
        }
        return r;
    }


    @SuppressWarnings("unused")
    public List<Pillar> pillarsWebToPillars(List<PillarWeb> pillarsWebs) {
        List<Pillar> r = new ArrayList<>();
        for (PillarWeb pillarWeb : pillarsWebs) {
            r.add(this.pillarWebToPillar(pillarWeb));
        }
        return r;
    }
    //</editor-fold>

    //<editor-fold desc="Situation">
    public Situation situationWebToSituation(SituationWeb situationWeb) {
        if (situationWeb == null) {
            return null;
        }
        Situation situation = new Situation();
        situation.setId(situationWeb.getId());
        situation.setState(situationWeb.getState());
        situation.setDescription(situationWeb.getDescription());
        situation.setCode(situationWeb.getCode());
        situation.setShortDescription(situationWeb.getShortDescription());
        return situation;
    }

    public SituationWeb situationToSituationWeb(Situation situation) {
        if (situation == null) {
            return null;
        }
        SituationWeb situationWeb = new SituationWeb();
        situationWeb.setId(situation.getId());
        situationWeb.setCode(situation.getCode());
        situationWeb.setDescription(situation.getDescription());
        situationWeb.setShortDescription(situation.getShortDescription());
        situationWeb.setState(situation.getState());

        return situationWeb;
    }

    @SuppressWarnings("unused")
    public List<Situation> situationsWebToSituations(List<SituationWeb> situationsWebs) {
        List<Situation> r = new ArrayList<>();
        for (SituationWeb situationWeb : situationsWebs) {
            r.add(this.situationWebToSituation(situationWeb));
        }
        return r;
    }

    public List<SituationWeb> situationsToSituationsWeb(Set<Situation> situations) {
        List<SituationWeb> r = new ArrayList<>();
        for (Situation situation : situations) {
            r.add(this.situationToSituationWeb(situation));
        }
        return r;
    }
    //</editor-fold>


    //<editor-fold desc="Statement">
    public StatementWeb statementToStatementWeb(Statement statement, boolean getPeriodAssignations, boolean getArea, boolean getPilar, boolean getSituation, boolean getParent) {
        if (statement == null) {
            return null;
        }
        StatementWeb statementWeb = new StatementWeb();
        statementWeb.setId(statement.getId());
        statementWeb.setCode(statement.getCode());
        statementWeb.setProductCode(statement.getProductCode());
        statementWeb.setDescription(statement.getDescription());
        statementWeb.setState(statement.getState());
        if (getParent) {
            statementWeb.setParentStatement(this.statementToStatementWeb(statement.getParentStatement(), false, false, false, false, false));
        }
        if (getArea) {
            statementWeb.setArea(this.areaToAreaWeb(statement.getArea()));
        }
        statementWeb.setAreaType(statement.getAreaType());
        if (getPilar) {
            statementWeb.setPillar(this.pillarToPillarWeb(statement.getPillar()));
        }
        if (getSituation) {
            statementWeb.setSituation(this.situationToSituationWeb(statement.getSituation()));
        }
        if (getPeriodAssignations) {
            statementWeb.setPeriodStatementAsignations(this.periodStatementAsignationsToPeriodStatementAsignationsWeb(statement.getPeriodStatementAsignations()));
        }
        return statementWeb;
    }


    public Statement statementWebToStatement(StatementWeb statementWeb) {
        if (statementWeb == null) {
            return null;
        }
        Statement statement;
        if (statementWeb.getId() == null) {
            statement = new Statement();
            statement.setState(statementWeb.getState());
            statement.setProductCode(statementWeb.getProductCode());
            statement.setDescription(statementWeb.getDescription());
            statement.setCode(statementWeb.getCode());
            statement.setProductCode(statementWeb.getProductCode());
            /*
            statement.setParentStatement(this.statementWebToStatement(statementWeb.getParentStatement()));

            statement.setArea(this.areaToAreaWeb(statementWeb.getArea()));

             */
            if (statementWeb.getParentStatement() != null && statementWeb.getParentStatement().getId() != null) {
                statement.setParentStatement(this.statementDao.find(statementWeb.getParentStatement().getId()));
            } else {
                statement.setParentStatement(null);
            }
            if (statementWeb.getArea() != null && statementWeb.getArea().getId() != null) {
                statement.setArea(this.areaDao.find(statementWeb.getArea().getId()));
                statement.getArea().addStatement(statement);
            }


        } else {
            statement = this.statementDao.find(statementWeb.getId());
            statement.setState(statementWeb.getState());
            statement.setDescription(statementWeb.getDescription());
            statement.setCode(statementWeb.getCode());
            statement.setProductCode(statementWeb.getProductCode());
            /*statement.setParentStatement(this.statementWebToStatement(statementWeb.getParentStatement()));
            statement.setArea(this.areaToAreaWeb(statementWeb.getArea()));*/
            if (statementWeb.getParentStatement() != null && statementWeb.getParentStatement().getId() != null) {
                statement.setParentStatement(this.statementDao.find(statementWeb.getParentStatement().getId()));
            } else {
                statement.setParentStatement(null);
            }
            if (statementWeb.getArea() != null && statementWeb.getArea().getId() != null) {
                statement.setArea(this.areaDao.find(statementWeb.getArea().getId()));
                statement.getArea().addStatement(statement);
            }
        }
        statement.setPillar(this.pillarWebToPillar(statementWeb.getPillar()));
        statement.setSituation(this.situationWebToSituation(statementWeb.getSituation()));

        statement.getPeriodStatementAsignations().forEach(periodStatementAsignation -> periodStatementAsignation.setState(State.INACTIVO));
        for (PeriodStatementAsignationWeb periodStatementAsignationWeb : statementWeb.getPeriodStatementAsignations()) {
            Optional<PeriodStatementAsignation> assignation = statement.getPeriodStatementAsignations().stream().filter(periodStatementAsignation -> periodStatementAsignation.getPeriod().getId().equals(periodStatementAsignationWeb.getPeriod().getId())).findFirst();
            if (assignation.isPresent()) {
                assignation.get().setState(periodStatementAsignationWeb.getState());
            } else {
                PeriodStatementAsignation psa = new PeriodStatementAsignation();
                psa.setState(periodStatementAsignationWeb.getState());
                psa.setPeriod(this.periodWebToPeriod(periodStatementAsignationWeb.getPeriod()));
                statement.addPeriodStatementAsignation(psa);
            }
        }
        statement.setAreaType(statementWeb.getAreaType());
        return statement;
    }

    public List<StatementWeb> statementsToStatementsWeb(List<Statement> statements, boolean getPeriodAssignations, boolean getArea, boolean getPilar, boolean getSituation, boolean getParent) {
        List<StatementWeb> r = new ArrayList<>();
        for (Statement statement : statements) {
            r.add(this.statementToStatementWeb(statement, getPeriodAssignations, getArea, getPilar, getSituation, getParent));
        }
        return r;
    }

    //</editor-fold>

    //<editor-fold desc="PeriodStatementAsignation">
    private PeriodStatementAsignationWeb periodStatementAsignationToPeriodStatementAsignationWeb(PeriodStatementAsignation periodStatementAsignation) {
        PeriodStatementAsignationWeb paw = new PeriodStatementAsignationWeb();
        paw.setId(periodStatementAsignation.getId());
        paw.setState(periodStatementAsignation.getState());
        paw.setPeriod(this.periodToPeriodWeb(periodStatementAsignation.getPeriod(), true));
        return paw;
    }

    private List<PeriodStatementAsignationWeb> periodStatementAsignationsToPeriodStatementAsignationsWeb(Set<PeriodStatementAsignation> periodStatementAsignations) {
        List<PeriodStatementAsignationWeb> periodStatementAsignationWebs = new ArrayList<>();
        for (PeriodStatementAsignation periodStatementAsignation : periodStatementAsignations) {
            periodStatementAsignationWebs.add(this.periodStatementAsignationToPeriodStatementAsignationWeb(periodStatementAsignation));

        }
        return periodStatementAsignationWebs;
    }
    //</editor-fold>

    //<editor-fold desc="Project">
    public ProjectWeb projectToProjectWeb(Project project) {
        ProjectWeb w = new ProjectWeb();
        w.setId(project.getId());
        w.setName(project.getName());
        w.setCode(project.getCode());
        w.setPeriod(this.periodToPeriodWeb(project.getPeriod(), true));
        w.setOrganization(this.organizationToOrganizationWeb(project.getOrganization()));
        w.setStartDate(project.getStartDate());
        w.setEndDate(project.getEndDate());
        w.setState(project.getState());
        List<User> focalPoints = project.getFocalPointAssignations().stream()
                .filter(focalPointAssignation -> focalPointAssignation.getState().equals(State.ACTIVO)).map(FocalPointAssignation::getFocalPointer).filter(user -> user.getState().equals(State.ACTIVO)).collect(Collectors.toList());

        w.setFocalPoints(new HashSet<>(this.usersToUsersWebSimple(focalPoints, true, true)));

        Set<Canton> cantones = project.getProjectLocationAssigments().stream()
                .filter(projectLocationAssigment -> projectLocationAssigment.getState().equals(State.ACTIVO))
                .map(ProjectLocationAssigment::getLocation).collect(Collectors.toSet());

        for (Canton canton : cantones) {
            w.getLocations().add(this.cantonToCantonWeb(canton));
        }

        return w;
    }


    public List<ProjectWeb> projectsToProjectsWeb(List<Project> projects) {
        List<ProjectWeb> r = new ArrayList<>();
        for (Project project : projects) {
            r.add(this.projectToProjectWeb(project));
        }
        return r;
    }


    //</editor-fold>


    //<editor-fold desc="Canton">
    public CantonWeb cantonToCantonWeb(Canton canton) {
        if (canton == null) {
            return null;
        }
        CantonWeb w = new CantonWeb();
        w.setId(canton.getId());
        w.setState(canton.getState());
        w.setName(canton.getName());
        w.setProvincia(this.provinciaToProvinciaWeb(canton.getProvincia()));
        w.setOffice(this.officeToOfficeWeb(canton.getOffice(), false, false));
        w.setCode(canton.getCode());
        return w;
    }

    public List<CantonWeb> cantonsToCantonsWeb(List<Canton> cantones) {
        List<CantonWeb> r = new ArrayList<>();
        for (Canton canton : cantones) {
            r.add(this.cantonToCantonWeb(canton));
        }
        return r;
    }

    public List<Canton> cantonsWebToCantons(List<CantonWeb> cantones) {
        List<Canton> r = new ArrayList<>();
        for (CantonWeb canton : cantones) {
            r.add(this.cantonWebToCanton(canton));
        }
        return r;
    }

    public Canton cantonWebToCanton(CantonWeb cantonWeb) {
        if (cantonWeb == null) {
            return null;
        }
        Canton w = new Canton();
        w.setId(cantonWeb.getId());
        w.setState(cantonWeb.getState());
        w.setName(cantonWeb.getName());
        w.setCode(cantonWeb.getCode());
        return w;
    }
    //</editor-fold>

    //<editor-fold desc="Provincia">
    public ProvinciaWeb provinciaToProvinciaWeb(Provincia provincia) {
        if (provincia == null) {
            return null;
        }
        ProvinciaWeb w = new ProvinciaWeb();
        w.setId(provincia.getId());
        w.setState(provincia.getState());
        w.setDescription(provincia.getDescription());
        w.setCode(provincia.getCode());
        return w;
    }


//</editor-fold>


    //<editor-fold desc="DissagregationAssignationToGeneralIndicator">
    public DissagregationAssignationToGeneralIndicatorWeb dissagregationAssignationToGeneralIndicatorToDissagregationAssignationToGeneralIndicatorWeb(DissagregationAssignationToGeneralIndicator d) {
        DissagregationAssignationToGeneralIndicatorWeb w = new DissagregationAssignationToGeneralIndicatorWeb();
        w.setId(d.getId());
        w.setState(d.getState());
        w.setDissagregationType(d.getDissagregationType());
        return w;
    }

    public DissagregationAssignationToGeneralIndicator dissagregationAssignationToGeneralIndicatorWebToDissagregationAssignationToGeneralIndicator(DissagregationAssignationToGeneralIndicatorWeb w) {
        DissagregationAssignationToGeneralIndicator d = new DissagregationAssignationToGeneralIndicator();
        d.setId(w.getId());
        d.setState(w.getState());
        d.setDissagregationType(w.getDissagregationType());
        return d;
    }


    public List<DissagregationAssignationToGeneralIndicatorWeb> dissagregationAssignationToGeneralIndicatorsToDissagregationAssignationToGeneralIndicatorsWeb(Set<DissagregationAssignationToGeneralIndicator> d) {
        List<DissagregationAssignationToGeneralIndicatorWeb> r = new ArrayList<>();
        for (DissagregationAssignationToGeneralIndicator dissagregationAssignationToGeneralIndicator : d) {
            r.add(this.dissagregationAssignationToGeneralIndicatorToDissagregationAssignationToGeneralIndicatorWeb(dissagregationAssignationToGeneralIndicator));
        }

        return r;
    }

    public Set<DissagregationAssignationToGeneralIndicator> dissagregationAssignationToGeneralIndicatorsWebToDissagregationAssignationToGeneralIndicators(List<DissagregationAssignationToGeneralIndicatorWeb> d) {
        Set<DissagregationAssignationToGeneralIndicator> r = new HashSet<>();
        for (DissagregationAssignationToGeneralIndicatorWeb dissagregationAssignationToGeneralIndicator : d) {
            r.add(this.dissagregationAssignationToGeneralIndicatorWebToDissagregationAssignationToGeneralIndicator(dissagregationAssignationToGeneralIndicator));
        }

        return r;
    }
    //</editor-fold>

    //<editor-fold desc="GeneralIndicator">
    public GeneralIndicatorWeb generalIndicatorToGeneralIndicatorWeb(GeneralIndicator indicator) {
        if (indicator == null) {
            return null;
        }
        GeneralIndicatorWeb indicatorWeb = new GeneralIndicatorWeb();
        indicatorWeb.setId(indicator.getId());
        indicatorWeb.setDescription(indicator.getDescription());
        indicatorWeb.setState(indicator.getState());
        indicatorWeb.setMeasureType(indicator.getMeasureType());
        indicatorWeb.setPeriod(this.periodToPeriodWeb(indicator.getPeriod(), true));
        indicatorWeb.setDissagregationAssignationsToGeneralIndicator(this.dissagregationAssignationToGeneralIndicatorsToDissagregationAssignationToGeneralIndicatorsWeb(indicator.getDissagregationAssignationsToGeneralIndicator()));
        return indicatorWeb;
    }

    public GeneralIndicator generalIndicatorWebToGeneralIndicator(GeneralIndicatorWeb indicatorWeb) {
        if (indicatorWeb == null) {
            return null;
        }
        GeneralIndicator indicator = new GeneralIndicator();
        indicator.setId(indicatorWeb.getId());
        indicator.setDescription(indicatorWeb.getDescription());
        indicator.setState(indicatorWeb.getState());
        indicator.setMeasureType(indicatorWeb.getMeasureType());
        Period period = this.periodWebToPeriod(indicatorWeb.getPeriod());
        if (period != null)
            period.setGeneralIndicator(indicator);
        indicator.setPeriod(period);

        Set<DissagregationAssignationToGeneralIndicator> dissagregationAssignationToIndicators = this.dissagregationAssignationToGeneralIndicatorsWebToDissagregationAssignationToGeneralIndicators(indicatorWeb.getDissagregationAssignationsToGeneralIndicator());
        for (DissagregationAssignationToGeneralIndicator dissagregationAssignationToIndicator : dissagregationAssignationToIndicators) {
            indicator.addDissagregationAssignationsToGeneralIndicator(dissagregationAssignationToIndicator);
        }
        return indicator;
    }

    public List<GeneralIndicatorWeb> generalIndicatorsToGeneralIndicatorsWeb(List<GeneralIndicator> indicators) {
        List<GeneralIndicatorWeb> r = new ArrayList<>();
        for (GeneralIndicator indicator : indicators) {
            r.add(this.generalIndicatorToGeneralIndicatorWeb(indicator));
        }
        return r;
    }

    @SuppressWarnings("unused")
    public List<GeneralIndicator> generalIndicatorsWebToGeneralIndicators(List<GeneralIndicatorWeb> indicatorsWebs) {
        List<GeneralIndicator> r = new ArrayList<>();
        for (GeneralIndicatorWeb indicatorWeb : indicatorsWebs) {
            r.add(this.generalIndicatorWebToGeneralIndicator(indicatorWeb));
        }
        return r;
    }
    //</editor-fold>

    //<editor-fold desc="IndicatorExecution">

    public IndicatorExecutionWeb indicatorExecutionToIndicatorExecutionWeb(IndicatorExecution ie, boolean getProject) throws GeneralAppException {

        IndicatorExecutionWeb iw = new IndicatorExecutionWeb();
        iw.setId(ie.getId());
        iw.setActivityDescription(ie.getActivityDescription());
        iw.setIndicatorType(ie.getIndicatorType());
        iw.setState(ie.getState());
        iw.setCompassIndicator(ie.getCompassIndicator());
        iw.setTotalExecution(ie.getTotalExecution());
        iw.setProjectStatement(this.statementToStatementWeb(ie.getProjectStatement(), false, true, true, true, false));
        iw.setPeriod(this.periodToPeriodWeb(ie.getPeriod(), true));
        iw.setKeepBudget(ie.getKeepBudget());
        iw.setAssignedBudget(ie.getAssignedBudget());
        iw.setAvailableBudget(ie.getAvailableBudget());
        iw.setTotalUsedBudget(ie.getTotalUsedBudget());
        if (getProject && ie.getProject() != null) {
            iw.setProject(this.projectToProjectWeb(ie.getProject()));
        }
        iw.setExecutionPercentage(ie.getExecutionPercentage());
        iw.setTarget(ie.getTarget());
        if (ie.getIndicatorType().equals(IndicatorType.GENERAL)) {
            IndicatorWeb indicatorWeb = new IndicatorWeb();
            indicatorWeb.setIndicatorType(IndicatorType.GENERAL);
            indicatorWeb.setMonitored(Boolean.TRUE);
            indicatorWeb.setCompassIndicator(Boolean.FALSE);
            indicatorWeb.setCode("General");
            indicatorWeb.setDescription(ie.getPeriod().getGeneralIndicator().getDescription());
            indicatorWeb.setBlockAfterUpdate(Boolean.FALSE);
            iw.setIndicator(indicatorWeb);


        } else {
            iw.setIndicator(this.indicatorToIndicatorWeb(ie.getIndicator(), true, true));
            iw.setReportingOffice(this.officeToOfficeWeb(ie.getReportingOffice(), false, false));
            iw.setSupervisorUser(this.userToUserWebSimple(ie.getSupervisorUser(), false, true));
            iw.setAssignedUser(this.userToUserWebSimple(ie.getAssignedUser(), false, true));
            iw.setAssignedUserBackup(this.userToUserWebSimple(ie.getAssignedUserBackup(), false, true));

        }
        iw.setQuarters(this.quartersToQuarterWeb(ie.getQuarters()));
        // set late state
        Frecuency frecuency = ie.getIndicatorType().equals(IndicatorType.GENERAL) ? Frecuency.MENSUAL : ie.getIndicator().getFrecuency();
        TimeStateEnum indicatorExecutionTimeState = this.setQuartersLateState(iw.getQuarters(), frecuency, this.appConfigurationService.getReportLimitDay());
        iw.setLate(indicatorExecutionTimeState);

        Optional<Quarter> lastReportedQuarter = ie.getQuarters().stream()
                .filter(quarter -> quarter.getState().equals(State.ACTIVO))
                .filter(quarter -> quarter.getTotalExecution() != null)
                .max(Comparator.comparingInt(Quarter::getOrder));
        if (lastReportedQuarter.isPresent()) {
            iw.setLastReportedQuarter(this.quarterToQuarterWeb(lastReportedQuarter.get()));
            Optional<Month> lastReportedMonth = lastReportedQuarter.get().getMonths()
                    .stream()
                    .filter(month -> month.getState().equals(State.ACTIVO))
                    .filter(month -> month.getTotalExecution() != null)
                    .max(Comparator.comparingInt(Month::getOrder));
            lastReportedMonth.ifPresent(month -> iw.setLastReportedMonth(this.monthToMonthWeb(month)));
        }

        List<MonthWeb> lateMonths = this.getLateIndicatorExecutionMonths(iw);

        List<Canton> activeCantons = ie.getIndicatorExecutionLocationAssigments()
                .stream()
                .filter(indicatorExecutionLocationAssigment -> indicatorExecutionLocationAssigment.getState().equals(State.ACTIVO))
                .map(IndicatorExecutionLocationAssigment::getLocation)
                .collect(Collectors.toList());
        iw.setLocations(this.cantonsToCantonsWeb(activeCantons));
        return iw;
    }

    private TimeStateEnum setQuartersLateState(List<QuarterWeb> quarters, Frecuency frecuency, Integer reportLimitDay) throws GeneralAppException {
        TimeStateEnum timeStateTotal = TimeStateEnum.NO_TIME;
        for (QuarterWeb quarter : quarters) {
            TimeStateEnum timeStateQuarter = TimeStateEnum.NO_TIME;
            for (MonthWeb month : quarter.getMonths()) {
                TimeStateEnum monthState = this.utilsService.getLateStateForMonth(month, frecuency, reportLimitDay);
                timeStateQuarter = TimeStateEnum.addStates(timeStateQuarter, monthState);
                month.setLate(monthState);
                //LOGGER.debug(month.toString());
            }
            quarter.setLate(timeStateQuarter);
            timeStateTotal = TimeStateEnum.addStates(timeStateTotal, timeStateQuarter);
        }
        return timeStateTotal;
    }

    public List<MonthWeb> getLateIndicatorExecutionMonths(IndicatorExecutionWeb indicatorExecution) throws GeneralAppException {
        Integer alertDays = this.appConfigurationService.getReportLimitDay();
        LocalDate today = LocalDate.now();

        int todayYear = today.getYear();
        int todayDay = today.getDayOfMonth();
        int todayMonth;
        if (alertDays < todayDay) {
            todayMonth = (today.getMonth().getValue() - 1) > 0 ? today.getMonth().getValue() - 1 : today.getMonth().getValue();
        } else {
            todayMonth = today.getMonth().getValue();
        }
        QuarterEnum todayQuarter = MonthEnum.getQuarterByMonthNumber(todayMonth);

        // obtengo los meses
        List<MonthWeb> monthList = indicatorExecution.getQuarters().stream()
                .filter(quarter -> quarter.getState().equals(State.ACTIVO))
                .map(quarter -> new ArrayList<>(quarter.getMonths()))
                .flatMap(Collection::stream)
                .filter(month -> month.getState().equals(State.ACTIVO))
                .sorted(Comparator.comparingInt(MonthWeb::getOrder))
                .collect(Collectors.toList());
        // busco todos lo mese anteriores a hoy y que no esten reportados
        Frecuency frecuency;
        if (indicatorExecution.getIndicatorType().equals(IndicatorType.GENERAL)) {
            frecuency = Frecuency.MENSUAL;
        } else {
            frecuency = indicatorExecution.getIndicator().getFrecuency();
        }
        if (frecuency.equals(Frecuency.MENSUAL)) {
            List<MonthWeb> lateMonths = monthList.stream()
                    // lo que no esten reportados
                    .filter(month -> month.getTotalExecution() == null)
                    .filter(month -> {
                        if (month.getYear().compareTo(todayYear) < 0) {
                            return true;
                        }
                        //noinspection RedundantIfStatement
                        if (month.getYear().compareTo(todayYear) == 0
                                && month.getMonth().getOrder() <= todayMonth
                        ) {
                            return true;
                        }
                        return false;
                    }).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(lateMonths)) {
                return lateMonths;
            } else {
                return new ArrayList<>();
            }
        }
        if (frecuency.equals(Frecuency.TRIMESTRAL)) {
            // las month of last quarter
            QuarterEnum previousQuarter = null;
            if (todayQuarter.getOrder() > 1) {
                previousQuarter = QuarterEnum.getByQuarterNumber(todayQuarter.getOrder() - 1);
            }
            MonthEnum previousMonth;
            if (previousQuarter != null) {
                List<MonthEnum> monthtsTmp = MonthEnum.getMonthsByQuarter(previousQuarter);
                //noinspection StreamToLoop,OptionalGetWithoutIsPresent
                previousMonth = monthtsTmp.stream().max(Comparator.comparingInt(MonthEnum::getOrder)).get();
            } else {
                previousMonth = null;
            }


            List<MonthWeb> lateMonths = monthList.stream()
                    // lo que no esten reportados
                    .filter(month -> month.getTotalExecution() == null)
                    .filter(month -> {
                        if (month.getYear().compareTo(todayYear) < 0) {
                            return true;
                        }
                        //noinspection RedundantIfStatement
                        if (month.getYear().compareTo(todayYear) == 0
                                && previousMonth != null
                                && month.getMonth().getOrder() <= previousMonth.getOrder()
                        ) {
                            return true;
                        }
                        return false;
                    }).collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(lateMonths)) {
                return lateMonths;
            } else {
                return new ArrayList<>();
            }
        }

        if (frecuency.equals(Frecuency.SEMESTRAL)) {
            // las month of last quarter
            MonthEnum previousMonth;
            if (todayMonth > 6) {
                previousMonth = MonthEnum.JUNIO;
            } else {
                previousMonth = null;
            }
            List<MonthWeb> lateMonths = monthList.stream()
                    // lo que no esten reportados
                    .filter(month -> month.getTotalExecution() == null)
                    .filter(month -> {
                        if (month.getYear().compareTo(todayYear) < 0) {
                            return true;
                        }
                        //noinspection RedundantIfStatement
                        if (month.getYear().compareTo(todayYear) == 0
                                && previousMonth != null
                                && month.getMonth().getOrder() <= previousMonth.getOrder()
                        ) {
                            return true;
                        }
                        return false;
                    }).collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(lateMonths)) {
                return lateMonths;
            } else {
                return new ArrayList<>();
            }
        }
        if (frecuency.equals(Frecuency.ANUAL)) {
            // las month of last quarter
            List<MonthWeb> lateMonths = monthList.stream()
                    // lo que no esten reportados
                    .filter(month -> month.getTotalExecution() == null)
                    .filter(month -> month.getYear().compareTo(todayYear) < 0)
                    .collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(lateMonths)) {
                return lateMonths;
            } else {
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }


    public List<IndicatorExecutionWeb> indicatorExecutionsToIndicatorExecutionsWeb(List<IndicatorExecution> indicatorExecutions, boolean getProject) throws GeneralAppException {
        List<IndicatorExecutionWeb> r = new ArrayList<>();
        for (IndicatorExecution indicatorExecution : indicatorExecutions) {
            r.add(this.indicatorExecutionToIndicatorExecutionWeb(indicatorExecution, getProject));
        }
        return r;
    }


    //</editor-fold>

    //<editor-fold desc="Quarter">


    public QuarterWeb quarterToQuarterWeb(Quarter quarter) {
        QuarterWeb q = new QuarterWeb();
        q.setId(quarter.getId());
        q.setQuarter(quarter.getQuarter());
        q.setCommentary(quarter.getCommentary());
        q.setOrder(quarter.getOrder());
        q.setYear(quarter.getYear());
        q.setTarget(quarter.getTarget());
        q.setTotalExecution(quarter.getTotalExecution());
        q.setState(quarter.getState());
        q.setExecutionPercentage(quarter.getExecutionPercentage());
        q.setMonths(this.monthsToMonthsWeb(quarter.getMonths()));
        q.setBlockUpdate(quarter.getBlockUpdate());
        return q;
    }

    public List<QuarterWeb> quartersToQuarterWeb(Set<Quarter> quarters) {
        List<QuarterWeb> r = new ArrayList<>();
        for (Quarter quarter : quarters) {
            r.add(this.quarterToQuarterWeb(quarter));
        }
        return r;
    }
    //</editor-fold>


    //<editor-fold desc="Quarter">
    public MonthWeb monthToMonthWeb(Month mo) {
        MonthWeb q = new MonthWeb();
        q.setId(mo.getId());
        q.setMonth(mo.getMonth());
        q.setOrder(mo.getOrder());
        q.setYear(mo.getYear());
        q.setState(mo.getState());
        q.setCommentary(mo.getCommentary());
        q.setTotalExecution(mo.getTotalExecution());
        if (CollectionUtils.isNotEmpty(mo.getSources())) {
            mo.getSources().size();
        }
        q.setSources(mo.getSources());
        q.setSourceOther(mo.getSourceOther());
        q.setChecked(mo.getChecked());
        q.setUsedBudget(mo.getUsedBudget());
        q.setBlockUpdate(mo.getBlockUpdate());
        return q;
    }


    public List<MonthWeb> monthsToMonthsWeb(Set<Month> months) {
        List<MonthWeb> r = new ArrayList<>();
        for (Month month : months) {
            r.add(this.monthToMonthWeb(month));
        }
        return r;
    }

    //</editor-fold>
    //<editor-fold desc="IndicatorValue">
    public IndicatorValueWeb indicatorToIndicatorValueWeb(IndicatorValue indicatorValue) {
        IndicatorValueWeb q = new IndicatorValueWeb();
        q.setId(indicatorValue.getId());
        q.setState(indicatorValue.getState());
        q.setMonthEnum(indicatorValue.getMonthEnum());
        q.setDissagregationType(indicatorValue.getDissagregationType());
        q.setPopulationType(this.standardDissagregationOptionToStandardDissagregationOptionWeb(indicatorValue.getPopulationType()));
        q.setCountryOfOrigin(this.standardDissagregationOptionToStandardDissagregationOptionWeb(indicatorValue.getCountryOfOrigin()));
        q.setGenderType(this.standardDissagregationOptionToStandardDissagregationOptionWeb(indicatorValue.getGenderType()));
        q.setDiversityType(this.standardDissagregationOptionToStandardDissagregationOptionWeb(indicatorValue.getDiversityType()));
        q.setAgeType(this.standardDissagregationOptionToStandardDissagregationOptionWeb(indicatorValue.getAgeType()));
        q.setLocation(this.cantonToCantonWeb(indicatorValue.getLocation()));
        q.setValue(indicatorValue.getValue());
        q.setNumeratorValue(indicatorValue.getNumeratorValue());
        q.setDenominatorValue(indicatorValue.getDenominatorValue());
        return q;
    }


    public List<IndicatorValueWeb> indicatorsToIndicatorValuesWeb(Set<IndicatorValue> indicatorValues) {
        List<IndicatorValueWeb> r = new ArrayList<>();
        for (IndicatorValue indicatorValue : indicatorValues) {
            r.add(this.indicatorToIndicatorValueWeb(indicatorValue));
        }
        return r;
    }
    //</editor-fold>

    //<editor-fold desc="IndicatorValueCustomDissagregation">
    public IndicatorValueCustomDissagregationWeb indicatorValueCustomDissagregationToIndicatorValueCustomDissagregationWeb(IndicatorValueCustomDissagregation indicatorValueCustomDissagregation) {
        IndicatorValueCustomDissagregationWeb q = new IndicatorValueCustomDissagregationWeb();
        q.setId(indicatorValueCustomDissagregation.getId());
        q.setState(indicatorValueCustomDissagregation.getState());
        q.setCustomDissagregationOption(this.customDissagregationOptionToCustomDissagregationOptionWeb(indicatorValueCustomDissagregation.getCustomDissagregationOption()));
        q.setMonthEnum(indicatorValueCustomDissagregation.getMonthEnum());
        q.setValue(indicatorValueCustomDissagregation.getValue());
        q.setNumeratorValue(indicatorValueCustomDissagregation.getNumeratorValue());
        q.setDenominatorValue(indicatorValueCustomDissagregation.getDenominatorValue());
        return q;
    }


    public List<IndicatorValueCustomDissagregationWeb> indicatorValueCustomDissagregationsToIndicatorValueCustomDissagregationWebs(Set<IndicatorValueCustomDissagregation> indicatorValueCustomDissagregation) {
        List<IndicatorValueCustomDissagregationWeb> r = new ArrayList<>();
        for (IndicatorValueCustomDissagregation indicatorValue : indicatorValueCustomDissagregation) {
            r.add(this.indicatorValueCustomDissagregationToIndicatorValueCustomDissagregationWeb(indicatorValue));
        }
        return r;
    }
    //</editor-fold>

    //<editor-fold desc="User">
    public UserWeb userToUserWebSimple(User user, Boolean setOffice, Boolean setOrganization) {
        if (user == null) {
            return null;
        }
        UserWeb uw = new UserWeb();
        uw.setId(user.getId());
        uw.setName(user.getName());
        uw.setEmail(user.getEmail());
        uw.setState(user.getState());
        uw.setUsername(user.getUsername());
        if (setOffice) {
            uw.setOffice(this.officeToOfficeWeb(user.getOffice(), false, false));
        }
        if (setOrganization) {
            uw.setOrganization(this.organizationToOrganizationWeb(user.getOrganization()));
        }
        return uw;

    }

    public List<UserWeb> usersToUsersWebSimple(List<User> users, Boolean setOffice, Boolean setOrganization) {
        List<UserWeb> r = new ArrayList<>();
        for (User user : users) {
            r.add(this.userToUserWebSimple(user, setOffice, setOrganization));
        }
        return r;

    }


    //</editor-fold>


    /////////////////******** standar dissagregations*********///////////////////////////////

    public <D extends StandardDissagregationOption> StandardDissagregationOptionWeb standardDissagregationOptionToStandardDissagregationOptionWeb(D dissagregationOption) {
        if(dissagregationOption==null)return null;
        StandardDissagregationOptionWeb d = new StandardDissagregationOptionWeb();
        d.setId(dissagregationOption.getId());
        d.setState(dissagregationOption.getState());
        d.setOrder(dissagregationOption.getOrder());
        if (dissagregationOption instanceof AgeDissagregationOption) {
            AgeDissagregationOption ageOption = (AgeDissagregationOption) dissagregationOption;
            d.setName(dissagregationOption.getName() + " " + ageOption.getAgeRange());
        } else {
            d.setName(dissagregationOption.getName());
        }
        d.setGroupName(dissagregationOption.getGroupName());
        return d;

    }

    public <LD extends StandardDissagregationOption> List<StandardDissagregationOptionWeb> standardDissagregationOptionsToStandardDissagregationOptionWebs(List<LD> options) {
        List<StandardDissagregationOptionWeb> r = new ArrayList<>();
        for (LD option : options) {
            r.add(this.standardDissagregationOptionToStandardDissagregationOptionWeb(option));
        }
        return r;

    }


}
