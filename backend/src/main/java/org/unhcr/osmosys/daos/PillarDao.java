package org.unhcr.osmosys.daos;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.Pillar;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class PillarDao extends GenericDaoJpa<Pillar, Long> {
    public PillarDao() {
        super(Pillar.class, Long.class);
    }

    public List<Pillar> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM Pillar o " +
                "WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, Pillar.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public Pillar getByCode(String code) throws GeneralAppException {

        String jpql = "SELECT DISTINCT o FROM Pillar o " +
                "WHERE lower(o.code) = lower(:code)";
        Query q = getEntityManager().createQuery(jpql, Pillar.class);
        q.setParameter("code", code);
        try {
            return (Pillar) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontró más de un item con el código " + code, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Pillar getByShortDescription(String shortDescription) throws GeneralAppException {

        String jpql = "SELECT DISTINCT o FROM Pillar o " +
                "WHERE lower(o.shortDescription) = lower(:shortDescription)";
        Query q = getEntityManager().createQuery(jpql, Pillar.class);
        q.setParameter("shortDescription", shortDescription);
        try {
            return (Pillar) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontró más de un item con la descripción corta " + shortDescription, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
