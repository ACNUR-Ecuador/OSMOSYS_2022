package org.unhcr.osmosys.daos;

import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.Period;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class PeriodDao extends GenericDaoJpa<Period, Long> {
    public PeriodDao() {
        super(Period.class, Long.class);
    }

    public List<Period> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM Period o " +
                "WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, Period.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public Period getByYear(Integer year) {

        String jpql = "SELECT DISTINCT o FROM Period o " +
                " WHERE o.year = :year";
        Query q = getEntityManager().createQuery(jpql, Period.class);
        q.setParameter("year", year);
        try {
            return (Period) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Period getWithGeneralIndicatorById(Long id)  {

        String jpql = "SELECT DISTINCT o FROM Period o left join fetch o.generalIndicator gi " +
                " WHERE o.id = :id";
        Query q = getEntityManager().createQuery(jpql, Period.class);
        q.setParameter("id", id);
        try {
            return (Period) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }



    public List<Period> getAllWithDissagregationOptions()  {

        String jpql = "SELECT DISTINCT o FROM Period o left join fetch o.generalIndicator gi " +
                " left join fetch o.periodPopulationTypeDissagregationOptions " +
                " left join fetch o.periodAgeDissagregationOptions " +
                " left join fetch o.periodGenderDissagregationOptions " +
                " left join fetch o.periodDiversityDissagregationOptions " +
                " left join fetch o.periodCountryOfOriginDissagregationOptions " +
                " order by o.year";
        Query q = getEntityManager().createQuery(jpql, Period.class);
        return q.getResultList();
    }
    public Period getWithDissagregationOptionsById(Long id)  {

        String jpql = "SELECT DISTINCT o FROM Period o left join fetch o.generalIndicator gi " +
                " left join fetch o.periodPopulationTypeDissagregationOptions " +
                " left join fetch o.periodAgeDissagregationOptions " +
                " left join fetch o.periodGenderDissagregationOptions " +
                " left join fetch o.periodDiversityDissagregationOptions " +
                " left join fetch o.periodCountryOfOriginDissagregationOptions " +
                " WHERE o.id = :id " +
                " order by o.year";
        Query q = getEntityManager().createQuery(jpql, Period.class);
        q.setParameter("id", id);
        try {
            return (Period) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
