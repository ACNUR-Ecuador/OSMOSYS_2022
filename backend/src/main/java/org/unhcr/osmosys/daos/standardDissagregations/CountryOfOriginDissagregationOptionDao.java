package org.unhcr.osmosys.daos.standardDissagregations;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.Area;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.CountryOfOriginDissagregationOption;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class CountryOfOriginDissagregationOptionDao extends GenericDaoJpa<CountryOfOriginDissagregationOption, Long> {
    public CountryOfOriginDissagregationOptionDao() {
        super(CountryOfOriginDissagregationOption.class, Long.class);
    }

    public List<Area> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM CountryOfOriginDissagregationOption o " +
                "WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, Area.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

}
