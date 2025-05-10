package org.unhcr.osmosys.daos;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.Statement;
import org.unhcr.osmosys.model.Tags;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class TagsDao extends GenericDaoJpa<Tags, Long> {
    public TagsDao() {
        super(Tags.class, Long.class);
    }
    public List<Tags> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM Tags o " +
                "WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, Tags.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public Tags getByName(String name) throws GeneralAppException {
        String jpql = "SELECT DISTINCT o FROM Tags o " +
                "WHERE lower(o.name) = lower(:name)";
        Query q = getEntityManager().createQuery(jpql, Tags.class);
        q.setParameter("name", name);
        try {
            return (Tags) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontró más de un item con el nombre  " + name, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public List<Tags> getByPeriodIdAndState(Long periodId, State state){
        String jpql = "SELECT DISTINCT o" +
                " FROM Tags o" +
                " left outer join fetch o.periodTagAssignations psa " +
                " left outer join fetch  psa.period p  " +
                " left outer join fetch p.periodPopulationTypeDissagregationOptions " +
                " left outer join fetch p.periodGenderDissagregationOptions " +
                " left outer join fetch p.periodDiversityDissagregationOptions " +
                " left outer join fetch p.periodCountryOfOriginDissagregationOptions " +
                " left outer join fetch p.periodAgeDissagregationOptions "+
                " WHERE " +
                " p.id = :periodId" +
                " and o.state=:state " +
                " and p.state=:state ";
        Query q = getEntityManager().createQuery(jpql, Tags.class);
        q.setParameter("state", state);
        q.setParameter("periodId", periodId);
        return q.getResultList();

    }
    public List<Tags> getTagsByIndicatorIdAndPeriodId(Long indicatorId, Long periodId) throws GeneralAppException {
        State state=State.ACTIVO;
        String jpql = "SELECT DISTINCT o" +
                " FROM Tags o" +
                " left outer join fetch o.periodTagAssignations psa " +
                " left outer join fetch o.indicatorTagAssignations ita " +
                " left outer join fetch ita.indicator i " +
                " left outer join fetch  psa.period p  " +
                " left outer join fetch p.periodPopulationTypeDissagregationOptions " +
                " left outer join fetch p.periodGenderDissagregationOptions " +
                " left outer join fetch p.periodDiversityDissagregationOptions " +
                " left outer join fetch p.periodCountryOfOriginDissagregationOptions " +
                " left outer join fetch p.periodAgeDissagregationOptions "+
                " WHERE " +
                " p.id = :periodId" +
                " and o.state=:state " +
                " and i.id=:indicatorId " +
                " and ita.state=:state " +
                " and i.state=:state " +
                " and p.state=:state ";
        Query q = getEntityManager().createQuery(jpql, Tags.class);
        q.setParameter("state", state);
        q.setParameter("periodId", periodId);
        q.setParameter("indicatorId", indicatorId);
        return q.getResultList();

    }






}
