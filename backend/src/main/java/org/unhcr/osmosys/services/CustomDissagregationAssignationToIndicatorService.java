package org.unhcr.osmosys.services;

import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.CustomDissagregationAssignationToIndicatorDao;
import org.unhcr.osmosys.model.CustomDissagregationAssignationToIndicator;
import org.unhcr.osmosys.webServices.model.CustomDissagregationAssignationToIndicatorWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;

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
            CustomDissagregationAssignationToIndicatorWeb customCissagregationAssignationToIndicatorWeb){
        CustomDissagregationAssignationToIndicator cdai = new CustomDissagregationAssignationToIndicator();
        cdai.setPeriod(this.periodService.find(customCissagregationAssignationToIndicatorWeb.getPeriod().getId()));
        cdai.setState(customCissagregationAssignationToIndicatorWeb.getState());
        cdai.setCustomDissagregation(this.customDissagregationService.find(customCissagregationAssignationToIndicatorWeb.getCustomDissagregation().getId()));
        return cdai;
    }

}
