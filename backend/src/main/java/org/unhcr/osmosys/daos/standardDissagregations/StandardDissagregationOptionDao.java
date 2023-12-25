package org.unhcr.osmosys.daos.standardDissagregations;


import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.standardDissagregations.options.*;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

/**
 * Dao Genérico
 *
 */
@SuppressWarnings("unchecked")
@Stateless
public class StandardDissagregationOptionDao extends GenericDaoJpa<StandardDissagregationOption, Long> {

    public StandardDissagregationOptionDao() {
        super(StandardDissagregationOption.class, Long.class);

    }



    public List<StandardDissagregationOption> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM " + this.getEntityClass().getSimpleName() + " o " +
                " WHERE o.state = :state" +
                " order by o.order ASC";
        Query q = getEntityManager().createQuery(jpql, StandardDissagregationOption.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public List<AgeDissagregationOption> getAgeOptionsByState(State state) {

        String jpql = "SELECT DISTINCT o FROM AgeOption o " +
                " WHERE o.state = :state" +
                " order by o.order ASC";
        Query q = getEntityManager().createQuery(jpql, StandardDissagregationOption.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public List<PopulationTypeDissagregationOption> getPopulationTypeOptionsByState(State state) {

        String jpql = "SELECT DISTINCT o FROM PopulationTypeOption o " +
                " WHERE o.state = :state" +
                " order by o.order ASC";
        Query q = getEntityManager().createQuery(jpql, StandardDissagregationOption.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public List<GenderDissagregationOption> getGenderOptionsByState(State state) {

        String jpql = "SELECT DISTINCT o FROM GenderOption o " +
                " WHERE o.state = :state" +
                " order by o.order ASC";
        Query q = getEntityManager().createQuery(jpql, StandardDissagregationOption.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public List<DiversityDissagregationOption> getDiversityOptionsByState(State state) {

        String jpql = "SELECT DISTINCT o FROM DiversityOption o " +
                " WHERE o.state = :state" +
                " order by o.order ASC";
        Query q = getEntityManager().createQuery(jpql, StandardDissagregationOption.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public List<CountryOfOriginDissagregationOption> getCountryOfOriginOptionsByState(State state) {

        String jpql = "SELECT DISTINCT o FROM CountryOfOriginOption o " +
                " WHERE o.state = :state" +
                " order by o.order ASC";
        Query q = getEntityManager().createQuery(jpql, StandardDissagregationOption.class);
        q.setParameter("state", state);
        return q.getResultList();
    }


}
