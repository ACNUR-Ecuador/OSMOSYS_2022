package org.unhcr.osmosys.services;

import com.sagatechs.generics.persistence.model.State;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.DissagregationAssignationToIndicatorDao;
import org.unhcr.osmosys.model.DissagregationAssignationToIndicator;
import org.unhcr.osmosys.model.standardDissagregations.options.AgeDissagregationOption;
import org.unhcr.osmosys.services.standardDissagregations.StandardDissagregationOptionService;
import org.unhcr.osmosys.webServices.model.DissagregationAssignationToIndicatorWeb;
import org.unhcr.osmosys.webServices.model.standardDissagregations.StandardDissagregationOptionWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class DissagregationAssignationToIndicatorService {

    @Inject
    DissagregationAssignationToIndicatorDao dissagregationAssignationToIndicatorDao;

    @Inject
    PeriodService periodService;

    @Inject
    StandardDissagregationOptionService standardDissagregationOptionService;

    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(DissagregationAssignationToIndicatorService.class);

    public DissagregationAssignationToIndicator find(Long id) {
        return this.dissagregationAssignationToIndicatorDao.find(id);
    }


    public boolean equalsWebToEntity(DissagregationAssignationToIndicatorWeb web, DissagregationAssignationToIndicator entity) {
        return web.getPeriod().getId().equals(entity.getPeriod().getId()) && web.getDissagregationType() == entity.getDissagregationType();

    }

    /**
     * @param news      new object in web
     * @param originals entities from db
     * @return entities no change, entities active and new actives
     */
    public List<DissagregationAssignationToIndicator> getToKeep(List<DissagregationAssignationToIndicatorWeb> news, List<DissagregationAssignationToIndicator> originals) {
        return originals.stream()
                .filter(dissagregationAssignationToIndicator ->
                        news.stream()
                                .anyMatch(dissagregationAssignationToIndicatorWeb ->
                                        this.equalsWebToEntity(dissagregationAssignationToIndicatorWeb, dissagregationAssignationToIndicator)
                                                && dissagregationAssignationToIndicatorWeb.getState().equals(State.ACTIVO)
                                                && dissagregationAssignationToIndicator.getState().equals(State.ACTIVO)
                                )
                )
                .distinct()
                .collect(Collectors.toList());

    }

    /**
     * @param news      new object in web
     * @param originals entities from db
     * @return entities no change, entities active and new inactives
     */
    public List<DissagregationAssignationToIndicator> getToDisableWithCoincidence(List<DissagregationAssignationToIndicatorWeb> news, List<DissagregationAssignationToIndicator> originals) {
        return originals.stream()
                .filter(dissagregationAssignationToIndicator ->
                        news.stream()
                                .anyMatch(dissagregationAssignationToIndicatorWeb ->
                                        this.equalsWebToEntity(dissagregationAssignationToIndicatorWeb, dissagregationAssignationToIndicator)
                                                && dissagregationAssignationToIndicatorWeb.getState().equals(State.INACTIVO)
                                                && dissagregationAssignationToIndicator.getState().equals(State.ACTIVO)
                                )
                )
                .distinct()
                .collect(Collectors.toList());

    }

    /**
     * exist in new and original
     *
     * @param news      new object in web
     * @param originals entities from db
     * @return entities to enable, entities inactive and new actives
     */
    public List<DissagregationAssignationToIndicator> getToEnable(List<DissagregationAssignationToIndicatorWeb> news, List<DissagregationAssignationToIndicator> originals) {
        return originals.stream()
                .filter(dissagregationAssignationToIndicator ->
                        news.stream()
                                .anyMatch(dissagregationAssignationToIndicatorWeb ->
                                        this.equalsWebToEntity(dissagregationAssignationToIndicatorWeb, dissagregationAssignationToIndicator)
                                                && dissagregationAssignationToIndicatorWeb.getState().equals(State.ACTIVO)
                                                && dissagregationAssignationToIndicator.getState().equals(State.INACTIVO)
                                )
                )
                .distinct()
                .collect(Collectors.toList());

    }

    /**
     * exist in new and original
     *
     * @param news      new object in web
     * @param originals entities from db
     * @return entities to enable, entities inactive and new actives
     */
    public List<DissagregationAssignationToIndicator> getToDisableNoCoincidence(
            List<DissagregationAssignationToIndicatorWeb> news, List<DissagregationAssignationToIndicator> originals) {
        return originals.stream()
                .filter(dissagregationAssignationToIndicator -> dissagregationAssignationToIndicator.getState().equals(State.ACTIVO))
                .filter(dissagregationAssignationToIndicator ->
                        news.stream()
                                .noneMatch(dissagregationAssignationToIndicatorWeb ->
                                        this.equalsWebToEntity(dissagregationAssignationToIndicatorWeb, dissagregationAssignationToIndicator)

                                )
                )
                .distinct()
                .collect(Collectors.toList());

    }

    public List<DissagregationAssignationToIndicator> getToDisableTotal(
            List<DissagregationAssignationToIndicatorWeb> news, List<DissagregationAssignationToIndicator> originals
    ) {

        List<DissagregationAssignationToIndicator> NoCoincidence = this.getToDisableNoCoincidence(news, originals);
        List<DissagregationAssignationToIndicator> WithCoincidence = this.getToDisableWithCoincidence(news, originals);
        WithCoincidence.addAll(NoCoincidence);
        return WithCoincidence;
    }

    /**
     * exist in new and original
     *
     * @param news      new object in web
     * @param originals entities from db
     * @return entities to enable, entities inactive and new actives
     */
    public List<DissagregationAssignationToIndicator> getToCreate(
            List<DissagregationAssignationToIndicatorWeb> news, List<DissagregationAssignationToIndicator> originals) {
        List<DissagregationAssignationToIndicatorWeb> toCreateWebs = news.stream()
                .filter(dissagregationAssignationToIndicatorWeb -> dissagregationAssignationToIndicatorWeb.getState().equals(State.ACTIVO))
                .filter(dissagregationAssignationToIndicatorWeb ->
                        originals.stream()
                                .noneMatch(dissagregationAssignationToIndicator ->
                                        this.equalsWebToEntity(dissagregationAssignationToIndicatorWeb, dissagregationAssignationToIndicator)

                                )
                )
                .distinct()
                .collect(Collectors.toList());

        List<DissagregationAssignationToIndicator> result = new ArrayList<>();
        for (DissagregationAssignationToIndicatorWeb toCreateWeb : toCreateWebs) {
            result.add(this.createDissagregationAssignationToIndicatorFromWeb(toCreateWeb));
        }

        return result;


    }

    public DissagregationAssignationToIndicator createDissagregationAssignationToIndicatorFromWeb(DissagregationAssignationToIndicatorWeb daiWeb) {
        DissagregationAssignationToIndicator dai = new DissagregationAssignationToIndicator();
        dai.setDissagregationType(daiWeb.getDissagregationType());
        dai.setPeriod(this.periodService.find(daiWeb.getPeriod().getId()));
        dai.setState(daiWeb.getState());
        dai.setDissagregationType(daiWeb.getDissagregationType());
        dai.setUseCustomAgeDissagregations(daiWeb.getUseCustomAgeDissagregations() != null ? daiWeb.getUseCustomAgeDissagregations() : Boolean.FALSE);
        if (dai.getUseCustomAgeDissagregations()) {
            for (StandardDissagregationOptionWeb customIndicatorOptionWeb : daiWeb.getCustomIndicatorOptions()) {
                AgeDissagregationOption ageDissagregationOption = (AgeDissagregationOption) this.standardDissagregationOptionService.getById(customIndicatorOptionWeb.getId());
                dai.addAgeDissagregationCustomizations(ageDissagregationOption);
            }
        }

        return dai;
    }


}
