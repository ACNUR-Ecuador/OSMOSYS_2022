package org.unhcr.osmosys.daos.standardDissagregations;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.Area;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.GenderDissagregationOption;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class GenderDissagregationOptionDao extends StandardDissagregationOptionDao<GenderDissagregationOption> {
    public GenderDissagregationOptionDao() {
        super(GenderDissagregationOption.class);
    }

}
