package org.unhcr.osmosys.webServices.services;

import com.sagatechs.generics.persistence.model.State;
import com.sagatechs.generics.security.servicio.UserService;
import org.unhcr.osmosys.daos.StatementDao;
import org.unhcr.osmosys.model.*;
import org.unhcr.osmosys.model.enums.IndicatorType;
import org.unhcr.osmosys.webServices.model.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
public class ModelWebTransformationService {

    @Inject
    StatementDao statementDao;

    @Inject
    UserService userService;

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

        options.forEach(customDissagregationOption -> {
            customDissagregation.addCustomDissagregationOption(customDissagregationOption);
        });

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
        for (MarkerWeb marker : customDissagregationOptionWeb.getMarkers()) {
            customDissagregationOption.addMarker(this.markerWebToMarker(marker));
        }
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
        customDissagregationOptionWeb.setMarkers(this.markersToMarkersWeb(customDissagregationOption.getMarkers()));
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

    //<editor-fold desc="CustomDissagregationFilterIndicator">
    public CustomDissagregationFilterIndicatorWeb customDissagregationFilterIndicatorsToCustomDissagregationFilterIndicatorsWeb(CustomDissagregationFilterIndicator c) {
        CustomDissagregationFilterIndicatorWeb w = new CustomDissagregationFilterIndicatorWeb();
        w.setId(c.getId());
        w.setState(c.getState());
        w.setCustomDissagregationOptions(this.customDissagregationOptionsToCustomDissagregationOptionsWeb(c.getCustomDissagregationOptions()));
        return w;
    }

    public CustomDissagregationFilterIndicator customDissagregationFilterIndicatorWebToCustomDissagregationFilterIndicator(CustomDissagregationFilterIndicatorWeb w1) {
        CustomDissagregationFilterIndicator c = new CustomDissagregationFilterIndicator();
        c.setId(w1.getId());
        c.setState(w1.getState());
        c.setCustomDissagregationOptions(this.customDissagregationOptionsWebToCustomDissagregationOptions(w1.getCustomDissagregationOptions()));
        return c;
    }

    public List<CustomDissagregationFilterIndicatorWeb> customDissagregationFilterIndicatorsToCustomDissagregationFilterIndicatorsWeb(Set<CustomDissagregationFilterIndicator> c) {
        List<CustomDissagregationFilterIndicatorWeb> r = new ArrayList<>();
        for (CustomDissagregationFilterIndicator customDissagregationFilterIndicator : c) {
            r.add(this.customDissagregationFilterIndicatorsToCustomDissagregationFilterIndicatorsWeb(customDissagregationFilterIndicator));
        }
        return r;
    }

    public Set<CustomDissagregationFilterIndicator> customDissagregationFilterIndicatorsWebToCustomDissagregationFilterIndicators(List<CustomDissagregationFilterIndicatorWeb> c) {
        Set<CustomDissagregationFilterIndicator> r = new HashSet<>();
        for (CustomDissagregationFilterIndicatorWeb customDissagregationFilterIndicator : c) {
            r.add(this.customDissagregationFilterIndicatorWebToCustomDissagregationFilterIndicator(customDissagregationFilterIndicator));
        }
        return r;
    }

    //</editor-fold>

    //<editor-fold desc="CustomDissagregationAssignationToIndicator">
    public CustomDissagregationAssignationToIndicatorWeb customDissagregationAssignationToIndicatorToCustomDissagregationAssignationToIndicatorWeb(CustomDissagregationAssignationToIndicator c) {
        CustomDissagregationAssignationToIndicatorWeb w = new CustomDissagregationAssignationToIndicatorWeb();
        w.setId(c.getId());
        w.setState(c.getState());
        w.setCustomDissagregation(this.customDissagregationWebToCustomDissagregation(c.getCustomDissagregation()));
        w.setCustomDissagregationFilterIndicators(this.customDissagregationFilterIndicatorsToCustomDissagregationFilterIndicatorsWeb(c.getCustomDissagregationFilterIndicators()));
        return w;
    }

