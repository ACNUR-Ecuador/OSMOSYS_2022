package org.unhcr.osmosys.daos.standardDissagregations;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.Area;
import org.unhcr.osmosys.model.standardDissagregations.PeriodStandardDissagregation.Options.DiversityDissagregationOption;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class DiversityDissagregationOptionDao extends GenericDaoJpa<DiversityDissagregationOption, Long> {
    public DiversityDissagregationOptionDao() {
        super(DiversityDissagregationOption.class, Long.class);
    }

    public List<Area> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM DiversityDissagregationOption o " +
                "WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, Area.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

}
