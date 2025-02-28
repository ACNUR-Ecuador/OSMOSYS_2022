package org.unhcr.osmosys.services;

import com.sagatechs.generics.persistence.model.State;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.CustomDissagregationAssignationToIndicatorDao;
import org.unhcr.osmosys.model.CustomDissagregation;
import org.unhcr.osmosys.model.CustomDissagregationAssignationToIndicator;
import org.unhcr.osmosys.webServices.model.CustomDissagregationAssignationToIndicatorWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class CustomDissagregationAssignationToIndicatorService {

    @Inject
    CustomDissagregationAssignationToIndicatorDao dissagregationAssignationToIndicatorDao;

    @Inject
    PeriodService periodService;

    @Inject
    CustomDissagregationService customDissagregationService;

    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(CustomDissagregationAssignationToIndicatorService.class);

    public CustomDissagregationAssignationToIndicator find(Long id) {
        return this.dissagregationAssignationToIndicatorDao.find(id);
    }

    public CustomDissagregationAssignationToIndicator createCustomDissagregationAssignationToIndicatorFromWeb(
            CustomDissagregationAssignationToIndicatorWeb customCissagregationAssignationToIndicatorWeb) {
        CustomDissagregationAssignationToIndicator cdai = new CustomDissagregationAssignationToIndicator();
        cdai.setPeriod(this.periodService.find(customCissagregationAssignationToIndicatorWeb.getPeriod().getId()));
        cdai.setState(customCissagregationAssignationToIndicatorWeb.getState());
        cdai.setCustomDissagregation(this.customDissagregationService.find(customCissagregationAssignationToIndicatorWeb.getCustomDissagregation().getId()));
        return cdai;
    }

    public boolean equalsWebToEntity(CustomDissagregationAssignationToIndicatorWeb web, CustomDissagregationAssignationToIndicator entity) {
        return web.getPeriod().getId().equals(entity.getPeriod().getId()) && web.getCustomDissagregation().getId().equals(entity.getCustomDissagregation().getId());
    }


    /**
     * @param news      new object in web
     * @param originals entities from db
     * @return entities no change, entities active and new actives
     */
    public List<CustomDissagregationAssignationToIndicator> getToKeep(List<CustomDissagregationAssignationToIndicatorWeb> news, List<CustomDissagregationAssignationToIndicator> originals) {
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
    public List<CustomDissagregationAssignationToIndicator> getToDisableWithCoincidence(List<CustomDissagregationAssignationToIndicatorWeb> news, List<CustomDissagregationAssignationToIndicator> originals) {
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
    public List<CustomDissagregationAssignationToIndicator> getToEnable(List<CustomDissagregationAssignationToIndicatorWeb> news, List<CustomDissagregationAssignationToIndicator> originals) {
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
    public List<CustomDissagregationAssignationToIndicator> getToDisableNoCoincidence(
            List<CustomDissagregationAssignationToIndicatorWeb> news, List<CustomDissagregationAssignationToIndicator> originals) {
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

    public List<CustomDissagregationAssignationToIndicator> getToDisableTotal(
            List<CustomDissagregationAssignationToIndicatorWeb> news, List<CustomDissagregationAssignationToIndicator> originals
    ) {

        List<CustomDissagregationAssignationToIndicator> NoCoincidence = this.getToDisableNoCoincidence(news, originals);
        List<CustomDissagregationAssignationToIndicator> WithCoincidence = this.getToDisableWithCoincidence(news, originals);
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
    public List<CustomDissagregationAssignationToIndicator> getToCreate(
            List<CustomDissagregationAssignationToIndicatorWeb> news, List<CustomDissagregationAssignationToIndicator> originals) {
        List<CustomDissagregationAssignationToIndicatorWeb> toCreateWebs = news.stream()
                .filter(dissagregationAssignationToIndicatorWeb -> dissagregationAssignationToIndicatorWeb.getState().equals(State.ACTIVO))
                .filter(dissagregationAssignationToIndicatorWeb ->
                        originals.stream()
                                .noneMatch(dissagregationAssignationToIndicator ->
                                        this.equalsWebToEntity(dissagregationAssignationToIndicatorWeb, dissagregationAssignationToIndicator)

                                )
                )
                .distinct()
                .collect(Collectors.toList());

        List<CustomDissagregationAssignationToIndicator> result = new ArrayList<>();
        for (CustomDissagregationAssignationToIndicatorWeb toCreateWeb : toCreateWebs) {
            result.add(this.createCustomDissagregationAssignationToIndicatorFromWeb(toCreateWeb));
        }

        return result;


    }


    /**********************************/
    public List<CustomDissagregationAssignationToIndicator> getActiveCustomDissagregationAssignationsByIndicatorExecutionId(Long ieId, Long periodId) {
        return this.dissagregationAssignationToIndicatorDao.getActiveCustomDissagregationAssignationsByIndicatorExecutionId(ieId, periodId);
    }

    public List<CustomDissagregation> getActiveCustomDissagregationsByIndicatorExecutionId(Long ieId, Long periodId) {
        return this.getActiveCustomDissagregationAssignationsByIndicatorExecutionId(ieId, periodId).stream()
                .map(CustomDissagregationAssignationToIndicator::getCustomDissagregation)
                .collect(Collectors.toList());
    }
}
