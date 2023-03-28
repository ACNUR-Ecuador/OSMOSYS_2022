package org.unhcr.osmosys.daos;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.Organization;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.List;

@Stateless
public class OrganizationDao extends GenericDaoJpa<Organization, Long> {
    public OrganizationDao() {
        super(Organization.class, Long.class);
    }


    @SuppressWarnings("unchecked")
    public List<Organization> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM Organization o " +
                "WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, Organization.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public Organization getByCode(String code) throws GeneralAppException {

        String jpql = "SELECT DISTINCT o FROM Organization o " +
                "WHERE lower(o.code) = lower(:code)";
        Query q = getEntityManager().createQuery(jpql, Organization.class);
        q.setParameter("code", code);
        try {
            return (Organization) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontró más de un ítem con el código " + code, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Organization getByAcronym(String acronym) throws GeneralAppException {

        String jpql = "SELECT DISTINCT o FROM Organization o " +
                "WHERE lower(o.acronym) = lower(:acronym)";
        Query q = getEntityManager().createQuery(jpql, Organization.class);
        q.setParameter("acronym", acronym);
        try {
            return (Organization) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontró más de un item con el acrónimo " + acronym, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Organization getByDescription(String description) throws GeneralAppException {

        String jpql = "SELECT DISTINCT o FROM Organization o " +
                "WHERE lower(o.description) = lower(:description)";
        Query q = getEntityManager().createQuery(jpql, Organization.class);
        q.setParameter("description", description);
        try {
            return (Organization) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontró más de un item con la descripción  " + description, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Organization> getByPeriodId(Long periodId) {

        String jpql = "SELECT DISTINCT o FROM Project pr " +
                " inner join pr.organization o " +
                " WHERE pr.state = :state" +
                " and o.state=:state " +
                " and pr.period.id =:periodId";
        Query q = getEntityManager().createQuery(jpql, Organization.class);
        q.setParameter("state", State.ACTIVO);
        q.setParameter("periodId", periodId);
        return q.getResultList();
    }

}
