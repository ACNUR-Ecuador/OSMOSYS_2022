package org.unhcr.osmosys.webServices.model;

import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.enums.DissagregationType;
import org.unhcr.osmosys.model.enums.MonthEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MonthValuesWeb implements Serializable {

    public MonthWeb month;

    Map<DissagregationType, List<IndicatorValueWeb>> indicatorValuesMap = new HashMap<>();

    public MonthWeb getMonth() {
        return month;
    }

    public void setMonth(MonthWeb month) {
        this.month = month;
    }

    public Map<DissagregationType, List<IndicatorValueWeb>> getIndicatorValuesMap() {
        return indicatorValuesMap;
    }

    public void setIndicatorValuesMap(Map<DissagregationType, List<IndicatorValueWeb>> indicatorValuesMap) {
        this.indicatorValuesMap = indicatorValuesMap;
    }
}
