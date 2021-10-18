package org.unhcr.osmosys.daos;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.Area;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class AreaDao extends GenericDaoJpa<Area, Long> {
    public AreaDao() {
        super(Area.class, Long.class);
    }

    public List<Area> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM Area o " +
                "WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, Area.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public Area getByCode(String code) throws GeneralAppException {

        String jpql = "SELECT DISTINCT o FROM Area o " +
                "WHERE lower(o.code) = lower(:code)";
        Query q = getEntityManager().createQuery(jpql, Area.class);
        q.setParameter("code", code);
        try {
            return (Area) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontró más de un item con el código " + code, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Area getByShortDescription(String shortDescription) throws GeneralAppException {

        String jpql = "SELECT DISTINCT o FROM Area o " +
                "WHERE lower(o.shortDescription) = lower(:shortDescription)";
        Query q = getEntityManager().createQuery(jpql, Area.class);
        q.setParameter("shortDescription", shortDescription);
        try {
            return (Area) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontró más de un item con la descripción corta " + shortDescription, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }


}
