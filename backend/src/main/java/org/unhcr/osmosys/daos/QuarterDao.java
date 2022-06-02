package org.unhcr.osmosys.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.Quarter;
import org.unhcr.osmosys.model.enums.QuarterEnum;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class QuarterDao extends GenericDaoJpa<Quarter, Long> {
    public QuarterDao() {
        super(Quarter.class, Long.class);
    }

    public List<Quarter> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM Quarter o " +
                "WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, Quarter.class);
        q.setParameter("state", state);
        return q.getResultList();
    }


    public List<Quarter> getQuarterByProjectIdQuarterEnumAndYear(Long projectId, QuarterEnum quarterEnum, Integer year) {
        String jpql = "SELECT DISTINCT o " +
                " FROM " +
                " IndicatorExecution ie " +
                " inner join ie.quarters o " +
                " inner join fetch o.months " +
                "WHERE  ie.project.id=:projectId and o.quarter=:quarterEnum and o.year=:year ";
        Query q = getEntityManager().createQuery(jpql, Quarter.class);
        q.setParameter("projectId", projectId);
        q.setParameter("quarterEnum", quarterEnum);
        q.setParameter("year", year);
        return q.getResultList();
    }
}
