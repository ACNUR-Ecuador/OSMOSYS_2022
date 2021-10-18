package org.unhcr.osmosys.daos;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.Situation;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class SituationDao extends GenericDaoJpa<Situation, Long> {
    public SituationDao() {
        super(Situation.class, Long.class);
    }

    public List<Situation> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM Situation o " +
                "WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, Situation.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public Situation getByCode(String code) throws GeneralAppException {

        String jpql = "SELECT DISTINCT o FROM Situation o " +
                "WHERE lower(o.code) = lower(:code)";
        Query q = getEntityManager().createQuery(jpql, Situation.class);
        q.setParameter("code", code);
        try {
            return (Situation) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontró más de un ítem con el código " + code, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Situation getByShortDescription(String shortDescription) throws GeneralAppException {

        String jpql = "SELECT DISTINCT o FROM Situation o " +
                "WHERE lower(o.shortDescription) = lower(:shortDescription)";
        Query q = getEntityManager().createQuery(jpql, Situation.class);
        q.setParameter("shortDescription", shortDescription);
        try {
            return (Situation) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontró más de un item con la descripción corta " + shortDescription, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Situation getByDescription(String description) throws GeneralAppException {

        String jpql = "SELECT DISTINCT o FROM Situation o " +
                "WHERE lower(o.description) = lower(:description)";
        Query q = getEntityManager().createQuery(jpql, Situation.class);
        q.setParameter("description", description);
        try {
            return (Situation) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontró más de un item con la descripción  " + description, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }


}
