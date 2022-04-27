package org.unhcr.osmosys.daos;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.GeneralIndicator;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class GeneralIndicatorDao extends GenericDaoJpa<GeneralIndicator, Long> {
    public GeneralIndicatorDao() {
        super(GeneralIndicator.class, Long.class);
    }

    public List<GeneralIndicator> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM GeneralIndicator o " +
                "WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, GeneralIndicator.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public GeneralIndicator getByIdAndState(Long periodId, State state) {

        String jpql = "SELECT DISTINCT o FROM GeneralIndicator o left join fetch o.dissagregationAssignationsToGeneralIndicator dissa " +
                "WHERE o.period.id =:periodId and o.state = :state";
        Query q = getEntityManager().createQuery(jpql, GeneralIndicator.class);
        q.setParameter("state", state);
        q.setParameter("periodId", periodId);
        try {
            return (GeneralIndicator) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }
    public GeneralIndicator getById(Long periodId) {

        String jpql = "SELECT DISTINCT o FROM GeneralIndicator o left join fetch o.dissagregationAssignationsToGeneralIndicator dissa " +
                "WHERE o.period.id =:periodId ";
        Query q = getEntityManager().createQuery(jpql, GeneralIndicator.class);
        q.setParameter("periodId", periodId);
        try {
            return (GeneralIndicator) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    public GeneralIndicator getByPeriodId(Long periodId) throws GeneralAppException {

        String jpql = "SELECT DISTINCT o FROM GeneralIndicator o " +
                "WHERE o.period.id =: periodId";
        Query q = getEntityManager().createQuery(jpql, GeneralIndicator.class);
        q.setParameter("periodId", periodId);
        try {
            return (GeneralIndicator) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Inconsistencia en base de datos, se encontro màs de un indicador general para el periodo " + periodId);
        }
    }

}
