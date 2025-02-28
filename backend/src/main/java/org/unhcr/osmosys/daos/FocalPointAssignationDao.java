package org.unhcr.osmosys.daos;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.FocalPointAssignation;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class FocalPointAssignationDao extends GenericDaoJpa<FocalPointAssignation, Long> {
    public FocalPointAssignationDao() {
        super(FocalPointAssignation.class, Long.class);
    }

    public List<FocalPointAssignation> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM FocalPointAssignation o " +
                "WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, FocalPointAssignation.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public FocalPointAssignation getById(Long id) throws GeneralAppException {

        String jpql = "SELECT DISTINCT o FROM FocalPointAssignation o " +
                "WHERE lower(o.id) = lower(:id)";
        Query q = getEntityManager().createQuery(jpql, FocalPointAssignation.class);
        q.setParameter("id", id);
        try {
            return (FocalPointAssignation) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontró más de un item con el código " + id, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
