package org.unhcr.osmosys.daos;

import com.sagatechs.generics.exceptions.GeneralAppException;
import com.sagatechs.generics.persistence.GenericDaoJpa;
import com.sagatechs.generics.persistence.model.State;
import org.jboss.logging.Logger;
import org.unhcr.osmosys.model.Statement;
import org.unhcr.osmosys.model.enums.AreaType;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class StatementDao extends GenericDaoJpa<Statement, Long> {
    private final static Logger LOGGER = Logger.getLogger(StatementDao.class);
    public StatementDao() {
        super(Statement.class, Long.class);
    }

    public List<Statement> getByState(State state) {

        String jpql = "SELECT DISTINCT o FROM Statement o " +
                " left outer join fetch o.area " +
                " left outer join fetch o.pillar " +
                " left outer join fetch o.situation " +
                " left outer join fetch o.periodStatementAsignations psa " +
                " left outer join fetch psa.period pe " +
                " left outer join fetch pe.periodAgeDissagregationOptions " +
                " left outer join fetch pe.periodCountryOfOriginDissagregationOptions " +
                " left outer join fetch pe.periodDiversityDissagregationOptions " +
                " left outer join fetch pe.periodGenderDissagregationOptions " +
                " left outer join fetch pe.periodPopulationTypeDissagregationOptions " +
                " left outer join fetch pe.generalIndicator gi " +
                " left outer join fetch gi.dissagregationAssignationsToGeneralIndicator " +
                " WHERE o.state = :state";
        Query q = getEntityManager().createQuery(jpql, Statement.class);
        q.setParameter("state", state);
        return q.getResultList();
    }


    public Statement getByCode(String code) throws GeneralAppException {
        String jpql = "SELECT DISTINCT o FROM Statement o " +
                "WHERE lower(o.code) = lower(:code)";
        Query q = getEntityManager().createQuery(jpql, Statement.class);
        q.setParameter("code", code);
        try {
            return (Statement) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontró más de un item con el código  " + code, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Statement getByCodeAndPeriodYearAndAreaType(String code, AreaType areaType, int year) throws GeneralAppException {
        // LOGGER.debug(code);
        String jpql = "SELECT DISTINCT o" +
                " FROM Statement o" +
                " inner join  o.periodStatementAsignations psa " +
                " inner join  psa.period per  " +
                " WHERE lower(o.code) = lower(:code)" +
                " and o.areaType =:areType " +
                " and per.year = :year";
        Query q = getEntityManager().createQuery(jpql, Statement.class);
        q.setParameter("code", code);
        q.setParameter("year", year);
        q.setParameter("areType", areaType);


        try {
            return (Statement) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontró más de un item con el código  " + code + " y el año " + year, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Statement getByCodeAndDescriptionAndAreaType(String code, String description, AreaType areaType) throws GeneralAppException {
        String jpql = "SELECT DISTINCT o" +
                " FROM Statement o" +
                " WHERE lower(o.code) = lower(:code)" +
                " and o.areaType =:areType " +
                " and lower(o.description) = lower(:description)";
        Query q = getEntityManager().createQuery(jpql, Statement.class);
        q.setParameter("code", code);
        q.setParameter("description", description);
        q.setParameter("areType", areaType);
        try {
            return (Statement) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new GeneralAppException("Se encontró más de un item con el código  " + code + " y la descripción " + description, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public List<Statement> getByPeriodYearAndAreaType(AreaType areaType, int year){
        String jpql = "SELECT DISTINCT o" +
                " FROM Statement o" +
                " inner join  o.periodStatementAsignations psa " +
                " inner join  psa.period per  " +
                " WHERE " +
                " o.areaType =:areType " +
                " and per.year = :year" +
                " and o.state=:state " +
                " and per.state=:state ";
        Query q = getEntityManager().createQuery(jpql, Statement.class);
        q.setParameter("state", State.ACTIVO);
        q.setParameter("year", year);
        q.setParameter("areType", areaType);


        return q.getResultList();

    }
    public List<Statement> getByPeriodIdAndState(Long periodId, State state){
        String jpql = "SELECT DISTINCT o" +
                " FROM Statement o" +
                " left outer join fetch o.periodStatementAsignations psa " +
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
        Query q = getEntityManager().createQuery(jpql, Statement.class);
        q.setParameter("state", state);
        q.setParameter("periodId", periodId);
        return q.getResultList();

    }

    public List<Statement> getChildStatementsByParentId(Long parentId){
        String jpql = "SELECT DISTINCT o" +
                " FROM Statement o" +
                " WHERE " +
                " o.parentStatement.id =:parentId" +
                " and o.areaType =:areaType" +
                " and o.state=:state ";

        Query q = getEntityManager().createQuery(jpql, Statement.class);
        q.setParameter("state", State.ACTIVO);
        q.setParameter("parentId", parentId);
        q.setParameter("areaType", AreaType.PRODUCTO);
        return q.getResultList();
    }
}