    public CustomDissagregationAssignationToIndicator customDissagregationAssignationToIndicatorWebToCustomDissagregationAssignationToIndicator(CustomDissagregationAssignationToIndicatorWeb w) {
        CustomDissagregationAssignationToIndicator c = new CustomDissagregationAssignationToIndicator();
        c.setId(w.getId());
        c.setState(w.getState());
        c.setCustomDissagregation(this.customDissagregationWebToCustomDissagregation(w.getCustomDissagregation()));
        c.setCustomDissagregationFilterIndicators(this.customDissagregationFilterIndicatorsWebToCustomDissagregationFilterIndicators(w.getCustomDissagregationFilterIndicators()));

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
    public IndicatorWeb indicatorToIndicatorWeb(Indicator indicator) {
        if (indicator == null) {
            return null;
        }
        IndicatorWeb indicatorWeb = new IndicatorWeb();
        indicatorWeb.setId(indicator.getId());
        indicatorWeb.setCode(indicator.getCode());
        indicatorWeb.setDescription(indicator.getDescription());
        indicatorWeb.setGuidePartners(indicator.getGuidePartners());
        indicatorWeb.setGuideDirectImplementation(indicator.getGuideDirectImplementation());
        indicatorWeb.setState(indicator.getState());
        indicatorWeb.setIndicatorType(indicator.getIndicatorType());
        indicatorWeb.setMeasureType(indicator.getMeasureType());
        indicatorWeb.setFrecuency(indicator.getFrecuency());
        indicatorWeb.setAreaType(indicator.getAreaType());
        indicatorWeb.setMonitored(indicator.getMonitored());
        indicatorWeb.setCalculated(indicator.getCalculated());
        indicatorWeb.setTotalIndicatorCalculationType(indicator.getTotalIndicatorCalculationType());
        indicatorWeb.setCompassIndicator(indicator.getCompassIndicator());
        List<MarkerWeb> markers = this.markersToMarkersWeb(indicator.getMarkers());
        indicatorWeb.setMarkers(markers);
        List<StatementWeb> statements = this.statementsToStatementsWeb(new ArrayList<>(indicator.getStatements()));
        indicatorWeb.setStatements(statements);
        indicatorWeb.setDissagregationsAssignationToIndicator(this.dissagregationAssignationToIndicatorsToDissagregationAssignationToIndicatorsWeb(indicator.getDissagregationsAssignationToIndicator()));
        indicatorWeb.setCustomDissagregationAssignationToIndicators(this.customDissagregationAssignationToIndicatorsToCustomDissagregationAssignationToIndicatorsWeb(indicator.getCustomDissagregationAssignationToIndicators()));
        return indicatorWeb;
    }

    public Indicator indicatorWebToIndicator(IndicatorWeb indicatorWeb) {
        if (indicatorWeb == null) {
            return null;
        }
        Indicator indicator = new Indicator();
        indicator.setId(indicatorWeb.getId());
        indicator.setCode(indicatorWeb.getCode());
        indicator.setDescription(indicatorWeb.getDescription());
        indicator.setGuidePartners(indicatorWeb.getGuidePartners());
        indicator.setGuideDirectImplementation(indicatorWeb.getGuideDirectImplementation());
        indicator.setState(indicatorWeb.getState());
        indicator.setIndicatorType(indicatorWeb.getIndicatorType());
        indicator.setMeasureType(indicatorWeb.getMeasureType());
        indicator.setFrecuency(indicatorWeb.getFrecuency());
        indicator.setAreaType(indicatorWeb.getAreaType());
        indicator.setMonitored(indicatorWeb.getMonitored());
        indicator.setCalculated(indicatorWeb.getCalculated());
        indicator.setTotalIndicatorCalculationType(indicatorWeb.getTotalIndicatorCalculationType());
        indicator.setCompassIndicator(indicatorWeb.getCompassIndicator());
        Set<Marker> markers = this.markersWebToMarkers(indicatorWeb.getMarkers());
        for (Marker marker : markers) {
            indicator.addMarker(marker);
        }
        Set<Statement> statements = this.statementsWebToStatements(indicatorWeb.getStatements());
        for (Statement statement : statements) {
            indicator.addStatement(statement);
        }
        Set<DissagregationAssignationToIndicator> dissagregationAssignationToIndicators = this.dissagregationAssignationToIndicatorsWebToDissagregationAssignationToIndicators(indicatorWeb.getDissagregationsAssignationToIndicator());
        for (DissagregationAssignationToIndicator dissagregationAssignationToIndicator : dissagregationAssignationToIndicators) {
            indicator.addDissagregationAssignationToIndicator(dissagregationAssignationToIndicator);
        }
        Set<CustomDissagregationAssignationToIndicator> customDissagregationAssignationToIndicators = this.customDissagregationAssignationToIndicatorsWebToCustomDissagregationAssignationToIndicators(indicatorWeb.getCustomDissagregationAssignationToIndicators());
        for (CustomDissagregationAssignationToIndicator customDissagregationAssignationToIndicator : customDissagregationAssignationToIndicators) {
            indicator.addCustomDissagregationAssignationToIndicator(customDissagregationAssignationToIndicator);
        }
        return indicator;
    }

    public List<IndicatorWeb> indicatorsToIndicatorsWeb(List<Indicator> indicators) {
        List<IndicatorWeb> r = new ArrayList<>();
        for (Indicator indicator : indicators) {
            r.add(this.indicatorToIndicatorWeb(indicator));
        }
        return r;
    }

    public List<Indicator> indicatorsWebToIndicators(List<IndicatorWeb> indicatorsWebs) {
        List<Indicator> r = new ArrayList<>();
        for (IndicatorWeb indicatorWeb : indicatorsWebs) {
            r.add(this.indicatorWebToIndicator(indicatorWeb));
        }
        return r;
    }
    //</editor-fold>

    //<editor-fold desc="DissagregationAssignationToIndicator">
    public DissagregationAssignationToIndicatorWeb dissagregationAssignationToIndicatorToDissagregationAssignationToIndicatorWeb(DissagregationAssignationToIndicator d) {
        DissagregationAssignationToIndicatorWeb w = new DissagregationAssignationToIndicatorWeb();
        w.setId(d.getId());
        w.setState(d.getState());
        w.setDissagregationType(d.getDissagregationType());
        w.setDissagregationFilterIndicators(this.dissagregationFilterIndicatorsToDissagregationFilterIndicatorsWeb(d.getDissagregationFilterIndicators()));
        return w;
    }

    public DissagregationAssignationToIndicator dissagregationAssignationToIndicatorWebToDissagregationAssignationToIndicator(DissagregationAssignationToIndicatorWeb w) {
        DissagregationAssignationToIndicator d = new DissagregationAssignationToIndicator();
        d.setId(w.getId());
        d.setState(w.getState());
        d.setDissagregationType(w.getDissagregationType());
        Set<DissagregationFilterIndicator> dissagregationFilterIndicators = this.dissagregationFilterIndicatorsWebToDissagregationFilterIndicators(w.getDissagregationFilterIndicators());
        for (DissagregationFilterIndicator dissagregationFilterIndicator : dissagregationFilterIndicators) {
            d.addDissagregationFilterIndicator(dissagregationFilterIndicator);
        }
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

    //<editor-fold desc="DissagregationFilterIndicator">
    public DissagregationFilterIndicatorWeb dissagregationFilterIndicatorToDissagregationFilterIndicatorWeb(DissagregationFilterIndicator d) {
        DissagregationFilterIndicatorWeb w = new DissagregationFilterIndicatorWeb();
        w.setId(d.getId());
        w.setState(d.getState());
        w.setDissagregationType(d.getDissagregationType());
        w.setPopulationType(d.getPopulationType());
        w.setCountryOfOrigin(d.getCountryOfOrigin());
        w.setGenderType(d.getGenderType());
        w.setAgeType(d.getAgeType());
        return w;
    }

    public DissagregationFilterIndicator dissagregationFilterIndicatorWebToDissagregationFilterIndicator(DissagregationFilterIndicatorWeb w) {
        DissagregationFilterIndicator d = new DissagregationFilterIndicator();
        d.setId(w.getId());
        d.setState(w.getState());
        d.setDissagregationType(w.getDissagregationType());
        d.setPopulationType(w.getPopulationType());
        d.setCountryOfOrigin(w.getCountryOfOrigin());
        d.setGenderType(w.getGenderType());
        d.setAgeType(w.getAgeType());
        return d;
    }

    public List<DissagregationFilterIndicatorWeb> dissagregationFilterIndicatorsToDissagregationFilterIndicatorsWeb(Set<DissagregationFilterIndicator> d) {
        List<DissagregationFilterIndicatorWeb> r = new ArrayList<>();
        for (DissagregationFilterIndicator dissagregationFilterIndicator : d) {
            r.add(this.dissagregationFilterIndicatorToDissagregationFilterIndicatorWeb(dissagregationFilterIndicator));
        }
        return r;
    }

    public Set<DissagregationFilterIndicator> dissagregationFilterIndicatorsWebToDissagregationFilterIndicators(List<DissagregationFilterIndicatorWeb> d) {
        Set<DissagregationFilterIndicator> r = new HashSet<>();
        for (DissagregationFilterIndicatorWeb dissagregationFilterIndicatorWeb : d) {
            r.add(this.dissagregationFilterIndicatorWebToDissagregationFilterIndicator(dissagregationFilterIndicatorWeb));
        }
        return r;
    }
    //</editor-fold>

    //<editor-fold desc="Marker">
    public MarkerWeb markerToMarkerWeb(Marker marker) {
        if (marker == null) {
            return null;
        }
        MarkerWeb markerWeb = new MarkerWeb();
        markerWeb.setId(marker.getId());
        markerWeb.setSubType(marker.getSubType());
        markerWeb.setType(marker.getType());
        markerWeb.setDescription(marker.getDescription());
        markerWeb.setShortDescription(marker.getShortDescription());
        markerWeb.setState(marker.getState());
        return markerWeb;
    }

    public Marker markerWebToMarker(MarkerWeb markerWeb) {
        if (markerWeb == null) {
            return null;
        }
        Marker marker = new Marker();
        marker.setId(markerWeb.getId());
        marker.setType(markerWeb.getType());
        marker.setSubType(markerWeb.getSubType());
        marker.setState(markerWeb.getState());
        marker.setDescription(markerWeb.getDescription());
        marker.setShortDescription(markerWeb.getShortDescription());
        return marker;
    }


    public List<MarkerWeb> markersToMarkersWeb(Set<Marker> markers) {
        List<MarkerWeb> r = new ArrayList<>();
        for (Marker marker : markers) {
            r.add(this.markerToMarkerWeb(marker));
        }
        return r;
    }

    public Set<Marker> markersWebToMarkers(List<MarkerWeb> markersWebs) {
        Set<Marker> r = new HashSet<>();
        for (MarkerWeb markerWeb : markersWebs) {
            r.add(this.markerWebToMarker(markerWeb));
        }
        return r;
    }
    //</editor-fold>

    //<editor-fold desc="Office">
    public OfficeWeb officeToOfficeWeb(Office office, boolean returnChilds) {
        if (office == null) {
            return null;
        }
        OfficeWeb officeWeb = new OfficeWeb();
        officeWeb.setId(office.getId());
        officeWeb.setDescription(office.getDescription());
        officeWeb.setAcronym(office.getAcronym());
        officeWeb.setType(office.getType());
        officeWeb.setState(office.getState());
        officeWeb.setParentOffice(this.officeToOfficeWeb(office.getParentOffice(), false));
        if (returnChilds) {
            officeWeb.setChildOffices(this.officesToOfficesWeb(new ArrayList<>(office.getChildOffices()), returnChilds));
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

    public List<OfficeWeb> officesToOfficesWeb(List<Office> offices, boolean returnChilds) {
        List<OfficeWeb> r = new ArrayList<>();
        for (Office office : offices) {
            r.add(this.officeToOfficeWeb(office, returnChilds));
        }
        return r;
    }

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
    public PeriodWeb periodToPeriodWeb(Period period) {
        if (period == null) {
            return null;
        }
        PeriodWeb periodWeb = new PeriodWeb();
        periodWeb.setId(period.getId());
        periodWeb.setYear(period.getYear());
        periodWeb.setState(period.getState());

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

    public List<PeriodWeb> periodsToPeriodsWeb(List<Period> periods) {
        List<PeriodWeb> r = new ArrayList<>();
        for (Period period : periods) {
            r.add(this.periodToPeriodWeb(period));
        }
        return r;
    }


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
    public StatementWeb statementWebToStatement(Statement statement) {
        if (statement == null) {
            return null;
        }
        StatementWeb statementWeb = new StatementWeb();
        statementWeb.setId(statement.getId());
        statementWeb.setCode(statement.getCode());
        statementWeb.setDescription(statement.getDescription());
        statementWeb.setShortDescription(statement.getShortDescription());
        statementWeb.setState(statement.getState());
        statementWeb.setParentStatement(this.statementWebToStatement(statement.getParentStatement()));
        statementWeb.setArea(this.areaToAreaWeb(statement.getArea()));
        statementWeb.setAreaType(statement.getAreaType());
        statementWeb.setPillar(this.pillarToPillarWeb(statement.getPillar()));
        statementWeb.setSituation(this.situationToSituationWeb(statement.getSituation()));

        statementWeb.setPeriodStatementAsignations(this.periodStatementAsignationsToPeriodStatementAsignationsWeb(statement.getPeriodStatementAsignations()));

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
            statement.setDescription(statementWeb.getDescription());
            statement.setCode(statementWeb.getCode());
            statement.setShortDescription(statementWeb.getShortDescription());
            statement.setAreaType(statementWeb.getArea().getAreaType());
            statement.setParentStatement(this.statementWebToStatement(statementWeb.getParentStatement()));
            statement.setArea(this.areaToAreaWeb(statementWeb.getArea()));
            statement.getArea().addStatement(statement);
            statement.setPillar(this.pillarWebToPillar(statementWeb.getPillar()));
            statement.setSituation(this.situationWebToSituation(statementWeb.getSituation()));

        } else {
            statement = this.statementDao.find(statementWeb.getId());
            statement.setState(statementWeb.getState());
            statement.setDescription(statementWeb.getDescription());
            statement.setCode(statementWeb.getCode());
            statement.setShortDescription(statementWeb.getShortDescription());
            statement.setAreaType(statementWeb.getArea().getAreaType());
            statement.setParentStatement(this.statementWebToStatement(statementWeb.getParentStatement()));
            statement.setArea(this.areaToAreaWeb(statementWeb.getArea()));
            statement.setPillar(this.pillarWebToPillar(statementWeb.getPillar()));
            statement.setSituation(this.situationWebToSituation(statementWeb.getSituation()));
        }

        statement.getPeriodStatementAsignations().forEach(periodStatementAsignation -> periodStatementAsignation.setState(State.INACTIVO));
        for (PeriodStatementAsignationWeb periodStatementAsignationWeb : statementWeb.getPeriodStatementAsignations()) {
            Optional<PeriodStatementAsignation> assignation = statement.getPeriodStatementAsignations().stream().filter(periodStatementAsignation -> periodStatementAsignation.getPeriod().getId().equals(periodStatementAsignationWeb.getPeriod().getId())).findFirst();
            if (assignation.isPresent()) {
                assignation.get().setState(periodStatementAsignationWeb.getState());
            } else {
                PeriodStatementAsignation psa = new PeriodStatementAsignation();
                psa.setState(periodStatementAsignationWeb.getState());
                psa.setPeriod(this.periodWebToPeriod(periodStatementAsignationWeb.getPeriod()));
                psa.setPopulationCoverage(0L);
                statement.addPeriodStatementAsignation(psa);
            }
        }
        return statement;
    }

    public List<StatementWeb> statementsToStatementsWeb(List<Statement> statements) {
        List<StatementWeb> r = new ArrayList<>();
        for (Statement statement : statements) {
            r.add(this.statementWebToStatement(statement));
        }
        return r;
    }

    public Set<Statement> statementsWebToStatements(List<StatementWeb> statementsWebs) {
        Set<Statement> r = new HashSet<>();
        for (StatementWeb statementWeb : statementsWebs) {
            r.add(this.statementWebToStatement(statementWeb));
        }
        return r;
    }
    //</editor-fold>

    //<editor-fold desc="PeriodStatementAsignation">
    private PeriodStatementAsignationWeb periodStatementAsignationToPeriodStatementAsignationWeb(PeriodStatementAsignation periodStatementAsignation) {
        PeriodStatementAsignationWeb paw = new PeriodStatementAsignationWeb();
        paw.setId(periodStatementAsignation.getId());
        paw.setState(periodStatementAsignation.getState());
        paw.setPopulationCoverage(periodStatementAsignation.getPopulationCoverage());
        paw.setPeriod(this.periodToPeriodWeb(periodStatementAsignation.getPeriod()));
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
        w.setPeriod(this.periodToPeriodWeb(project.getPeriod()));
        w.setOrganization(this.organizationToOrganizationWeb(project.getOrganization()));
        w.setStartDate(project.getStartDate());
        w.setEndDate(project.getEndDate());
        w.setState(project.getState());
        w.setFocalPoint(this.userService.userToUserWeb(project.getFocalPoint()));
        Set<Canton> cantones = project.getProjectLocationAssigments().stream().filter(projectLocationAssigment -> {
            return projectLocationAssigment.getState().equals(State.ACTIVO);
        }).map(projectLocationAssigment -> {
            return projectLocationAssigment.getLocation();
        }).collect(Collectors.toSet());

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
        w.setDescription(canton.getDescription());
        w.setProvincia(this.provinciaToProvinciaWeb(canton.getProvincia()));
        w.setOffice(this.officeToOfficeWeb(canton.getOffice(), false));
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
        w.setDescription(cantonWeb.getDescription());
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
        indicatorWeb.setPeriod(this.periodToPeriodWeb(indicator.getPeriod()));
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

    public List<GeneralIndicator> generalIndicatorsWebToGeneralIndicators(List<GeneralIndicatorWeb> indicatorsWebs) {
        List<GeneralIndicator> r = new ArrayList<>();
        for (GeneralIndicatorWeb indicatorWeb : indicatorsWebs) {
            r.add(this.generalIndicatorWebToGeneralIndicator(indicatorWeb));
        }
        return r;
    }
    //</editor-fold>

    //<editor-fold desc="IndicatorExecution">
    public IndicatorExecutionGeneralIndicatorAdministrationResumeWeb indicatorExecutionToIndicatorExecutionGeneralIndicatorAdministrationResumeWeb(IndicatorExecution indicatorExecution) {
        IndicatorExecutionGeneralIndicatorAdministrationResumeWeb i = new IndicatorExecutionGeneralIndicatorAdministrationResumeWeb();
        i.setId(indicatorExecution.getId());
        i.setIndicatorDescription(indicatorExecution.getPeriod().getGeneralIndicator().getDescription());
        i.setIndicatorType(indicatorExecution.getIndicatorType());
        i.setTotalExecution(indicatorExecution.getTotalExecution());
        i.setTarget(indicatorExecution.getTarget());
        i.setState(indicatorExecution.getState());
        i.setQuarters(this.quartersToQuarterResumesWeb(indicatorExecution.getQuarters()));
        i.setExecutionPercentage(indicatorExecution.getExecutionPercentage());
        return i;

    }

    public IndicatorExecutionGeneralIndicatorResumeWeb indicatorExecutionToIndicatorExecutionGeneralIndicatorResumeWeb(IndicatorExecution indicatorExecution) {
        IndicatorExecutionGeneralIndicatorResumeWeb i = new IndicatorExecutionGeneralIndicatorResumeWeb();
        i.setId(indicatorExecution.getId());

        IndicatorWeb indicatorWeb = new IndicatorWeb();
        indicatorWeb.setIndicatorType(IndicatorType.GENERAL);
        indicatorWeb.setMonitored(Boolean.TRUE);
        indicatorWeb.setCompassIndicator(Boolean.FALSE);
        indicatorWeb.setCode("General");
        indicatorWeb.setDescription(indicatorExecution.getPeriod().getGeneralIndicator().getDescription());
        i.setIndicator(indicatorWeb);
        i.setIndicatorType(indicatorExecution.getIndicatorType());
        i.setTotalExecution(indicatorExecution.getTotalExecution());
        i.setTarget(indicatorExecution.getTarget());
        i.setState(indicatorExecution.getState());
        i.setQuarters(this.quartersToQuarterWeb(indicatorExecution.getQuarters()));
        i.setExecutionPercentage(indicatorExecution.getExecutionPercentage());
        return i;

    }

    public IndicatorExecutionPerformanceIndicatorAdministrationResumeWeb indicatorExecutionToIndicatorExecutionPerformanceIndicatorAdministrationResumeWeb(
            IndicatorExecution indicatorExecution) {
        IndicatorExecutionPerformanceIndicatorAdministrationResumeWeb i = new IndicatorExecutionPerformanceIndicatorAdministrationResumeWeb();
        i.setId(indicatorExecution.getId());
        i.setIndicatorCode(indicatorExecution.getIndicator().getCode());
        i.setIndicatorDescription(indicatorExecution.getIndicator().getDescription());
        i.setIndicatorType(indicatorExecution.getIndicatorType());
        i.setTotalExecution(indicatorExecution.getTotalExecution());
        i.setTarget(indicatorExecution.getTarget());
        i.setState(indicatorExecution.getState());
        i.setQuarters(this.quartersToQuarterResumesWeb(indicatorExecution.getQuarters()));
        i.setExecutionPercentage(indicatorExecution.getExecutionPercentage());
        i.setQuarters(this.quartersToQuarterResumesWeb(indicatorExecution.getQuarters()));
        return i;

    }

    public List<IndicatorExecutionGeneralIndicatorAdministrationResumeWeb> indicatorExecutionsToIndicatorExecutionGeneralIndicatorAdministrationResumesWeb(List<IndicatorExecution> indicatorExecutions) {
        List<IndicatorExecutionGeneralIndicatorAdministrationResumeWeb> r = new ArrayList<>();
        for (IndicatorExecution indicatorExecution : indicatorExecutions) {
            r.add(this.indicatorExecutionToIndicatorExecutionGeneralIndicatorAdministrationResumeWeb(indicatorExecution));
        }
        return r;
    }

    public List<IndicatorExecutionGeneralIndicatorResumeWeb> indicatorExecutionsToIndicatorExecutionGeneralIndicatorResumesWeb(List<IndicatorExecution> indicatorExecutions) {
        List<IndicatorExecutionGeneralIndicatorResumeWeb> r = new ArrayList<>();
        for (IndicatorExecution indicatorExecution : indicatorExecutions) {
            r.add(this.indicatorExecutionToIndicatorExecutionGeneralIndicatorResumeWeb(indicatorExecution));
        }
        return r;
    }

    public List<IndicatorExecutionPerformanceIndicatorAdministrationResumeWeb> indicatorExecutionsToIndicatorExecutionPerformanceIndicatorAdministrationResumesWeb(List<IndicatorExecution> indicatorExecutions) {
        List<IndicatorExecutionPerformanceIndicatorAdministrationResumeWeb> r = new ArrayList<>();
        for (IndicatorExecution indicatorExecution : indicatorExecutions) {
            r.add(this.indicatorExecutionToIndicatorExecutionPerformanceIndicatorAdministrationResumeWeb(indicatorExecution));
        }
        return r;
    }
    //</editor-fold>

    //<editor-fold desc="Quarter">
    public QuarterResumeWeb quarterToQuarterResumeWeb(Quarter quarter) {
        QuarterResumeWeb q = new QuarterResumeWeb();
        q.setId(quarter.getId());
        q.setQuarter(quarter.getQuarter());
        q.setCommentary(quarter.getCommentary());
        q.setOrder(quarter.getOrder());
        q.setYear(quarter.getYear());
        q.setTarget(quarter.getTarget());
        q.setTotalExecution(quarter.getTotalExecution());
        q.setState(quarter.getState());
        q.setExecutionPercentage(quarter.getExecutionPercentage());
        return q;
    }


    public Set<QuarterResumeWeb> quartersToQuarterResumesWeb(Set<Quarter> quarters) {
        Set<QuarterResumeWeb> r = new HashSet<>();
        for (Quarter quarter : quarters) {
            r.add(this.quarterToQuarterResumeWeb(quarter));
        }
        return r;
    }


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
        return q;
    }

    public Set<QuarterWeb> quartersToQuarterWeb(Set<Quarter> quarters) {
        Set<QuarterWeb> r = new HashSet<>();
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
        q.setPopulationType(indicatorValue.getPopulationType());
        q.setCountryOfOrigin(indicatorValue.getCountryOfOrigin());
        q.setGenderType(indicatorValue.getGenderType());
        q.setDiversityType(indicatorValue.getDiversityType());
        q.setLocation(this.cantonToCantonWeb(indicatorValue.getLocation()));
        q.setShowValue(indicatorValue.getShowValue());
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
}
