package org.unhcr.osmosys.daos;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.osmosys.model.Indicator;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class IndicatorDao extends GenericDaoJpa<Indicator, Long> {

    private static final String indicatorJpql =
            " SELECT DISTINCT o" +
                    " FROM Indicator o " +
                    " left outer join o.statement sta " +
                    " left join sta.periodStatementAsignations psa " +
                    " left outer join o.customDissagregationAssignationToIndicators cda " +
                    " left outer join cda.customDissagregation " +
                    " left outer join cda.customDissagregationFilterIndicators " +
                    " left outer join o.dissagregationsAssignationToIndicator da " +
                    " left join psa.period p ";

    public IndicatorDao() {
        super(Indicator.class, Long.class);
    }

    public List<Indicator> getByState(State state) {

        String jpql = IndicatorDao.indicatorJpql +
                "WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, Indicator.class);
        q.setParameter("state", state);
        return q.getResultList();
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

    public List<Indicator> getByPeriodAssignmentAndState(Long periodId, State state) {
        String jpql = IndicatorDao.indicatorJpql +
                " WHERE p.id = :periodId and psa.state =:state and o.state =:state";
        Query q = getEntityManager().createQuery(jpql, Indicator.class);
        q.setParameter("periodId", periodId);
        q.setParameter("state", state);
        return q.getResultList();

    }

    public Indicator findWithData(Long id) throws GeneralAppException {

        String jpql = IndicatorDao.indicatorJpql +
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

    public List<Indicator> getAllWithData() {

        Query q = getEntityManager().createQuery(IndicatorDao.indicatorJpql, Indicator.class);
        return q.getResultList();
    }

    public List<Indicator> getByCodeList(List<String> codeList) {
        String jpql = " SELECT DISTINCT o" +
                " FROM Indicator o " +
                " left outer join fetch o.statement sta " +
                " left outer join fetch  sta.area " +
                " left join fetch  sta.periodStatementAsignations psa " +
                " left outer join fetch o.customDissagregationAssignationToIndicators cda " +
                " left outer join fetch cda.customDissagregation " +
                " left outer join fetch cda.customDissagregationFilterIndicators " +
                " left outer join fetch o.dissagregationsAssignationToIndicator da " +
                " left join fetch psa.period p " +
                " WHERE o.code in (:codeList)";
        Query q = getEntityManager().createQuery(jpql, Indicator.class);
        q.setParameter("codeList", codeList);
        return q.getResultList();

    }

    public List<Indicator> getByPeriodYearAssignmentAndState(int year, State state) {
        String jpql = " SELECT DISTINCT o" +
                " FROM Indicator o " +
                " inner join fetch o.statement sta " +
                " inner join sta.periodStatementAsignations psa " +
                " inner join psa.period p " +
                " WHERE " +
                " o.state=:state " +
                " and sta.state=:state " +
                " and psa.state=:state " +
                " and psa.state=:state " +
                " and p.year =:year ";

        Query q = getEntityManager().createQuery(jpql, Indicator.class);
        q.setParameter("state", state);
        q.setParameter("year", year);
        return q.getResultList();
    }

    public Indicator getByPeriodAndCode(Long periodId, String code) throws GeneralAppException {
        String jpql = IndicatorDao.indicatorJpql +
                " WHERE p.id = :periodId and o.code =:code";
        Query q = getEntityManager().createQuery(jpql, Indicator.class);
        q.setParameter("periodId", periodId);
        q.setParameter("code", code);
        try {
            return (Indicator) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException(
                    "Se encontró más de un item con el código  " + code + " en el periodo " + periodId, Response.Status.INTERNAL_SERVER_ERROR);
        }

    }

    public Indicator getByCodeAndDescription(String code, String description) throws GeneralAppException {
        String jpql = IndicatorDao.indicatorJpql +
                " WHERE UPPER(o.code)=UPPER(:code) " +
                " and LOWER(o.description) =lower(:description) ";
        Query q = getEntityManager().createQuery(jpql, Indicator.class);
        q.setParameter("description", description);
        q.setParameter("code", code);
        try {
            return (Indicator) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontró más de un item con el código  " + code
                    + " y la descripción "+ description, Response.Status.INTERNAL_SERVER_ERROR);
        }

    }
}
