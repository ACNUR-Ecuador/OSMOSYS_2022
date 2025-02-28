package org.unhcr.osmosys.daos;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.Area;
import org.unhcr.osmosys.model.TesterBaseEntity;
import org.unhcr.osmosys.model.enums.AreaType;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class TesterDao extends GenericDaoJpa<TesterBaseEntity, Long> {
    public TesterDao() {
        super(TesterBaseEntity.class, Long.class);
    }

    public List<TesterBaseEntity> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM TesterBaseEntity o " +
                "WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, TesterBaseEntity.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public TesterBaseEntity getByCode(String code) throws GeneralAppException {

        String jpql = "SELECT DISTINCT o FROM TesterBaseEntity o " +
                "WHERE lower(o.code) = lower(:code)";
        Query q = getEntityManager().createQuery(jpql, TesterBaseEntity.class);
        q.setParameter("code", code);
        try {
            return (TesterBaseEntity) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontró más de un item con el código " + code, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }


}
