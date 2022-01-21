package org.unhcr.osmosys.services;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.model.State;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.daos.IndicatorValueCustomDissagregationDao;
import org.unhcr.osmosys.model.CustomDissagregation;
import org.unhcr.osmosys.model.CustomDissagregationOption;
import org.unhcr.osmosys.model.IndicatorValueCustomDissagregation;
import org.unhcr.osmosys.webServices.services.ModelWebTransformationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class IndicatorValueCustomDissagregationService {

    @Inject
    IndicatorValueCustomDissagregationDao indicatorValueCustomDissagregationDao;

    @Inject
    ModelWebTransformationService modelWebTransformationService;

    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(IndicatorValueCustomDissagregationService.class);

    public IndicatorValueCustomDissagregation getById(Long id) {
        return this.indicatorValueCustomDissagregationDao.find(id);
    }

    public IndicatorValueCustomDissagregation saveOrUpdate(IndicatorValueCustomDissagregation indicatorValueCustomDissagregation) {
        if (indicatorValueCustomDissagregation.getId() == null) {
            this.indicatorValueCustomDissagregationDao.save(indicatorValueCustomDissagregation);
        } else {
            this.indicatorValueCustomDissagregationDao.update(indicatorValueCustomDissagregation);
        }
        return indicatorValueCustomDissagregation;
    }

    public List<IndicatorValueCustomDissagregation> createIndicatorValuesCustomDissagregationForMonth(
            CustomDissagregation customDissagregation
    ) throws GeneralAppException {

        List<CustomDissagregationOption> options = customDissagregation.getCustomDissagregationOptions().stream().filter(customDissagregationOption -> {
            return customDissagregationOption.getState().equals(State.ACTIVO);
        }).collect(Collectors.toList());

        List<IndicatorValueCustomDissagregation> r = new ArrayList<>();
        for (CustomDissagregationOption option : options) {
            IndicatorValueCustomDissagregation iv = new IndicatorValueCustomDissagregation();
            iv.setCustomDissagregationOption(option);
            iv.setState(State.ACTIVO);
            r.add(iv);
        }
        return r;
    }


    public List<IndicatorValueCustomDissagregation> getIndicatorValuesByMonthId(Long monthId) {
        return this.indicatorValueCustomDissagregationDao.getIndicatorValueCustomDissagregationsByMonthId(monthId);
    }
}
