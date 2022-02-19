package org.unhcr.osmosys.services;

import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.DissagregationAssignationToIndicatorDao;
import org.unhcr.osmosys.model.DissagregationAssignationToIndicator;
import org.unhcr.osmosys.webServices.model.DissagregationAssignationToIndicatorWeb;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class DissagregationAssignationToIndicatorService {

    @Inject
    DissagregationAssignationToIndicatorDao dissagregationAssignationToIndicatorDao;

    @Inject
    PeriodService periodService;

    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(DissagregationAssignationToIndicatorService.class);

    public DissagregationAssignationToIndicator find(Long id) {
        return this.dissagregationAssignationToIndicatorDao.find(id);
    }

    public DissagregationAssignationToIndicator createDissagregationAssignationToIndicatorFromWeb(DissagregationAssignationToIndicatorWeb dissagregationAssignationToIndicatorWeb){
        DissagregationAssignationToIndicator dai = new DissagregationAssignationToIndicator();
        dai.setDissagregationType(dissagregationAssignationToIndicatorWeb.getDissagregationType());
        dai.setPeriod(this.periodService.find(dissagregationAssignationToIndicatorWeb.getPeriod().getId()));
        dai.setState(dissagregationAssignationToIndicatorWeb.getState());
        return dai;
    }

}
