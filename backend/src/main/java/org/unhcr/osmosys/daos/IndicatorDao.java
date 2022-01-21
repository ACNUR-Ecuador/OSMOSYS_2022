package org.unhcr.osmosys.daos;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.Indicator;
import org.unhcr.osmosys.model.enums.AreaType;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class IndicatorDao extends GenericDaoJpa<Indicator, Long> {
    public IndicatorDao() {
        super(Indicator.class, Long.class);
    }

    public List<Indicator> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM Indicator o " +
                "WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, Indicator.class);
        q.setParameter("state", state);
        return q.getResultList();
    }

    public List<Indicator> getByAreaType(AreaType areaType) throws GeneralAppException {

        String jpql = "SELECT DISTINCT o FROM Indicator o " +
                "WHERE o.areaType = :areaType ";
        Query q = getEntityManager().createQuery(jpql, Indicator.class);
        q.setParameter("areaType", areaType);

        return q.getResultList();

    }


    public Indicator getByDescription(String description) throws GeneralAppException {

        String jpql = "SELECT DISTINCT o FROM Indicator o " +
                "WHERE lower(o.description) = lower(:description)";
        Query q = getEntityManager().createQuery(jpql, Indicator.class);
        q.setParameter("description", description);
        try {
            return (Indicator) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontró más de un item con la descripción  " + description, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }


    public Indicator getByCode(String code) throws GeneralAppException {
        String jpql = "SELECT DISTINCT o FROM Indicator o " +
                "WHERE lower(o.code) = lower(:code)";
        Query q = getEntityManager().createQuery(jpql, Indicator.class);
        q.setParameter("code", code);
        try {
            return (Indicator) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontró más de un item con el código  " + code, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public List<Indicator> getByPeriodAssignmentAndState(Long periodId, State state) throws GeneralAppException {
        String jpql = "SELECT DISTINCT o FROM Indicator o " +
                " left join o.statements sta " +
                " left join sta.periodStatementAsignations psa " +
                " left join psa.period p " +
                " WHERE p.id = :periodId and psa.state =:state and o.state =:state";
        Query q = getEntityManager().createQuery(jpql, Indicator.class);
        q.setParameter("periodId", periodId);
        q.setParameter("state", state);
        return q.getResultList();

    }

    public Indicator findWithData(Long id) throws GeneralAppException {

        String jpql = "SELECT DISTINCT o" +
                " FROM Indicator o " +
                " left outer join o.statements sts "+
                " left outer join o.customDissagregationAssignationToIndicators cda "+
                " left outer join cda.customDissagregation "+
                " left outer join cda.customDissagregationFilterIndicators "+
                " left outer join o.dissagregationsAssignationToIndicator da "+
                " left outer join da.dissagregationFilterIndicators "+
                " left outer join o.markers "+
                " WHERE o.id=:id";
        Query q = getEntityManager().createQuery(jpql, Indicator.class);
        q.setParameter("id", id);
        try {
            return (Indicator) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontró más de un item con el id  " + id, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
