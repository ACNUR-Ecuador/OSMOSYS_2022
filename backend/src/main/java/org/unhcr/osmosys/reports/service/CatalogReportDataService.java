package org.unhcr.osmosys.reports.service;

import org.jboss.logging.Logger;
import org.unhcr.osmosys.model.Indicator;
import org.unhcr.osmosys.reports.model.IndicatorCatalogDTO;
import org.unhcr.osmosys.services.IndicatorService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;

@SuppressWarnings({"unchecked", "rawtypes"})
@Stateless
public class CatalogReportDataService {
    private static final Logger LOGGER = Logger.getLogger(CatalogReportDataService.class);

    @Inject
    IndicatorService indicatorService;


}
